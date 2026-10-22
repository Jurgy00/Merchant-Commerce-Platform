
package com.jurgens.merchantplatform.repositories;

import com.jurgens.merchantplatform.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByCategoryId(Long categoryId);

    Optional<Product> findByNameAndCategoryId(String name, Long categoryId);

    List<Product> findByCategory_Merchant_BusinessName(String businessName);

    Optional<Product> findByIdAndCategory_Merchant_BusinessName(
            Long id,
            String businessName
    );
}
