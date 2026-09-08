package com.jurgens.merchantplatform.services;

import com.jurgens.merchantplatform.entities.Order;
import com.jurgens.merchantplatform.entities.OrderStatus;
import com.jurgens.merchantplatform.entities.Payment;
import com.jurgens.merchantplatform.entities.PaymentStatus;
import com.jurgens.merchantplatform.repositories.OrderRepository;
import com.jurgens.merchantplatform.repositories.PaymentRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final MpesaService mpesaService;

    public PaymentService(
            PaymentRepository paymentRepository,
            OrderRepository orderRepository,
            MpesaService mpesaService
    ) {
        this.paymentRepository = paymentRepository;
        this.orderRepository = orderRepository;
        this.mpesaService = mpesaService;
    }

    public Payment initiatePayment(
            Long orderId,
            String phoneNumber
    ) {

        // Find the order
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() ->
                        new RuntimeException("Order not found")
                );

        // Get the amount from the order
        BigDecimal amount = order.getTotalAmount();

        // Create payment
        Payment payment = new Payment();

        payment.setOrder(order);
        payment.setAmount(amount);
        payment.setPhoneNumber(phoneNumber);
        payment.setStatus(PaymentStatus.PENDING);
        payment.setCreatedAt(LocalDateTime.now());

        // Save payment
        payment = paymentRepository.save(payment);

        // Send STK Push
        MpesaService.StkPushResponse response =
                mpesaService.initiateStkPush(
                        amount.intValue(),
                        phoneNumber,
                        order.getOrderNumber()
                );

        // Save MerchantRequestID
        payment.setMerchantRequestId(
                response.MerchantRequestID()
        );

        // Save CheckoutRequestID
        payment.setCheckoutRequestId(
                response.CheckoutRequestID()
        );

        // Save Daraja result
        payment.setResultCode(
                Integer.valueOf(response.ResponseCode())
        );

        payment.setResultDesc(
                response.ResponseDescription()
        );

        // Save updated payment
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

        // Find the payment using CheckoutRequestID
        Payment payment =
                paymentRepository.findByCheckoutRequestId(
                        checkoutRequestId
                ).orElseThrow(() ->
                        new RuntimeException("Payment not found")
                );
        // Ignore duplicate callbacks
        if (payment.getStatus() == PaymentStatus.SUCCESS
                || payment.getStatus() == PaymentStatus.FAILED) {

            return;
        }

        // Save M-Pesa result
        payment.setResultCode(resultCode);
        payment.setResultDesc(resultDesc);

        // Check whether payment was successful
        if (resultCode == 0) {

            payment.setStatus(PaymentStatus.SUCCESS);

            // Confirm the order
            payment.getOrder().setStatus(OrderStatus.CONFIRMED);

            orderRepository.save(payment.getOrder());

        } else {

            // Payment failed
            payment.setStatus(PaymentStatus.FAILED);
        }

        // Save payment
        paymentRepository.save(payment);
    }

    public java.util.List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }
}