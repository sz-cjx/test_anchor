package com.sztus.azeroth.microservice.customer.client.object.parameter.request;

import com.sztus.azeroth.microservice.customer.client.object.view.CustomerIdentityView;

public class SaveCustomerIdentityRequest extends CustomerIdentityView {
    private Long portfolioId;

    public Long getPortfolioId() {
        return portfolioId;
    }

    public void setPortfolioId(Long portfolioId) {
        this.portfolioId = portfolioId;
    }
}
