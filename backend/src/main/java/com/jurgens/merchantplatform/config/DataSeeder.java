package com.jurgens.merchantplatform.config;

import com.jurgens.merchantplatform.entities.Category;
import com.jurgens.merchantplatform.entities.Merchant;
import com.jurgens.merchantplatform.entities.Product;
import com.jurgens.merchantplatform.repositories.CategoryRepository;
import com.jurgens.merchantplatform.repositories.MerchantRepository;
import com.jurgens.merchantplatform.repositories.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class DataSeeder implements CommandLineRunner {

    private final MerchantRepository merchantRepository;
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    public DataSeeder(MerchantRepository merchantRepository,
                      CategoryRepository categoryRepository,
                      ProductRepository productRepository) {
        this.merchantRepository = merchantRepository;
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (merchantRepository.count() > 0) {
            return;
        }

        Merchant merchant = new Merchant();
        merchant.setBusinessName("Jurgens Electronics");
        merchant.setEmail("contact@jurgensielectronics.co.ke");
        merchant.setPhoneNumber("0742098841");
        merchantRepository.save(merchant);

        Category phones = new Category();
        phones.setName("Phones");
        phones.setDescription("Smartphones and mobile accessories");
        phones.setMerchant(merchant);
        categoryRepository.save(phones);

        Product phone = new Product();
        phone.setName("Tecno Spark 20");
        phone.setDescription("6.6-inch display, 128GB storage, 8GB RAM");
        phone.setPrice(new BigDecimal("18999.00"));
        phone.setStockQuantity(15);
        phone.setImageUrl("https://placehold.co/400x400?text=Tecno+Spark+20");
        phone.setCategory(phones);
        productRepository.save(phone);
    }
}
