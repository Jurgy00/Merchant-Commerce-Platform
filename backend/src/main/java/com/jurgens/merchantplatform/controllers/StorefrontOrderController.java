package com.jurgens.merchantplatform.controllers;

import com.jurgens.merchantplatform.dto.CreateOrderRequest;
import com.jurgens.merchantplatform.dto.OrderResponse;
import com.jurgens.merchantplatform.services.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/storefront/orders")
public class StorefrontOrderController {

    private final OrderService orderService;

    public StorefrontOrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(
            @Valid @RequestBody CreateOrderRequest request
    ) {
        OrderResponse response = orderService.createOrder(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }
}
