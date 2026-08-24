package com.jurgens.merchantplatform.repositories;

import com.jurgens.merchantplatform.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
