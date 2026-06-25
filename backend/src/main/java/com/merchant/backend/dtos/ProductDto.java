package com.merchant.backend.dtos;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class ProductDto {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer stockQuantity;
    private String imageUrl;
    private CategoryDto category;

    @Data
    public static class CategoryDto {
        private Long id;
        private String name;
        private String slug;
    }
}