package com.jurgens.merchantplatform.controllers;

import com.jurgens.merchantplatform.entities.Payment;
import com.jurgens.merchantplatform.services.PaymentService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/stk-push")
    public Payment initiateStkPush(
            @RequestParam Long orderId,
            @RequestParam String phoneNumber
    ) {

        return paymentService.initiatePayment(
                orderId,
                phoneNumber
        );
    }
    @GetMapping
    public java.util.List<Payment> getAllPayments() {
        return paymentService.getAllPayments();
    }
    @PostMapping("/mpesa/callback")
    public String mpesaCallback(
            @RequestBody Map<String, Object> callback
    ) {

        System.out.println("M-Pesa Callback Received:");
        System.out.println(callback);

        paymentService.processMpesaCallback(callback);

        return "Callback received";
    }
}