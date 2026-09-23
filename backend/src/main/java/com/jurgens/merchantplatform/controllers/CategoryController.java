package com.jurgens.merchantplatform.controllers;

import com.jurgens.merchantplatform.dto.CategoryResponse;
import com.jurgens.merchantplatform.dto.CreateCategoryRequest;
import com.jurgens.merchantplatform.dto.UpdateCategoryRequest;
import com.jurgens.merchantplatform.services.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping("/api/merchants/{merchantId}/categories")
    public ResponseEntity<CategoryResponse> createCategory(
            @PathVariable Long merchantId,
            @RequestBody CreateCategoryRequest request) {

        CategoryResponse created =
                categoryService.createCategory(merchantId, request);

        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/api/merchants/{merchantId}/categories")
    public ResponseEntity<List<CategoryResponse>> getCategoriesByMerchant(
            @PathVariable Long merchantId) {

        return ResponseEntity.ok(
                categoryService.getCategoriesByMerchant(merchantId)
        );
    }

    @GetMapping("/api/categories/{id}")
    public ResponseEntity<CategoryResponse> getCategoryById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                categoryService.getCategoryById(id)
        );
    }

    @PutMapping("/api/categories/{id}")
    public ResponseEntity<CategoryResponse> updateCategory(
            @PathVariable Long id,
            @RequestBody UpdateCategoryRequest request) {

        return ResponseEntity.ok(
                categoryService.updateCategory(id, request)
        );
    }

    @DeleteMapping("/api/categories/{id}")
    public ResponseEntity<Void> deleteCategory(
            @PathVariable Long id) {

        categoryService.deleteCategory(id);

        return ResponseEntity.noContent().build();
    }
}