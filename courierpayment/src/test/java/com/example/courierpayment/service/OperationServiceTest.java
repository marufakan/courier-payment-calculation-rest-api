package com.example.courierpayment.service;

import com.example.courierpayment.entity.Operation;
import com.example.courierpayment.repository.OperationRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class OperationServiceTest {
    private final OperationRepository operationRepository= Mockito.mock(OperationRepository.class);
    private final OperationService operationService = new OperationService(operationRepository);

    @Test
    void testFindOperationByOperationId_OperationExists() {
        Long operationId = 1L;
        Operation operation = new Operation();
        operation.setOperationId(operationId);
        operation.setHourlyRate(new BigDecimal(10));
        operation.setPackageRate(new BigDecimal(20));

        when(operationRepository.findById(operationId)).thenReturn(Optional.of(operation));
        Optional<Operation> result = operationService.findOperationByOperationId(operationId);
        assertThat(result).isPresent();
        assertThat(result.get().getHourlyRate().compareTo(new BigDecimal("10"))).isZero();
        assertThat(result.get().getPackageRate().compareTo(new BigDecimal("20"))).isZero();
    }

    @Test
    void testFindOperationByOperationId_OperationDoesNotExists() {
        Long operationId = 500L;
        when(operationRepository.findById(operationId)).thenReturn(Optional.empty());
        Optional<Operation> result = operationService.findOperationByOperationId(operationId);
        assertThat(result).isEmpty();
    }
}