package com.sztus.azeroth.microservice.customer.client.object.parameter.request;

import com.sztus.azeroth.microservice.customer.client.object.view.CustomerContactInfoView;

public class SaveCustomerContactInfoRequest extends CustomerContactInfoView {
    private Boolean isVerified;

    private Long portfolioId;

    public Long getPortfolioId() {
        return portfolioId;
    }

    public void setPortfolioId(Long portfolioId) {
        this.portfolioId = portfolioId;
    }

    public Boolean getIsVerified() {
        return isVerified;
    }

    public void setIsVerified(Boolean verified) {
        isVerified = verified;
    }
}
