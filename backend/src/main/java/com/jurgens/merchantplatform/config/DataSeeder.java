
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
import java.util.List;

@Component
public class DataSeeder implements CommandLineRunner {

    private final MerchantRepository merchantRepository;
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    public DataSeeder(
            MerchantRepository merchantRepository,
            CategoryRepository categoryRepository,
            ProductRepository productRepository
    ) {
        this.merchantRepository = merchantRepository;
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
    }

    @Override
    public void run(String... args) {
        seedElectronics();
        seedBookstore();
    }

    private void seedElectronics() {
        Merchant merchant = merchantRepository
                .findByEmail("contact@jurgensielectronics.co.ke")
                .orElseGet(() -> {
                    Merchant created = new Merchant();
                    created.setBusinessName("Jurgens Electronics");
                    created.setEmail("contact@jurgensielectronics.co.ke");
                    created.setPhoneNumber("0742098841");
                    return merchantRepository.save(created);
                });

        Category phones = getOrCreateCategory(
                merchant,
                "Phones",
                "Smartphones and mobile accessories"
        );

        if (productRepository.findByNameAndCategoryId(
                "Tecno Spark 20", phones.getId()
        ).isEmpty()) {
            Product phone = new Product();
            phone.setName("Tecno Spark 20");
            phone.setDescription(
                    "6.6-inch display, 128GB storage, 8GB RAM"
            );
            phone.setPrice(new BigDecimal("18999.00"));
            phone.setStockQuantity(15);
            phone.setImageUrl(
                    "https://placehold.co/400x400?text=Tecno+Spark+20"
            );
            phone.setCategory(phones);
            productRepository.save(phone);
        }
    }

    private void seedBookstore() {
        Merchant bookstore = merchantRepository
                .findByEmail("bookstore@merchantplatform.local")
                .orElseGet(() -> {
                    Merchant created = new Merchant();
                    created.setBusinessName("Merchant Bookstore");
                    created.setEmail("bookstore@merchantplatform.local");
                    created.setPhoneNumber("");
                    return merchantRepository.save(created);
                });

        Category selfDevelopment = getOrCreateCategory(
                bookstore,
                "Self Development",
                "Books for personal growth and better habits"
        );

        Category technology = getOrCreateCategory(
                bookstore,
                "Technology",
                "Software development, programming and technology"
        );

        Category business = getOrCreateCategory(
                bookstore,
                "Business",
                "Business, productivity and financial thinking"
        );

        Category fiction = getOrCreateCategory(
                bookstore,
                "Fiction",
                "Fiction, novels and thought-provoking stories"
        );

        seedBook(
                "Atomic Habits", "James Clear",
                "A practical guide to building good habits and breaking bad ones.",
                "1850.00", 12, "/books/atomic-habits.jpg", selfDevelopment
        );

        seedBook(
                "Clean Code", "Robert C. Martin",
                "Principles and practices for writing readable, maintainable software.",
                "3200.00", 5, "/books/clean-code.jpg", technology
        );

        seedBook(
                "Deep Work", "Cal Newport",
                "Strategies for focused work in a world full of distractions.",
                "1950.00", 8, "/books/deep-work.jpg", business
        );

        seedBook(
                "The Psychology of Money", "Morgan Housel",
                "Stories about how people think about money, wealth and risk.",
                "2100.00", 10, "/books/psychology-money.jpg", business
        );

        seedBook(
                "1984", "George Orwell",
                "A dystopian novel about surveillance, power and freedom.",
                "1250.00", 18, "/books/1984.jpg", fiction
        );

        seedBook(
                "Sapiens", "Yuval Noah Harari",
                "An exploration of human history and the development of civilization.",
                "2400.00", 9, "/books/sapiens.jpg", fiction
        );

        seedBook(
                "The Alchemist", "Paulo Coelho",
                "A philosophical novel about pursuing a personal dream.",
                "1350.00", 15, "/books/alchemist.jpg", fiction
        );

        seedBook(
                "Rich Dad Poor Dad", "Robert T. Kiyosaki",
                "Lessons on financial literacy, assets and wealth building.",
                "1600.00", 11, "/books/rich-dad-poor-dad.jpg", business
        );

        seedBook(
                "Start with Why", "Simon Sinek",
                "How purpose can inspire people and organizations.",
                "1850.00", 7, "/books/start-with-why.jpg", business
        );

        seedBook(
                "Don't Make Me Think", "Steve Krug",
                "A practical introduction to intuitive web usability and design.",
                "2900.00", 4, "/books/dont-make-me-think.jpg", technology
        );
    }

    private Category getOrCreateCategory(
            Merchant merchant,
            String name,
            String description
    ) {
        List<Category> categories =
                categoryRepository.findByMerchantId(merchant.getId());

        return categories.stream()
                .filter(category -> category.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElseGet(() -> {
                    Category category = new Category();
                    category.setName(name);
                    category.setDescription(description);
                    category.setMerchant(merchant);
                    return categoryRepository.save(category);
                });
    }

    private void seedBook(
            String name,
            String author,
            String description,
            String price,
            int stock,
            String imageUrl,
            Category category
    ) {
        if (productRepository.findByNameAndCategoryId(
                name, category.getId()
        ).isPresent()) {
            return;
        }

        Product book = new Product();
        book.setName(name);
        book.setAuthor(author);
        book.setDescription(description);
        book.setPrice(new BigDecimal(price));
        book.setStockQuantity(stock);
        book.setImageUrl(imageUrl);
        book.setCategory(category);

        productRepository.save(book);
    }
}

