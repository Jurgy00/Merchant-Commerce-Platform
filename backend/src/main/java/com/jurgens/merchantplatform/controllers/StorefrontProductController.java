
package com.jurgens.merchantplatform.controllers;

import com.jurgens.merchantplatform.dto.ProductResponse;
import com.jurgens.merchantplatform.entities.Category;
import com.jurgens.merchantplatform.entities.Product;
import com.jurgens.merchantplatform.repositories.ProductRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/storefront/products")
public class StorefrontProductController {

    private static final String BOOKSTORE_NAME = "Merchant Bookstore";

    private final ProductRepository productRepository;

    public StorefrontProductController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    private ProductResponse toResponse(Product product) {
        Category category = product.getCategory();

        ProductResponse.CategorySummary categorySummary =
                category == null
                        ? null
                        : new ProductResponse.CategorySummary(
                                category.getId(),
                                category.getName()
                        );

        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getStockQuantity(),
                product.getImageUrl(),
                product.getAuthor(),
                categorySummary
        );
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> getBooks() {
        List<ProductResponse> books = productRepository
                .findByCategory_Merchant_BusinessName(BOOKSTORE_NAME)
                .stream()
                .map(this::toResponse)
                .toList();

        return ResponseEntity.ok(books);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getBook(@PathVariable Long id) {
        Product book = productRepository
                .findByIdAndCategory_Merchant_BusinessName(
                        id,
                        BOOKSTORE_NAME
                )
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Book not found"
                ));

        return ResponseEntity.ok(toResponse(book));
    }
}
