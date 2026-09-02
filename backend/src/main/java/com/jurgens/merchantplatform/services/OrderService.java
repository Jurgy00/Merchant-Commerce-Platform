
        package com.jurgens.merchantplatform.services;

import com.jurgens.merchantplatform.entities.Order;
import com.jurgens.merchantplatform.entities.OrderItem;
import com.jurgens.merchantplatform.exceptions.ResourceNotFoundException;
import com.jurgens.merchantplatform.repositories.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Order createOrder(Order order) {

        for (OrderItem item : order.getItems()) {
            item.setOrder(order);
        }

        return orderRepository.save(order);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Order not found with id: " + id));
    }
}

