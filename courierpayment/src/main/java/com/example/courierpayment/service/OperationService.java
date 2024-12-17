package com.example.courierpayment.service;

import com.example.courierpayment.entity.Operation;
import com.example.courierpayment.repository.OperationRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OperationService {

    private final OperationRepository operationRepository;
    public OperationService(OperationRepository operationRepository) {
        this.operationRepository = operationRepository;
    }

    /**
     * Checks for the existence of a Operation record.
     * @param operationId
     * @return
     */
    public Optional<Operation> findOperationByOperationId(Long operationId) {
        return operationRepository.findById(operationId);
    }
}
