package com.jurgens.merchantplatform.services;

import com.jurgens.merchantplatform.dto.CategoryResponse;
import com.jurgens.merchantplatform.dto.CreateCategoryRequest;
import com.jurgens.merchantplatform.dto.UpdateCategoryRequest;
import com.jurgens.merchantplatform.entities.Category;
import com.jurgens.merchantplatform.entities.Merchant;
import com.jurgens.merchantplatform.exceptions.ResourceNotFoundException;
import com.jurgens.merchantplatform.repositories.CategoryRepository;
import com.jurgens.merchantplatform.repositories.MerchantRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final MerchantRepository merchantRepository;

    public CategoryService(CategoryRepository categoryRepository,
                           MerchantRepository merchantRepository) {
        this.categoryRepository = categoryRepository;
        this.merchantRepository = merchantRepository;
    }

    public CategoryResponse createCategory(
            Long merchantId,
            CreateCategoryRequest request) {

        Merchant merchant = merchantRepository.findById(merchantId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Merchant not found with id: " + merchantId
                        ));

        Category category = new Category();

        category.setName(request.getName());
        category.setDescription(request.getDescription());
        category.setMerchant(merchant);

        Category savedCategory = categoryRepository.save(category);

        return convertToResponse(savedCategory);
    }

    public List<CategoryResponse> getCategoriesByMerchant(Long merchantId) {

        return categoryRepository.findByMerchantId(merchantId)
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    public CategoryResponse getCategoryById(Long id) {

        Category category = getCategoryEntityById(id);

        return convertToResponse(category);
    }

    public CategoryResponse updateCategory(
            Long id,
            UpdateCategoryRequest request) {

        Category existingCategory = getCategoryEntityById(id);

        existingCategory.setName(request.getName());
        existingCategory.setDescription(request.getDescription());

        Category updatedCategory = categoryRepository.save(existingCategory);

        return convertToResponse(updatedCategory);
    }

    public void deleteCategory(Long id) {

        Category category = getCategoryEntityById(id);

        categoryRepository.delete(category);
    }

    private Category getCategoryEntityById(Long id) {

        return categoryRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Category not found with id: " + id
                        ));
    }

    private CategoryResponse convertToResponse(Category category) {

        CategoryResponse response = new CategoryResponse();

        response.setId(category.getId());
        response.setName(category.getName());
        response.setDescription(category.getDescription());
        response.setMerchantId(category.getMerchant().getId());

        return response;
    }
}