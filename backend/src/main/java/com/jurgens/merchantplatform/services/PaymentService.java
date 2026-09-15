
        package com.jurgens.merchantplatform.services;

import com.jurgens.merchantplatform.entities.Order;
import com.jurgens.merchantplatform.entities.OrderStatus;
import com.jurgens.merchantplatform.entities.Payment;
import com.jurgens.merchantplatform.entities.PaymentStatus;
import com.jurgens.merchantplatform.exceptions.ResourceNotFoundException;
import com.jurgens.merchantplatform.repositories.OrderRepository;
import com.jurgens.merchantplatform.repositories.PaymentRepository;
import com.jurgens.merchantplatform.repositories.ProductRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

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

    public Payment initiatePayment(
            Long orderId,
            String phoneNumber
    ) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Order not found")
                );

        BigDecimal amount = order.getTotalAmount();

        Payment payment = new Payment();

        payment.setOrder(order);
        payment.setAmount(amount);
        payment.setPhoneNumber(phoneNumber);
        payment.setStatus(PaymentStatus.PENDING);
        payment.setCreatedAt(LocalDateTime.now());

        payment = paymentRepository.save(payment);

        MpesaService.StkPushResponse response =
                mpesaService.initiateStkPush(
                        amount.intValue(),
                        phoneNumber,
                        order.getOrderNumber()
                );

        payment.setMerchantRequestId(
                response.MerchantRequestID()
        );

        payment.setCheckoutRequestId(
                response.CheckoutRequestID()
        );

        payment.setResultCode(
                Integer.valueOf(response.ResponseCode())
        );

        payment.setResultDesc(
                response.ResponseDescription()
        );

        return paymentRepository.save(payment);
    }

    public void processMpesaCallback(
            Map<String, Object> callback
    ) {

        Map<String, Object> body =
                (Map<String, Object>) callback.get("Body");

        Map<String, Object> stkCallback =
                (Map<String, Object>) body.get("stkCallback");

        String checkoutRequestId =
                (String) stkCallback.get("CheckoutRequestID");

        Integer resultCode =
                ((Number) stkCallback.get("ResultCode")).intValue();

        String resultDesc =
                (String) stkCallback.get("ResultDesc");

        Payment payment =
                paymentRepository.findByCheckoutRequestId(
                        checkoutRequestId
                ).orElseThrow(() ->
                        new ResourceNotFoundException("Payment not found")
                );

        if (payment.getStatus() == PaymentStatus.SUCCESS
                || payment.getStatus() == PaymentStatus.FAILED) {

            return;
        }

        payment.setResultCode(resultCode);
        payment.setResultDesc(resultDesc);

        if (resultCode == 0) {

            Map<String, Object> callbackMetadata =
                    (Map<String, Object>)
                            stkCallback.get("CallbackMetadata");

            if (callbackMetadata != null) {

                List<Map<String, Object>> items =
                        (List<Map<String, Object>>)
                                callbackMetadata.get("Item");

                if (items != null) {

                    for (Map<String, Object> item : items) {

                        String name =
                                (String) item.get("Name");

                        Object value =
                                item.get("Value");

                        if ("Amount".equals(name)) {

                            BigDecimal callbackAmount =
                                    new BigDecimal(String.valueOf(value));

                            if (callbackAmount.compareTo(
                                    payment.getAmount()) != 0) {

                                throw new RuntimeException(
                                        "Payment amount does not match order amount"
                                );
                            }
                        }

                        if ("MpesaReceiptNumber".equals(name)) {

                            String receiptNumber =
                                    String.valueOf(value);

                            Payment existingPayment =
                                    paymentRepository
                                            .findByMpesaReceiptNumber(
                                                    receiptNumber
                                            )
                                            .orElse(null);

                            if (existingPayment != null
                                    && !existingPayment.getId()
                                    .equals(payment.getId())) {

                                return;
                            }

                            payment.setMpesaReceiptNumber(
                                    receiptNumber
                            );
                        }

                        if ("TransactionDate".equals(name)) {

                            String transactionDate =
                                    String.valueOf(value);

                            DateTimeFormatter formatter =
                                    DateTimeFormatter.ofPattern(
                                            "yyyyMMddHHmmss"
                                    );

                            payment.setTransactionDate(
                                    LocalDateTime.parse(
                                            transactionDate,
                                            formatter
                                    )
                            );
                        }
                    }
                }
            }

            for (var item : payment.getOrder().getItems()) {

                var product = item.getProduct();

                int newStock =
                        product.getStockQuantity() - item.getQuantity();

                if (newStock < 0) {
                    throw new RuntimeException(
                            "Insufficient stock for product: "
                                    + product.getName()
                    );
                }

                product.setStockQuantity(newStock);

                productRepository.save(product);
            }

            payment.setStatus(PaymentStatus.SUCCESS);

            payment.getOrder().setStatus(
                    OrderStatus.CONFIRMED
            );

            orderRepository.save(
                    payment.getOrder()
            );

        } else {

            payment.setStatus(
                    PaymentStatus.FAILED
            );
        }

        paymentRepository.save(payment);
    }

    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    public List<Payment> getPaymentsByOrderId(Long orderId) {
        return paymentRepository.findByOrderId(orderId);
    }
}