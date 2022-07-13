package com.sztus.azeroth.microservice.customer.client.object.parameter.request;

import java.math.BigDecimal;

public class UpdateCreditAmountRequest {

    private Long portfolioId;

    private BigDecimal creditAmount;

    private Long customerId;

    public Long getPortfolioId() {
        return portfolioId;
    }

    public void setPortfolioId(Long portfolioId) {
        this.portfolioId = portfolioId;
    }

    public BigDecimal getCreditAmount() {
        return creditAmount;
    }

    public void setCreditAmount(BigDecimal creditAmount) {
        this.creditAmount = creditAmount;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }
}
