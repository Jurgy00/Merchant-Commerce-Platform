package com.merchant.backend;

import com.merchant.backend.entities.*;
import com.merchant.backend.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final MerchantRepository merchantRepository;
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    @Override
    public void run(String... args) {
        if (merchantRepository.count() > 0) return;

        // Merchant
        Merchant merchant = new Merchant();
        merchant.setName("Bookstore");
        merchant.setDescription("Your neighbourhood bookstore, now online.");
        merchant.setEmail("hello@bookstore.co.ke");
        merchant.setPhone("0742098841");
        merchantRepository.save(merchant);

        // Categories
        Category fiction = new Category();
        fiction.setName("Fiction");
        fiction.setSlug("fiction");
        fiction.setMerchant(merchant);
        categoryRepository.save(fiction);

        Category nonFiction = new Category();
        nonFiction.setName("Non-Fiction");
        nonFiction.setSlug("non-fiction");
        nonFiction.setMerchant(merchant);
        categoryRepository.save(nonFiction);

        Category children = new Category();
        children.setName("Children");
        children.setSlug("children");
        children.setMerchant(merchant);
        categoryRepository.save(children);

        Category textbooks = new Category();
        textbooks.setName("Textbooks");
        textbooks.setSlug("textbooks");
        textbooks.setMerchant(merchant);
        categoryRepository.save(textbooks);

        Category biography = new Category();
        biography.setName("Biography");
        biography.setSlug("biography");
        biography.setMerchant(merchant);
        categoryRepository.save(biography);

        // Products
        saveProduct("Things Fall Apart", "Chinua Achebe's landmark novel about pre-colonial Igbo society.", new BigDecimal("850.00"), 12, "https://covers.openlibrary.org/b/isbn/9780385474542-L.jpg", fiction);
        saveProduct("Petals of Blood", "Ngugi wa Thiong'o's story of four villagers in post-independence Kenya.", new BigDecimal("950.00"), 8, "https://covers.openlibrary.org/b/isbn/9780143104827-L.jpg", fiction);
        saveProduct("Half of a Yellow Sun", "Chimamanda Ngozi Adichie brings the Nigerian Civil War to life.", new BigDecimal("1100.00"), 5, "https://covers.openlibrary.org/b/isbn/9781400095209-L.jpg", fiction);
        saveProduct("Americanah", "A razor-sharp look at race, identity, and love.", new BigDecimal("1050.00"), 7, "https://covers.openlibrary.org/b/isbn/9780307455925-L.jpg", fiction);
        saveProduct("The Alchemist", "Paulo Coelho's beloved fable about following your dreams.", new BigDecimal("800.00"), 18, "https://covers.openlibrary.org/b/isbn/9780062315007-L.jpg", fiction);
        saveProduct("Long Walk to Freedom", "Nelson Mandela's autobiography.", new BigDecimal("1200.00"), 6, "https://covers.openlibrary.org/b/isbn/9780316548182-L.jpg", biography);
        saveProduct("I Am Malala", "The story of the girl who stood up for education.", new BigDecimal("950.00"), 9, "https://covers.openlibrary.org/b/isbn/9780316322423-L.jpg", biography);
        saveProduct("Sapiens", "Yuval Noah Harari's sweeping account of humankind's history.", new BigDecimal("1350.00"), 4, "https://covers.openlibrary.org/b/isbn/9780062316097-L.jpg", nonFiction);
        saveProduct("Atomic Habits", "James Clear's proven framework for building good habits.", new BigDecimal("1150.00"), 15, "https://covers.openlibrary.org/b/isbn/9780735211292-L.jpg", nonFiction);
        saveProduct("Business Accounting 101", "A practical introduction to bookkeeping for Kenyan businesses.", new BigDecimal("1800.00"), 3, "https://covers.openlibrary.org/b/isbn/9780470533475-L.jpg", textbooks);
        saveProduct("Diary of a Wimpy Kid", "Greg Heffley's hilarious diary of middle-school survival.", new BigDecimal("650.00"), 22, "https://covers.openlibrary.org/b/isbn/9780810993136-L.jpg", children);
    }

    private void saveProduct(String name, String description, BigDecimal price, int stock, String imageUrl, Category category) {
        Product product = new Product();
        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);
        product.setStockQuantity(stock);
        product.setImageUrl(imageUrl);
        product.setCategory(category);
        productRepository.save(product);
    }
}