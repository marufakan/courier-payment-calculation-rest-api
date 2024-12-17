package com.example.courierpayment.repository;

import com.example.courierpayment.entity.Shift;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ShiftRepository extends JpaRepository<Shift,Long> {
    List<Shift> findAllByCourierId(Long courierId, Pageable pageable);
}
