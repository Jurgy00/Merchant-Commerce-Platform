package com.jurgens.merchantplatform.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.List;

public record CreateOrderRequest(

        @NotBlank(message = "Customer name is required")
        @Size(max = 150, message = "Customer name must not exceed 150 characters")
        String customerName,

        @NotBlank(message = "Phone number is required")
        @Size(max = 30, message = "Phone number is too long")
        String phoneNumber,

        @NotBlank(message = "Delivery address is required")
        @Size(max = 1000, message = "Delivery address must not exceed 1000 characters")
        String deliveryAddress,

        @Size(max = 1000, message = "Optional notes must not exceed 1000 characters")
        String optionalNotes,

        @NotEmpty(message = "An order must contain at least one item")
        @Valid
        List<OrderItemRequest> items

) {
}
