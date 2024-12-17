package com.example.courierpayment.service;

import com.example.courierpayment.Dto.PaymentSummaryDTO;
import com.example.courierpayment.Dto.ShiftPaymentDTO;
import com.example.courierpayment.entity.Operation;
import com.example.courierpayment.entity.Payment;
import com.example.courierpayment.entity.Shift;
import com.example.courierpayment.repository.PaymentRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.time.LocalDate;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public PaymentSummaryDTO getPaymentsByCourierAndDate(Long courierId, LocalDate date) {
        List<Payment> payments = paymentRepository.findAllByCourierIdAndDate(courierId, date);

        BigDecimal totalPayment = payments.stream()
                .map(Payment::getPaymentAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        List<ShiftPaymentDTO> shiftPayments = payments.stream()
                .map(payment -> new ShiftPaymentDTO(
                        payment.getShiftId(),
                        payment.getDate().toString(),
                        payment.getPaymentAmount()
                ))
                .collect(Collectors.toList());

        PaymentSummaryDTO paymentSummary = new PaymentSummaryDTO();
        paymentSummary.setTotalPayment(totalPayment);
        paymentSummary.setShiftPayments(shiftPayments);

        return paymentSummary;
    }

    /**
     * new payment record or updating payment record
     * @param updatedShift
     * @param operation
     * @return
     */
    public Optional<Payment> checkAndUpdatePayment(Shift updatedShift, Operation operation) {
        BigDecimal packageCountBD = BigDecimal.valueOf(updatedShift.getPackageCount());
        BigDecimal hoursWorkedBD = BigDecimal.valueOf(updatedShift.getHoursWorked());
        BigDecimal calculatedPaymentAmount = packageCountBD.multiply(operation.getPackageRate())
                .add(hoursWorkedBD.multiply(operation.getHourlyRate()));
        try {
            Optional<Payment> existingPaymentOpt = paymentRepository.findByShiftIdAndCourierId(
                    updatedShift.getShiftId(), updatedShift.getCourierId());

            Payment payment;
            if (existingPaymentOpt.isPresent()) {
                payment = existingPaymentOpt.get();
                if (payment.getPaymentAmount().compareTo(calculatedPaymentAmount) != 0) {
                    payment.setPaymentAmount(calculatedPaymentAmount);
                    payment.setDate(updatedShift.getDate());
                    paymentRepository.save(payment);
                }
            } else {
                payment = new Payment();
                payment.setShiftId(updatedShift.getShiftId());
                payment.setCourierId(updatedShift.getCourierId());
                payment.setPaymentAmount(calculatedPaymentAmount);
                payment.setDate(updatedShift.getDate());
                paymentRepository.save(payment);
            }

            return Optional.of(payment);
        } catch (Exception e) {
            return Optional.empty();
        }

    }

}
