package com.jurgens.merchantplatform.services;

import com.jurgens.merchantplatform.dto.CreateOrderRequest;
import com.jurgens.merchantplatform.dto.OrderItemRequest;
import com.jurgens.merchantplatform.dto.OrderItemResponse;
import com.jurgens.merchantplatform.dto.OrderResponse;
import com.jurgens.merchantplatform.entities.Order;
import com.jurgens.merchantplatform.entities.OrderItem;
import com.jurgens.merchantplatform.entities.OrderStatus;
import com.jurgens.merchantplatform.entities.PaymentStatus;
import com.jurgens.merchantplatform.entities.Product;
import com.jurgens.merchantplatform.exceptions.ResourceNotFoundException;
import com.jurgens.merchantplatform.repositories.OrderRepository;
import com.jurgens.merchantplatform.repositories.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class OrderService {

    private static final String BOOKSTORE_NAME = "Merchant Bookstore";

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    public OrderService(
            OrderRepository orderRepository,
            ProductRepository productRepository
    ) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public OrderResponse createOrder(CreateOrderRequest request) {
        Order order = new Order();
        order.setOrderReference(generateOrderReference());
        order.setCustomerName(request.customerName().trim());
        order.setPhoneNumber(request.phoneNumber().trim());
        order.setDeliveryAddress(request.deliveryAddress().trim());
        order.setOptionalNotes(
                request.optionalNotes() == null
                        || request.optionalNotes().isBlank()
                        ? null
                        : request.optionalNotes().trim()
        );
        order.setOrderStatus(OrderStatus.PENDING);
        order.setPaymentStatus(PaymentStatus.PENDING);

        BigDecimal totalAmount = BigDecimal.ZERO;

        Set<Long> requestedProductIds = new HashSet<>();

        for (OrderItemRequest requestedItem : request.items()) {
            if (!requestedProductIds.add(requestedItem.productId())) {
                throw new IllegalArgumentException(
                        "A book can only appear once in an order: "
                                + requestedItem.productId()
                );
            }

            Product product = productRepository
                    .findByIdAndCategory_Merchant_BusinessName(
                            requestedItem.productId(),
                            BOOKSTORE_NAME
                    )
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Book not found in this bookstore: "
                                    + requestedItem.productId()
                    ));

            int quantity = requestedItem.quantity();

            if (product.getStockQuantity() == null
                    || quantity > product.getStockQuantity()) {
                throw new IllegalArgumentException(
                        "Insufficient stock for book: " + product.getName()
                );
            }

            BigDecimal unitPrice = product.getPrice();
            BigDecimal lineTotal = unitPrice.multiply(
                    BigDecimal.valueOf(quantity)
            );

            OrderItem item = new OrderItem();
            item.setProductId(product.getId());
            item.setProductName(product.getName());
            item.setQuantity(quantity);
            item.setUnitPrice(unitPrice);
            item.setLineTotal(lineTotal);

            order.addItem(item);
            totalAmount = totalAmount.add(lineTotal);
        }

        order.setTotalAmount(totalAmount);

        Order savedOrder = orderRepository.save(order);

        return toResponse(savedOrder);
    }

    private String generateOrderReference() {
        return "BOOK-" + UUID.randomUUID()
                .toString()
                .replace("-", "")
                .substring(0, 12)
                .toUpperCase();
    }

    private OrderResponse toResponse(Order order) {
        List<OrderItemResponse> items = new ArrayList<>();

        for (OrderItem item : order.getItems()) {
            items.add(new OrderItemResponse(
                    item.getProductId(),
                    item.getProductName(),
                    item.getQuantity(),
                    item.getUnitPrice(),
                    item.getLineTotal()
            ));
        }

        return new OrderResponse(
                order.getId(),
                order.getOrderReference(),
                order.getCustomerName(),
                order.getPhoneNumber(),
                order.getDeliveryAddress(),
                order.getOptionalNotes(),
                order.getTotalAmount(),
                order.getOrderStatus(),
                order.getPaymentStatus(),
                order.getCreatedAt(),
                items
        );
    }
}
