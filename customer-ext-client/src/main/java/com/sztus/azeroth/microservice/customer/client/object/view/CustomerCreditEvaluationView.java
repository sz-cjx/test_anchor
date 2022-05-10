package com.sztus.azeroth.microservice.customer.client.object.view;

import java.math.BigDecimal;

public class CustomerCreditEvaluationView {

    private Long customerId;

    private Integer creditPoint;

    private BigDecimal maximumCreditAmount;

    private Long updatedAt;

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Integer getCreditPoint() {
        return creditPoint;
    }

    public void setCreditPoint(Integer creditPoint) {
        this.creditPoint = creditPoint;
    }

    public BigDecimal getMaximumCreditAmount() {
        return maximumCreditAmount;
    }

    public void setMaximumCreditAmount(BigDecimal maximumCreditAmount) {
        this.maximumCreditAmount = maximumCreditAmount;
    }

    public Long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Long updatedAt) {
        this.updatedAt = updatedAt;
    }
}
