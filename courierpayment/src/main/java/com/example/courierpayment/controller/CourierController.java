package com.example.courierpayment.controller;

import com.example.courierpayment.Dto.PaymentSummaryDTO;
import com.example.courierpayment.Dto.ShiftRequestDTO;
import com.example.courierpayment.Dto.ShiftResponseDTO;
import com.example.courierpayment.entity.Shift;
import com.example.courierpayment.service.CourierService;
import com.example.courierpayment.service.PaymentService;
import com.example.courierpayment.service.ShiftService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/couriers")
public class CourierController {

    private final CourierService courierService;
    private final ShiftService shiftService;
    private final PaymentService paymentService;
    public CourierController(CourierService courierService, ShiftService shiftService, PaymentService paymentService) {
        this.courierService = courierService;
        this.shiftService = shiftService;
        this.paymentService = paymentService;
    }

    @PostMapping("/{courierId}/shifts")
    public ResponseEntity<ShiftResponseDTO> createShift(@PathVariable Long courierId,
                                                        @Valid @RequestBody ShiftRequestDTO shiftRequestDTO) {
        ShiftResponseDTO shiftResponseDTO = shiftService.createShift(courierId, shiftRequestDTO);
        return new ResponseEntity<>(shiftResponseDTO, HttpStatus.CREATED);
    }

    @GetMapping("/{courierId}/shifts")
    public List<Shift> getShiftsByCourierId(
            @PathVariable Long courierId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "4") int size) {
        return shiftService.getAllShiftWithPagination(courierId,page,size);
    }

    @PutMapping("/{courierId}/shifts/{shiftId}")
    public ResponseEntity<ShiftResponseDTO> updateShift(
            @PathVariable Long courierId,
            @PathVariable Long shiftId,
            @Valid @RequestBody ShiftRequestDTO shiftRequestDTO) {

        ShiftResponseDTO  shiftResponseDTO = shiftService.updateShiftByCourierId(courierId, shiftId, shiftRequestDTO);
        return ResponseEntity.accepted().body(shiftResponseDTO);
    }

    @GetMapping("/{courierId}/payments")
    public ResponseEntity<PaymentSummaryDTO> getPaymentsByDate(
            @PathVariable Long courierId,
            @RequestParam LocalDate date) {

        PaymentSummaryDTO paymentSummary = paymentService.getPaymentsByCourierAndDate(courierId, date);
        return ResponseEntity.ok(paymentSummary);
    }
}
