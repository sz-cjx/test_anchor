package com.arbfintech.microservice.customer.object.dto;

import java.math.BigDecimal;

public class CalculationResultDTO {

    private Boolean personalResult;

    private Boolean contactResult;

    private Boolean employmentResult;

    private Boolean bankResult;

    private Boolean ibvResult;

    private BigDecimal loanAmount;

    public Boolean getPersonalResult() {
        return personalResult;
    }

    public void setPersonalResult(Boolean personalResult) {
        this.personalResult = personalResult;
    }

    public Boolean getContactResult() {
        return contactResult;
    }

    public void setContactResult(Boolean contactResult) {
        this.contactResult = contactResult;
    }

    public Boolean getEmploymentResult() {
        return employmentResult;
    }

    public void setEmploymentResult(Boolean employmentResult) {
        this.employmentResult = employmentResult;
    }

    public Boolean getBankResult() {
        return bankResult;
    }

    public void setBankResult(Boolean bankResult) {
        this.bankResult = bankResult;
    }

    public Boolean getIbvResult() {
        return ibvResult;
    }

    public void setIbvResult(Boolean ibvResult) {
        this.ibvResult = ibvResult;
    }

    public BigDecimal getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(BigDecimal loanAmount) {
        this.loanAmount = loanAmount;
    }
}
