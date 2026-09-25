package com.jurgens.merchantplatform.services;

import com.jurgens.merchantplatform.dto.CreateOrderRequest;
import com.jurgens.merchantplatform.dto.OrderItemRequest;
import com.jurgens.merchantplatform.dto.OrderItemResponse;
import com.jurgens.merchantplatform.dto.OrderResponse;
import com.jurgens.merchantplatform.entities.Merchant;
import com.jurgens.merchantplatform.entities.Order;
import com.jurgens.merchantplatform.entities.OrderItem;
import com.jurgens.merchantplatform.entities.OrderStatus;
import com.jurgens.merchantplatform.entities.Product;
import com.jurgens.merchantplatform.exceptions.ResourceNotFoundException;
import com.jurgens.merchantplatform.repositories.MerchantRepository;
import com.jurgens.merchantplatform.repositories.OrderRepository;
import com.jurgens.merchantplatform.repositories.ProductRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final MerchantRepository merchantRepository;

    public OrderService(OrderRepository orderRepository,
                        ProductRepository productRepository,
                        MerchantRepository merchantRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.merchantRepository = merchantRepository;
    }

    public OrderResponse createOrder(CreateOrderRequest request) {

        Merchant merchant = merchantRepository.findById(request.getMerchantId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Merchant not found with id: " + request.getMerchantId()
                        ));

        Order order = new Order();

        order.setCustomerName(request.getCustomerName());
        order.setPhoneNumber(request.getPhoneNumber());
        order.setDeliveryLocation(request.getDeliveryLocation());
        order.setNotes(request.getNotes());
        order.setMerchant(merchant);

        BigDecimal totalAmount = BigDecimal.ZERO;

        for (OrderItemRequest itemRequest : request.getItems()) {

            Product product = productRepository.findById(itemRequest.getProductId())
                    .orElseThrow(() ->
                            new ResourceNotFoundException(
                                    "Product not found with id: " + itemRequest.getProductId()
                            ));

            OrderItem item = new OrderItem();

            item.setOrder(order);
            item.setProduct(product);
            item.setQuantity(itemRequest.getQuantity());
            item.setUnitPrice(product.getPrice());

            BigDecimal subtotal = product.getPrice()
                    .multiply(BigDecimal.valueOf(itemRequest.getQuantity()));

            item.setSubtotal(subtotal);

            order.getItems().add(item);

            totalAmount = totalAmount.add(subtotal);
        }

        order.setTotalAmount(totalAmount);
        order.setCreatedAt(LocalDateTime.now());
        order.setOrderNumber("ORD-" + System.currentTimeMillis());
        order.setStatus(OrderStatus.PENDING);

        Order savedOrder = orderRepository.save(order);

        return convertToResponse(savedOrder);
    }

    public List<OrderResponse> getAllOrders() {

        return orderRepository.findAll()
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    public OrderResponse getOrderById(Long id) {

        Order order = orderRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Order not found with id: " + id
                        ));

        return convertToResponse(order);
    }

    private OrderResponse convertToResponse(Order order) {

        OrderResponse response = new OrderResponse();

        response.setId(order.getId());
        response.setOrderNumber(order.getOrderNumber());
        response.setCustomerName(order.getCustomerName());
        response.setPhoneNumber(order.getPhoneNumber());
        response.setDeliveryLocation(order.getDeliveryLocation());
        response.setNotes(order.getNotes());
        response.setTotalAmount(order.getTotalAmount());
        response.setStatus(order.getStatus());
        response.setCreatedAt(order.getCreatedAt());
        response.setMerchantId(order.getMerchant().getId());

        List<OrderItemResponse> itemResponses = order.getItems()
                .stream()
                .map(this::convertItemToResponse)
                .toList();

        response.setItems(itemResponses);

        return response;
    }

    private OrderItemResponse convertItemToResponse(OrderItem item) {

        OrderItemResponse response = new OrderItemResponse();

        response.setProductId(item.getProduct().getId());
        response.setProductName(item.getProduct().getName());
        response.setQuantity(item.getQuantity());
        response.setUnitPrice(item.getUnitPrice());
        response.setSubtotal(item.getSubtotal());

        return response;
    }
}