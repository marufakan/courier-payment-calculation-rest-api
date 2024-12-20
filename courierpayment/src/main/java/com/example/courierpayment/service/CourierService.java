package com.example.courierpayment.service;

import com.example.courierpayment.entity.Courier;
import com.example.courierpayment.repository.CourierRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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

    public void saveCourier(Courier courier){
        if (courier == null) {
            throw new IllegalArgumentException("Courier cannot be null");
        }
        if (!StringUtils.hasText(courier.getName())) {
            throw new IllegalArgumentException("Courier name cannot be empty");
        }

        if (courier.getCourierId() != null) {
            courierRepository.findById(courier.getCourierId()).ifPresent(existingCourier -> {
                throw new IllegalStateException(
                        "Courier with ID " + existingCourier.getCourierId() + " already exists");
            });
        }
        courierRepository.save(courier);
    }
}
