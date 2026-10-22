package com.jurgens.merchantplatform.dto;

import com.jurgens.merchantplatform.entities.OrderStatus;
import com.jurgens.merchantplatform.entities.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrderResponse(
        Long id,
        String orderReference,
        String customerName,
        String phoneNumber,
        String deliveryAddress,
        String optionalNotes,
        BigDecimal totalAmount,
        OrderStatus orderStatus,
        PaymentStatus paymentStatus,
        LocalDateTime createdAt,
        List<OrderItemResponse> items
) {
}
