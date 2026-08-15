
package com.jurgens.merchantplatform.services;

import com.jurgens.merchantplatform.entities.Category;
import com.jurgens.merchantplatform.entities.Product;
import com.jurgens.merchantplatform.exceptions.ResourceNotFoundException;
import com.jurgens.merchantplatform.repositories.CategoryRepository;
import com.jurgens.merchantplatform.repositories.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    public Product createProduct(Long categoryId, Product product) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + categoryId));

        product.setCategory(category);
        return productRepository.save(product);
    }

    public List<Product> getProductsByCategory(Long categoryId) {
        return productRepository.findByCategoryId(categoryId);
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
    }

    public Product updateProduct(Long id, Product productDetails) {
        Product existingProduct = getProductById(id);

        existingProduct.setName(productDetails.getName());
        existingProduct.setDescription(productDetails.getDescription());
        existingProduct.setPrice(productDetails.getPrice());
        existingProduct.setStockQuantity(productDetails.getStockQuantity());
        existingProduct.setImageUrl(productDetails.getImageUrl());

        return productRepository.save(existingProduct);
    }

    public void deleteProduct(Long id) {
        Product product = getProductById(id);
        productRepository.delete(product);
    }
}