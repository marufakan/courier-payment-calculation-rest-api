package com.example.courierpayment.repository;

import com.example.courierpayment.entity.Courier;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourierRepository extends JpaRepository<Courier,Long> {
}
