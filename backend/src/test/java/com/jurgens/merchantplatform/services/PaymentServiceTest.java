
package com.jurgens.merchantplatform.services;
import com.jurgens.merchantplatform.dto.PaymentRequest;
import org.junit.jupiter.api.Assertions;
import com.jurgens.merchantplatform.entities.Order;
import com.jurgens.merchantplatform.entities.OrderItem;
import com.jurgens.merchantplatform.entities.OrderStatus;
import com.jurgens.merchantplatform.entities.Payment;
import com.jurgens.merchantplatform.entities.PaymentStatus;
import com.jurgens.merchantplatform.entities.Product;
import com.jurgens.merchantplatform.repositories.OrderRepository;
import com.jurgens.merchantplatform.repositories.PaymentRepository;
import com.jurgens.merchantplatform.repositories.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class PaymentServiceTest {

    private PaymentRepository paymentRepository;
    private OrderRepository orderRepository;
    private MpesaService mpesaService;
    private ProductRepository productRepository;
    private PaymentService paymentService;

    @BeforeEach
    void setUp() {
        paymentRepository = mock(PaymentRepository.class);
        orderRepository = mock(OrderRepository.class);
        mpesaService = mock(MpesaService.class);
        productRepository = mock(ProductRepository.class);

        paymentService = new PaymentService(
                paymentRepository,
                orderRepository,
                mpesaService,
                productRepository
        );
    }

    @Test
    void duplicateCallbackForSuccessfulPaymentDoesNotProcessAgain() {
        Payment payment = new Payment();
        payment.setStatus(PaymentStatus.SUCCESS);

        when(paymentRepository.findByCheckoutRequestIdForUpdate("checkout-123"))
                .thenReturn(Optional.of(payment));

        Map<String, Object> stkCallback = new HashMap<>();
        stkCallback.put("CheckoutRequestID", "checkout-123");
        stkCallback.put("ResultCode", 0);

        Map<String, Object> body = new HashMap<>();
        body.put("stkCallback", stkCallback);

        Map<String, Object> callback = new HashMap<>();
        callback.put("Body", body);

        paymentService.processMpesaCallback(callback);

        verify(paymentRepository)
                .findByCheckoutRequestIdForUpdate("checkout-123");
        verify(paymentRepository, never()).save(any(Payment.class));
        verifyNoInteractions(productRepository);
    }

    @Test
    void duplicateCallbackForFailedPaymentDoesNotProcessAgain() {
        Payment payment = new Payment();
        payment.setStatus(PaymentStatus.FAILED);

        when(paymentRepository.findByCheckoutRequestIdForUpdate("checkout-failed"))
                .thenReturn(Optional.of(payment));

        Map<String, Object> stkCallback = new HashMap<>();
        stkCallback.put("CheckoutRequestID", "checkout-failed");
        stkCallback.put("ResultCode", 1);

        Map<String, Object> body = new HashMap<>();
        body.put("stkCallback", stkCallback);

        Map<String, Object> callback = new HashMap<>();
        callback.put("Body", body);

        paymentService.processMpesaCallback(callback);

        verify(paymentRepository)
                .findByCheckoutRequestIdForUpdate("checkout-failed");
        verify(paymentRepository, never()).save(any(Payment.class));
        verifyNoInteractions(productRepository);
    }

    @Test
    void successfulCallbackDeductsStockAndConfirmsOrder() {
        Product product = new Product();
        product.setId(1L);
        product.setName("Test Phone");
        product.setStockQuantity(10);

        Order order = new Order();
        order.setStatus(OrderStatus.PENDING);
        order.setTotalAmount(new BigDecimal("2000.00"));

        OrderItem orderItem = new OrderItem();
        orderItem.setOrder(order);
        orderItem.setProduct(product);
        orderItem.setQuantity(2);

        order.setItems(List.of(orderItem));

        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setAmount(new BigDecimal("2000.00"));
        payment.setStatus(PaymentStatus.PENDING);
        payment.setCheckoutRequestId("checkout-success");

        when(paymentRepository.findByCheckoutRequestIdForUpdate("checkout-success"))
                .thenReturn(Optional.of(payment));

        when(productRepository.findByIdForUpdate(1L))
                .thenReturn(Optional.of(product));

        Map<String, Object> amount = Map.of(
                "Name", "Amount",
                "Value", 2000
        );
        Map<String, Object> receipt = Map.of(
                "Name", "MpesaReceiptNumber",
                "Value", "QAB123XYZ"
        );
        Map<String, Object> transactionDate = Map.of(
                "Name", "TransactionDate",
                "Value", "20261025120000"
        );

        Map<String, Object> stkCallback = new HashMap<>();
        stkCallback.put("CheckoutRequestID", "checkout-success");
        stkCallback.put("ResultCode", 0);
        stkCallback.put("ResultDesc", "The service request is processed successfully");
        stkCallback.put(
                "CallbackMetadata",
                Map.of("Item", List.of(amount, receipt, transactionDate))
        );

        Map<String, Object> body = Map.of("stkCallback", stkCallback);
        Map<String, Object> callback = Map.of("Body", body);

        paymentService.processMpesaCallback(callback);

        assertEquals(8, product.getStockQuantity());
        assertEquals(PaymentStatus.SUCCESS, payment.getStatus());
        assertEquals("QAB123XYZ", payment.getMpesaReceiptNumber());
        assertEquals(OrderStatus.CONFIRMED, order.getStatus());

        verify(productRepository).save(product);
        verify(orderRepository).save(order);
        verify(paymentRepository).save(payment);
    }
@Test
void pendingPaymentPreventsDuplicateStkPush() {
    Order order = new Order();
    order.setStatus(OrderStatus.PENDING);
    order.setTotalAmount(new BigDecimal("2000.00"));

    Payment existingPayment = new Payment();
    existingPayment.setStatus(PaymentStatus.PENDING);

    when(orderRepository.findByIdForUpdate(1L))
            .thenReturn(Optional.of(order));

    when(paymentRepository.findByOrderIdAndStatus(
            1L, PaymentStatus.PENDING))
            .thenReturn(Optional.of(existingPayment));

    PaymentRequest request = new PaymentRequest();
    request.setOrderId(1L);
    request.setPhoneNumber("0712345678");

    Assertions.assertThrows(
            IllegalStateException.class,
            () -> paymentService.initiatePayment(request)
    );

    verify(mpesaService, never())
            .initiateStkPush(anyInt(), anyString(), anyString());

    verify(paymentRepository, never()).saveAndFlush(any(Payment.class));
}
}
