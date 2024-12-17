package com.example.courierpayment.service;

import com.example.courierpayment.entity.Courier;
import com.example.courierpayment.repository.CourierRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CourierService {

    private final CourierRepository courierRepository;
    public CourierService(CourierRepository courierRepository) {
        this.courierRepository = courierRepository;
    }

    /**
     * Checks for the existence of a courier record.
     * @param courierId
     * @return
     */
    public Optional<Courier> findCourierByCourierId(Long courierId) {
        return courierRepository.findById(courierId);
    }
}
