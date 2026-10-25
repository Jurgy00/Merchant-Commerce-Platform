package com.jurgens.merchantplatform.repositories;

import com.jurgens.merchantplatform.entities.Payment;
import com.jurgens.merchantplatform.entities.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> findByCheckoutRequestId(String checkoutRequestId);

    Optional<Payment> findByMpesaReceiptNumber(String mpesaReceiptNumber);

    List<Payment> findByOrderId(Long orderId);

    Optional<Payment> findByOrderIdAndStatus(
            Long orderId,
            PaymentStatus status
    );
}
