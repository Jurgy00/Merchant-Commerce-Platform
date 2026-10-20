package com.jurgens.merchantplatform.dto;

import java.math.BigDecimal;

public record ProductResponse(
        Long id,
        String name,
        String description,
        BigDecimal price,
        Integer stockQuantity,
        String imageUrl,
        CategorySummary category
) {
    public record CategorySummary(
            Long id,
            String name
    ) {}
}
