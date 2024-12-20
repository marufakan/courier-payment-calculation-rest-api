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

    public void saveOperation(Operation operation) {
        if (operation == null) {
            throw new IllegalArgumentException("Operation cannot be null and must be linked to a Courier");
        }

        if (operation.getOperationId() != null) {
            operationRepository.findById(operation.getOperationId()).ifPresent(existingOperation -> {
                throw new IllegalStateException(
                        "Payment with ID " + existingOperation.getOperationId() + " already exists");
            });
        }

        operationRepository.save(operation);
    }
}
