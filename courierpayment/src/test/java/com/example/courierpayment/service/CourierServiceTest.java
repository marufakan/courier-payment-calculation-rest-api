package com.example.courierpayment.service;

import com.example.courierpayment.entity.Courier;
import com.example.courierpayment.repository.CourierRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class CourierServiceTest {
    private final CourierRepository courierRepository = Mockito.mock(CourierRepository.class);
    private final CourierService courierService = new CourierService(courierRepository);


    @Test
    void testFindCourierByCourierId_CourierExists() {
        Long courierId = 1L;
        Courier courier = new Courier();
        courier.setCourierId(courierId);
        courier.setName("John Doe");

        when(courierRepository.findById(courierId)).thenReturn(Optional.of(courier));
        Optional<Courier> result = courierService.findCourierByCourierId(courierId);
        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("John Doe");
    }

    @Test
    void testFindCourierByCourierId_CourierDoesNotExist() {
        Long courierId = 500L;
        when(courierRepository.findById(courierId)).thenReturn(Optional.empty());
        Optional<Courier> result = courierService.findCourierByCourierId(courierId);
        assertThat(result).isEmpty();
    }
}
