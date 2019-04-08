package com.arbfintech.microservice.loan.entity;

import java.math.BigDecimal;

public class Program {

    private Integer id;
    private Integer companyId;
    private String programName;
    private BigDecimal minimumLoanSize;
    private BigDecimal maximumLoanSize;
    private BigDecimal InterestRate;
    private Integer status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public String getProgramName() {
        return programName;
    }

    public void setProgramName(String programName) {
        this.programName = programName;
    }

    public BigDecimal getMinimumLoanSize() {
        return minimumLoanSize;
    }

    public void setMinimumLoanSize(BigDecimal minimumLoanSize) {
        this.minimumLoanSize = minimumLoanSize;
    }

    public BigDecimal getMaximumLoanSize() {
        return maximumLoanSize;
    }

    public void setMaximumLoanSize(BigDecimal maximumLoanSize) {
        this.maximumLoanSize = maximumLoanSize;
    }

    public BigDecimal getInterestRate() {
        return InterestRate;
    }

    public void setInterestRate(BigDecimal interestRate) {
        InterestRate = interestRate;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
