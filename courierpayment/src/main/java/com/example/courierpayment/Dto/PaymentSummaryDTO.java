package com.example.courierpayment.Dto;

import java.math.BigDecimal;
import java.util.List;

public class PaymentSummaryDTO {
    private BigDecimal totalPayment;
    private List<ShiftPaymentDTO> shiftPayments;

    public BigDecimal getTotalPayment() {
        return totalPayment;
    }

    public void setTotalPayment(BigDecimal totalPayment) {
        this.totalPayment = totalPayment;
    }

    public List<ShiftPaymentDTO> getShiftPayments() {
        return shiftPayments;
    }

    public void setShiftPayments(List<ShiftPaymentDTO> shiftPayments) {
        this.shiftPayments = shiftPayments;
    }
}
