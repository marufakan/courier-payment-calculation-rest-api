package com.example.courierpayment.repository;

import com.example.courierpayment.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment,Long> {
    List<Payment> findAllByCourierIdAndDate(Long courierId, LocalDate date);

    Optional<Payment> findByShiftIdAndCourierId(Long shiftId, Long courierId);
}
