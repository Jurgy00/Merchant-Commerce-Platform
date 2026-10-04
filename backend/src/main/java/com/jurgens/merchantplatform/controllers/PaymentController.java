
        package com.jurgens.merchantplatform.controllers;

import com.jurgens.merchantplatform.dto.PaymentRequest;
import com.jurgens.merchantplatform.dto.PaymentResponse;
import com.jurgens.merchantplatform.services.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/stk-push")
    public ResponseEntity<PaymentResponse> initiateStkPush(
            @RequestBody PaymentRequest request
    ) {

        return ResponseEntity.ok(
                paymentService.initiatePayment(request)
        );
    }

    @GetMapping
    public ResponseEntity<List<PaymentResponse>> getAllPayments() {

        return ResponseEntity.ok(
                paymentService.getAllPayments()
        );
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<PaymentResponse>> getPaymentsByOrderId(
            @PathVariable Long orderId
    ) {

        return ResponseEntity.ok(
                paymentService.getPaymentsByOrderId(orderId)
        );
    }

    @PostMapping("/mpesa/callback")
    public ResponseEntity<Void> mpesaCallback(
            @RequestBody Map<String, Object> callback
    ) {

        System.out.println("M-Pesa Callback Received:");
        System.out.println(callback);

        paymentService.processMpesaCallback(callback);

        return ResponseEntity.ok().build();
    }
}

