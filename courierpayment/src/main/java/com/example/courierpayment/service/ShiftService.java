package com.example.courierpayment.service;

import com.example.courierpayment.Dto.ShiftRequestDTO;
import com.example.courierpayment.Dto.ShiftResponseDTO;
import com.example.courierpayment.entity.Operation;
import com.example.courierpayment.entity.Shift;
import com.example.courierpayment.entity.Payment;
import com.example.courierpayment.repository.ShiftRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Service
public class ShiftService {

    private final ShiftRepository shiftRepository;
    private final CourierService courierService;
    private final OperationService operationService;
    private final PaymentService paymentService;

    public ShiftService(ShiftRepository shiftRepository, CourierService courierService, OperationService operationService, PaymentService paymentService) {
        this.shiftRepository = shiftRepository;
        this.courierService = courierService;
        this.operationService = operationService;
        this.paymentService = paymentService;
    }

    public ShiftResponseDTO createShift(Long courierId, ShiftRequestDTO shiftRequestDTO) {
        Shift shift = new Shift();
        shift.setCourierId(courierId);
        shift.setOperationId(shiftRequestDTO.getOperationId());
        shift.setPackageCount(shiftRequestDTO.getPackageCount());
        shift.setHoursWorked(shiftRequestDTO.getHoursWorked());
        shift.setDate(shiftRequestDTO.getDate());

        return getShiftResponseDTO(shift);
    }

    public List<Shift> getAllShiftWithPagination(Long courierId, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo-1, pageSize);
        return shiftRepository.findAllByCourierId(courierId, pageable);
    }

    public ShiftResponseDTO updateShiftByCourierId(Long courierId,Long shiftId,ShiftRequestDTO shiftRequestDTO) {
        courierService.findCourierByCourierId(courierId)
                .orElseThrow(() -> new RuntimeException("Courier not found"));

        Shift shift = shiftRepository.findById(shiftId)
                .orElseThrow(() -> new RuntimeException("Shift not found"));

        if(!shift.getCourierId().equals(courierId)) {
            throw new RuntimeException("Courier does not match with the shift");
        }

        shift.setOperationId(shiftRequestDTO.getOperationId());
        shift.setDate(shiftRequestDTO.getDate());
        shift.setPackageCount(shiftRequestDTO.getPackageCount());
        shift.setHoursWorked(shiftRequestDTO.getHoursWorked());

        return getShiftResponseDTO(shift);
    }

    private ShiftResponseDTO getShiftResponseDTO(Shift shift) {
        Shift updatedShift = shiftRepository.save(shift);
        
        Operation operation = operationService.findOperationByOperationId(updatedShift.getOperationId())
                .orElseThrow(() -> new RuntimeException("Operation with ID " + updatedShift.getOperationId() + " not found"));

        Payment payment = paymentService.checkAndUpdatePayment(updatedShift, operation)
                .orElseThrow(() -> new RuntimeException("Payment could not be created or updated!"));;

        ShiftResponseDTO responseDTO = new ShiftResponseDTO();
        responseDTO.setId(updatedShift.getShiftId());
        responseDTO.setCourierId(updatedShift.getCourierId());
        responseDTO.setOperationId(updatedShift.getOperationId());
        responseDTO.setPackageCount(updatedShift.getPackageCount());
        responseDTO.setHoursWorked(updatedShift.getHoursWorked());
        responseDTO.setDate(updatedShift.getDate());

        return responseDTO;
    }

}
