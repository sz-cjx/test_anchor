package com.arbfintech.microservice.customer.object.dto;

import com.alibaba.fastjson.JSONObject;

import java.math.BigDecimal;
import java.util.List;

public class PaymentScheduleDTO {

    private Long customerId;

    private String effectiveDate;

    private BigDecimal loanAmount;

    private String firstDebitDate;

    private String lastDebitDate;

    private BigDecimal regularAmount;

    private BigDecimal totalAmount;

    private BigDecimal totalInterest;

    private BigDecimal totalPrincipal;

    private BigDecimal totalUnpaidFee;

    private List<Object> installmentList;

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(String effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public BigDecimal getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(BigDecimal loanAmount) {
        this.loanAmount = loanAmount;
    }

    public String getFirstDebitDate() {
        return firstDebitDate;
    }

    public void setFirstDebitDate(String firstDebitDate) {
        this.firstDebitDate = firstDebitDate;
    }

    public String getLastDebitDate() {
        return lastDebitDate;
    }

    public void setLastDebitDate(String lastDebitDate) {
        this.lastDebitDate = lastDebitDate;
    }

    public BigDecimal getRegularAmount() {
        return regularAmount;
    }

    public void setRegularAmount(BigDecimal regularAmount) {
        this.regularAmount = regularAmount;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getTotalInterest() {
        return totalInterest;
    }

    public void setTotalInterest(BigDecimal totalInterest) {
        this.totalInterest = totalInterest;
    }

    public BigDecimal getTotalPrincipal() {
        return totalPrincipal;
    }

    public void setTotalPrincipal(BigDecimal totalPrincipal) {
        this.totalPrincipal = totalPrincipal;
    }

    public BigDecimal getTotalUnpaidFee() {
        return totalUnpaidFee;
    }

    public void setTotalUnpaidFee(BigDecimal totalUnpaidFee) {
        this.totalUnpaidFee = totalUnpaidFee;
    }

    public List<Object> getInstallmentList() {
        return installmentList;
    }

    public void setInstallmentList(List<Object> installmentList) {
        this.installmentList = installmentList;
    }
}
