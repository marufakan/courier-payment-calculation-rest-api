package com.example.courierpayment.Dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDate;

public class ShiftRequestDTO {

    @NotNull(message = "Operation ID cannot be null")
    @Min(value = 1, message = "Operation Id count must be at least 1")
    private Long operationId;

    @NotNull(message = "Date cannot be null")
    @PastOrPresent(message = "Date must be in the past or present")
    private java.time.LocalDate date;

    @Min(value = 1, message = "Package count must be at least 1")
    private int packageCount;

    @Min(value = 1, message = "Hours worked must be at least 1")
    @Max(value = 24, message = "Hours worked cannot exceed 24")
    private int hoursWorked;

    public Long getOperationId() {
        return operationId;
    }

    public void setOperationId(Long operationId) {
        this.operationId = operationId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getPackageCount() {
        return packageCount;
    }

    public void setPackageCount(int packageCount) {
        this.packageCount = packageCount;
    }

    public int getHoursWorked() {
        return hoursWorked;
    }

    public void setHoursWorked(int hoursWorked) {
        this.hoursWorked = hoursWorked;
    }
}

