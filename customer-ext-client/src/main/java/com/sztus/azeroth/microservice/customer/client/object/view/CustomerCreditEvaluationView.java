package com.sztus.azeroth.microservice.customer.client.object.view;

import java.math.BigDecimal;

public class CustomerCreditEvaluationView {

    private Long customerId;

    private Long portfolioId;

    private Integer creditPoint;

    private BigDecimal creditAmount;

    private Long updatedAt;

    private Long createdAt;

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

    public BigDecimal getCreditAmount() {
        return creditAmount;
    }

    public void setCreditAmount(BigDecimal creditAmount) {
        this.creditAmount = creditAmount;
    }

    public Long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Long updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Long getPortfolioId() {
        return portfolioId;
    }

    public void setPortfolioId(Long portfolioId) {
        this.portfolioId = portfolioId;
    }
}
