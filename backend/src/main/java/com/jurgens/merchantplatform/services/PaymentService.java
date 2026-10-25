
package com.jurgens.merchantplatform.services;

import com.jurgens.merchantplatform.dto.PaymentRequest;
import com.jurgens.merchantplatform.dto.PaymentResponse;
import com.jurgens.merchantplatform.entities.Order;
import com.jurgens.merchantplatform.entities.OrderStatus;
import com.jurgens.merchantplatform.entities.Payment;
import com.jurgens.merchantplatform.entities.PaymentStatus;
import com.jurgens.merchantplatform.exceptions.ResourceNotFoundException;
import com.jurgens.merchantplatform.repositories.OrderRepository;
import com.jurgens.merchantplatform.repositories.PaymentRepository;
import com.jurgens.merchantplatform.repositories.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final MpesaService mpesaService;
    private final ProductRepository productRepository;

    public PaymentService(
            PaymentRepository paymentRepository,
            OrderRepository orderRepository,
            MpesaService mpesaService,
            ProductRepository productRepository
    ) {
        this.paymentRepository = paymentRepository;
        this.orderRepository = orderRepository;
        this.mpesaService = mpesaService;
        this.productRepository = productRepository;
    }

    @Transactional
    public PaymentResponse initiatePayment(PaymentRequest request) {

        if (request == null || request.getOrderId() == null) {
            throw new IllegalArgumentException("Order ID is required");
        }

        Long orderId = request.getOrderId();
        String phoneNumber = request.getPhoneNumber();

        if (phoneNumber == null || phoneNumber.isBlank()) {
            throw new IllegalArgumentException("Phone number is required");
        }

        phoneNumber = phoneNumber.trim();

        if (phoneNumber.startsWith("0")) {
            phoneNumber = "254" + phoneNumber.substring(1);
        } else if (phoneNumber.startsWith("+254")) {
            phoneNumber = phoneNumber.substring(1);
        }

        if (!phoneNumber.matches("254(7|1)[0-9]{8}")) {
            throw new IllegalArgumentException("Invalid Kenyan phone number");
        }

        Order order = orderRepository.findByIdForUpdate(orderId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Order not found")
                );

        if (order.getStatus() == OrderStatus.CANCELLED) {
            throw new IllegalStateException(
                    "Cannot initiate payment for a cancelled order"
            );
        }

        if (order.getStatus() == OrderStatus.CONFIRMED) {
            throw new IllegalStateException(
                    "Order has already been confirmed"
            );
        }

        Payment existingPayment = paymentRepository
                .findByOrderIdAndStatus(orderId, PaymentStatus.PENDING)
                .orElse(null);

        if (existingPayment != null) {
            throw new IllegalStateException(
                    "A payment is already pending for this order"
            );
        }

        BigDecimal amount = order.getTotalAmount();

        if (amount == null || amount.signum() <= 0) {
            throw new IllegalStateException(
                    "Order total must be greater than zero"
            );
        }

        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setAmount(amount);
        payment.setPhoneNumber(phoneNumber);
        payment.setStatus(PaymentStatus.PENDING);
        payment.setCreatedAt(LocalDateTime.now());

        payment = paymentRepository.saveAndFlush(payment);

        MpesaService.StkPushResponse response =
                mpesaService.initiateStkPush(
                        amount.intValue(),
                        phoneNumber,
                        order.getOrderNumber()
                );

        payment.setMerchantRequestId(response.MerchantRequestID());
        payment.setCheckoutRequestId(response.CheckoutRequestID());
        payment.setResultCode(Integer.valueOf(response.ResponseCode()));
        payment.setResultDesc(response.ResponseDescription());

        Payment savedPayment = paymentRepository.save(payment);

        return convertToResponse(savedPayment);
    }

    @Transactional
    public void processMpesaCallback(Map<String, Object> callback) {

        if (callback == null || callback.get("Body") == null) {
            throw new IllegalArgumentException("Invalid M-Pesa callback");
        }

        Map<String, Object> body =
                (Map<String, Object>) callback.get("Body");

        Map<String, Object> stkCallback =
                (Map<String, Object>) body.get("stkCallback");

        if (stkCallback == null
                || stkCallback.get("CheckoutRequestID") == null
                || !(stkCallback.get("ResultCode") instanceof Number)) {
            throw new IllegalArgumentException(
                    "Invalid M-Pesa STK callback data"
            );
        }

        String checkoutRequestId =
                (String) stkCallback.get("CheckoutRequestID");

        int resultCode =
                ((Number) stkCallback.get("ResultCode")).intValue();

        String resultDesc = (String) stkCallback.get("ResultDesc");

        Payment payment = paymentRepository
                .findByCheckoutRequestId(checkoutRequestId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Payment not found")
                );

        if (payment.getStatus() == PaymentStatus.SUCCESS
                || payment.getStatus() == PaymentStatus.FAILED) {
            return;
        }

        payment.setResultCode(resultCode);
        payment.setResultDesc(resultDesc);

        if (resultCode != 0) {
            payment.setStatus(PaymentStatus.FAILED);
            paymentRepository.save(payment);
            return;
        }

        Map<String, Object> callbackMetadata =
                (Map<String, Object>) stkCallback.get("CallbackMetadata");

        if (callbackMetadata == null
                || !(callbackMetadata.get("Item") instanceof List<?>)) {
            throw new IllegalArgumentException(
                    "Successful callback is missing transaction metadata"
            );
        }

        List<Map<String, Object>> items =
                (List<Map<String, Object>>) callbackMetadata.get("Item");

        BigDecimal callbackAmount = null;
        String receiptNumber = null;
        LocalDateTime transactionDate = null;

        for (Map<String, Object> item : items) {
            String name = (String) item.get("Name");
            Object value = item.get("Value");

            if ("Amount".equals(name) && value instanceof Number) {
                callbackAmount = BigDecimal.valueOf(
                        ((Number) value).doubleValue()
                );
            } else if ("MpesaReceiptNumber".equals(name)
                    && value instanceof String) {
                receiptNumber = (String) value;
            } else if ("TransactionDate".equals(name) && value != null) {
                transactionDate = LocalDateTime.parse(
                        value.toString(),
                        DateTimeFormatter.ofPattern("yyyyMMddHHmmss")
                );
            }
        }

        if (callbackAmount == null) {
            throw new IllegalArgumentException(
                    "Successful callback is missing the transaction amount"
            );
        }

        if (receiptNumber == null || receiptNumber.isBlank()) {
            throw new IllegalArgumentException(
                    "Successful callback is missing the M-Pesa receipt number"
            );
        }

        if (callbackAmount.compareTo(payment.getAmount()) != 0) {
            throw new IllegalArgumentException(
                    "M-Pesa callback amount does not match payment amount"
            );
        }

        // Combine quantities for duplicate products and sort by product ID.
        Map<Long, Integer> requiredQuantities = new TreeMap<>();

        for (var item : payment.getOrder().getItems()) {
            Long productId = item.getProduct().getId();

            requiredQuantities.merge(
                    productId,
                    item.getQuantity(),
                    Integer::sum
            );
        }

        // Lock products in ID order and verify all stock before deducting.
        Map<Long, com.jurgens.merchantplatform.entities.Product>
                lockedProducts = new TreeMap<>();

        for (var entry : requiredQuantities.entrySet()) {
            var product = productRepository.findByIdForUpdate(entry.getKey())
                    .orElseThrow(() ->
                            new ResourceNotFoundException(
                                    "Product not found with id: " + entry.getKey()
                            )
                    );

            if (product.getStockQuantity() < entry.getValue()) {
                throw new IllegalStateException(
                        "Insufficient stock for product: " + product.getName()
                );
            }

            lockedProducts.put(entry.getKey(), product);
        }

        // Deduct stock only after every product passes validation.
        for (var entry : requiredQuantities.entrySet()) {
            var product = lockedProducts.get(entry.getKey());

            product.setStockQuantity(
                    product.getStockQuantity() - entry.getValue()
            );

            productRepository.save(product);
        }

        payment.setMpesaReceiptNumber(receiptNumber);
        payment.setTransactionDate(transactionDate);
        payment.setStatus(PaymentStatus.SUCCESS);
        payment.getOrder().setStatus(OrderStatus.CONFIRMED);

        orderRepository.save(payment.getOrder());
        paymentRepository.save(payment);
    }

    public List<PaymentResponse> getAllPayments() {
        return paymentRepository.findAll()
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    public List<PaymentResponse> getPaymentsByOrderId(Long orderId) {
        return paymentRepository.findByOrderId(orderId)
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    private PaymentResponse convertToResponse(Payment payment) {
        PaymentResponse response = new PaymentResponse();

        response.setId(payment.getId());
        response.setOrderId(payment.getOrder().getId());
        response.setAmount(payment.getAmount());
        response.setPhoneNumber(payment.getPhoneNumber());
        response.setStatus(payment.getStatus());
        response.setMpesaReceiptNumber(payment.getMpesaReceiptNumber());
        response.setCheckoutRequestId(payment.getCheckoutRequestId());
        response.setResultDesc(payment.getResultDesc());

        return response;
    }
}
