package com.example.courierpayment.service;

import com.example.courierpayment.Dto.ShiftRequestDTO;
import com.example.courierpayment.Dto.ShiftResponseDTO;
import com.example.courierpayment.entity.Courier;
import com.example.courierpayment.entity.Operation;
import com.example.courierpayment.entity.Shift;
import com.example.courierpayment.repository.ShiftRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class ShiftServiceTest {

    private final CourierService courierService = Mockito.mock(CourierService.class);
    private final OperationService operationService = Mockito.mock(OperationService.class);
    private final PaymentService paymentService = Mockito.mock(PaymentService.class);
    private final ShiftRepository shiftRepository = Mockito.mock(ShiftRepository.class);

    private final ShiftService shiftService =new ShiftService(shiftRepository,courierService,operationService,paymentService);

    private Courier courier;
    private Operation operation;

    @BeforeEach
    void setUp() {
        courier = new Courier();
        courier.setCourierId(1L);
        courier.setName("John Doe");
        courierService.saveCourier(courier);

        operation=new Operation();
        operation.setOperationId(1L);
        operation.setHourlyRate(new BigDecimal(20));
        operation.setPackageRate(new BigDecimal(5));
        operationService.saveOperation(operation);
    }
    @Test
    void testCreateShift_Success() {
        when(courierService.findCourierByCourierId(courier.getCourierId())).thenReturn(Optional.of(courier));
        Optional<Courier> resultCourier = courierService.findCourierByCourierId(courier.getCourierId());

        when(operationService.findOperationByOperationId(operation.getOperationId())).thenReturn(Optional.of(operation));
        Optional<Operation> resultOperation = operationService.findOperationByOperationId(operation.getOperationId());

        ShiftRequestDTO shiftRequestDTO = new ShiftRequestDTO();
        shiftRequestDTO.setOperationId(resultOperation.get().getOperationId());
        LocalDate specificDate = LocalDate.of(2024, 12, 20);
        shiftRequestDTO.setDate(specificDate);
        shiftRequestDTO.setHoursWorked(10);
        shiftRequestDTO.setPackageCount(10);

        ShiftResponseDTO expectedShiftResponse = new ShiftResponseDTO();
        expectedShiftResponse.setId(1L);
        expectedShiftResponse.setDate(specificDate);
        expectedShiftResponse.setHoursWorked(10);
        expectedShiftResponse.setPackageCount(10);
        expectedShiftResponse.setCourierId(resultCourier.get().getCourierId());
        expectedShiftResponse.setOperationId(resultOperation.get().getOperationId());

        when(shiftService.createShift(resultCourier.get().getCourierId(), shiftRequestDTO)).thenReturn(expectedShiftResponse);
    }

    @Test
    void testGetAllShiftWithPagination() {
        Long courierId = 1L;
        int pageNo = 1;
        int pageSize = 5;

        Shift shift1 = new Shift();
        shift1.setShiftId(1L);
        shift1.setHoursWorked(8);

        Shift shift2 = new Shift();
        shift2.setShiftId(2L);
        shift2.setHoursWorked(10);

        List<Shift> shifts = List.of(shift1, shift2);
        Pageable pageable = PageRequest.of(pageNo-1, pageSize);

        when(shiftRepository.findAllByCourierId(courierId, pageable)).thenReturn(shifts);
        List<Shift> result = shiftService.getAllShiftWithPagination(courierId, pageNo, pageSize);
        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getShiftId()).isEqualTo(1L);
        assertThat(result.get(1).getShiftId()).isEqualTo(2L);
        verify(shiftRepository, times(1)).findAllByCourierId(courierId, pageable);
    }
}