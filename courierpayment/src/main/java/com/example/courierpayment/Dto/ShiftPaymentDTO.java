package com.example.courierpayment.Dto;

import java.math.BigDecimal;

public class ShiftPaymentDTO {
    private Long shiftId;
    private String date;
    private BigDecimal paymentAmount;

    public ShiftPaymentDTO(Long shiftId, String date, BigDecimal paymentAmount) {
        this.shiftId = shiftId;
        this.date = date;
        this.paymentAmount = paymentAmount;
    }

    public Long getShiftId() {
        return shiftId;
    }

    public void setShiftId(Long shiftId) {
        this.shiftId = shiftId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public BigDecimal getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(BigDecimal paymentAmount) {
        this.paymentAmount = paymentAmount;
    }
}
