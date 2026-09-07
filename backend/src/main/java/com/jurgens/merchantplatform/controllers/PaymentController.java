package com.jurgens.merchantplatform.controllers;

import com.jurgens.merchantplatform.entities.Payment;
import com.jurgens.merchantplatform.services.PaymentService;
import org.springframework.web.bind.annotation.*;

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
    @PostMapping("/mpesa/callback")
    public String mpesaCallback(@RequestBody String callback) {

        System.out.println("M-Pesa Callback Received:");
        System.out.println(callback);

        return "Callback received";
    }
}