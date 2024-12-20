package com.example.courierpayment.service;

import com.example.courierpayment.entity.Operation;
import com.example.courierpayment.entity.Payment;
import com.example.courierpayment.entity.Shift;
import com.example.courierpayment.repository.PaymentRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class PaymentServiceTest {
    private final PaymentRepository paymentRepository = Mockito.mock(PaymentRepository.class);
    private final PaymentService paymentService = new PaymentService(paymentRepository);

    @Test
    void testCheckAndUpdatePayment_Success() {
        Shift shift=new Shift();
        shift.setShiftId(1L);
        LocalDate specificDate = LocalDate.of(2024, 12, 20);
        shift.setDate(specificDate);
        shift.setHoursWorked(10);
        shift.setCourierId(1L);
        shift.setPackageCount(10);
        shift.setOperationId(1L);
        Operation operation =new Operation();
        operation.setOperationId(1L);
        operation.setHourlyRate(new BigDecimal(10));
        operation.setPackageRate(new BigDecimal(10));

        Payment existingPayment = new Payment();
        existingPayment.setPaymentId(1L);
        existingPayment.setPaymentAmount(new BigDecimal(200));
        existingPayment.setDate(specificDate);
        existingPayment.setCourierId(1L);

        Optional<Payment> result = paymentService.checkAndUpdatePayment(shift, operation);

        assertThat(result).isPresent();
        Payment updatedPayment = result.get();
        assertThat(updatedPayment.getPaymentAmount()).isEqualTo(existingPayment.getPaymentAmount());
        assertThat(updatedPayment.getDate()).isEqualTo(existingPayment.getDate());
    }
}