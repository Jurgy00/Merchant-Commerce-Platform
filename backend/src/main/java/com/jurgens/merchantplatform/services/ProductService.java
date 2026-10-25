
package com.jurgens.merchantplatform.services;

import com.jurgens.merchantplatform.dto.CreateProductRequest;
import com.jurgens.merchantplatform.dto.ProductResponse;
import com.jurgens.merchantplatform.dto.UpdateProductRequest;
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

    public ProductService(
            ProductRepository productRepository,
            CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    public ProductResponse createProduct(CreateProductRequest request) {

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Category not found with id: "
                                        + request.getCategoryId()
                        ));

        Product product = new Product();

        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setStockQuantity(request.getStockQuantity());
        product.setImageUrl(request.getImageUrl());
        product.setCategory(category);

        Product savedProduct = productRepository.save(product);

        return convertToResponse(savedProduct);
    }

    public List<ProductResponse> getAllProducts() {

        List<String> bookstoreCategories = List.of(
                "Fiction",
                "Technology",
                "Business",
                "Self Development",
                "Children"
        );

        return productRepository.findByCategory_NameIn(bookstoreCategories)
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    public ProductResponse getProductById(Long id) {

        Product product = getProductEntityById(id);

        return convertToResponse(product);
    }

    public List<ProductResponse> getProductsByCategory(Long categoryId) {

        return productRepository.findByCategoryId(categoryId)
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    public ProductResponse updateProduct(
            Long id,
            UpdateProductRequest request) {

        Product existingProduct = getProductEntityById(id);

        if (request.getName() != null) {
            existingProduct.setName(request.getName());
        }

        if (request.getDescription() != null) {
            existingProduct.setDescription(request.getDescription());
        }

        if (request.getPrice() != null) {
            existingProduct.setPrice(request.getPrice());
        }

        if (request.getStockQuantity() != null) {
            existingProduct.setStockQuantity(request.getStockQuantity());
        }

        if (request.getImageUrl() != null) {
            existingProduct.setImageUrl(request.getImageUrl());
        }

        Product updatedProduct = productRepository.save(existingProduct);

        return convertToResponse(updatedProduct);
    }

    public void deleteProduct(Long id) {

        Product product = getProductEntityById(id);

        productRepository.delete(product);
    }

    private Product getProductEntityById(Long id) {

        return productRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Product not found with id: " + id
                        ));
    }

    private ProductResponse convertToResponse(Product product) {

        ProductResponse response = new ProductResponse();

        response.setId(product.getId());
        response.setName(product.getName());
        response.setDescription(product.getDescription());
        response.setPrice(product.getPrice());
        response.setStockQuantity(product.getStockQuantity());
        response.setImageUrl(product.getImageUrl());

        Category category = product.getCategory();

        response.setCategoryId(category.getId());
        response.setCategoryName(category.getName());
        response.setCategoryDescription(category.getDescription());

        return response;
    }
}
