package com.jurgens.merchantplatform.repositories;

import com.jurgens.merchantplatform.entities.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
