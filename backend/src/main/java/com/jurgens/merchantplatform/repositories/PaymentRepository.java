package com.jurgens.merchantplatform.repositories;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.jurgens.merchantplatform.entities.Payment;
import com.jurgens.merchantplatform.entities.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

@Lock(LockModeType.PESSIMISTIC_WRITE)
@Query("SELECT p FROM Payment p WHERE p.checkoutRequestId = :checkoutRequestId")
Optional<Payment> findByCheckoutRequestIdForUpdate(
        @Param("checkoutRequestId") String checkoutRequestId
);
    Optional<Payment> findByMpesaReceiptNumber(String mpesaReceiptNumber);

    List<Payment> findByOrderId(Long orderId);

    Optional<Payment> findByOrderIdAndStatus(
            Long orderId,
            PaymentStatus status
    );
}
