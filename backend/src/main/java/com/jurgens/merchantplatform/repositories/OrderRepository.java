package com.jurgens.merchantplatform.repositories;

import com.jurgens.merchantplatform.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    Optional<Order> findByOrderReference(String orderReference);

    boolean existsByOrderReference(String orderReference);
}
