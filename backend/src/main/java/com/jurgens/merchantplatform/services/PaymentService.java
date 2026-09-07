package com.jurgens.merchantplatform.services;

import com.jurgens.merchantplatform.entities.Order;
import com.jurgens.merchantplatform.entities.Payment;
import com.jurgens.merchantplatform.entities.PaymentStatus;
import com.jurgens.merchantplatform.repositories.OrderRepository;
import com.jurgens.merchantplatform.repositories.PaymentRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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

    public Payment initiatePayment(Long orderId, String phoneNumber) {

        // 1. Find the order
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() ->
                        new RuntimeException("Order not found")
                );

        // 2. Get the amount from the order
        BigDecimal amount = order.getTotalAmount();

        // 3. Create payment
        Payment payment = new Payment();

        payment.setOrder(order);
        payment.setAmount(amount);
        payment.setPhoneNumber(phoneNumber);
        payment.setStatus(PaymentStatus.PENDING);
        payment.setCreatedAt(LocalDateTime.now());

        // 4. Save payment
        payment = paymentRepository.save(payment);

        // 5. Send STK Push
        MpesaService.StkPushResponse response =
                mpesaService.initiateStkPush(
                        amount.intValue(),
                        phoneNumber,
                        order.getOrderNumber()
                );

        // 6. Save MerchantRequestID
        payment.setMerchantRequestId(
                response.MerchantRequestID()
        );

        // 7. Save CheckoutRequestID
        payment.setCheckoutRequestId(
                response.CheckoutRequestID()
        );

        // 8. Save the Daraja result information
        payment.setResultCode(
                Integer.valueOf(response.ResponseCode())
        );

        payment.setResultDesc(
                response.ResponseDescription()
        );

        // 9. Save updated payment
        return paymentRepository.save(payment);
    }
}