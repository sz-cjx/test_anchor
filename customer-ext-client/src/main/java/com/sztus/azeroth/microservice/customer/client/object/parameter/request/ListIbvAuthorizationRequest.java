package com.sztus.azeroth.microservice.customer.client.object.parameter.request;

public class ListIbvAuthorizationRequest {
    private Long customerId;

    private Long portfolioId;

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Long getPortfolioId() {
        return portfolioId;
    }

    public void setPortfolioId(Long portfolioId) {
        this.portfolioId = portfolioId;
    }
}
