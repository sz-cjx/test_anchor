package com.sztus.azeroth.microservice.customer.client.object.view;

public class IbvAuthorizationView {

    private Long id;
    private Long customerId;
    private Long ibvProviderId;
    private Long portfolioId;
    private String requestData;
    private String requestCode;
    private Long authorizedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Long getIbvProviderId() {
        return ibvProviderId;
    }

    public void setIbvProviderId(Long ibvProviderId) {
        this.ibvProviderId = ibvProviderId;
    }

    public Long getPortfolioId() {
        return portfolioId;
    }

    public void setPortfolioId(Long portfolioId) {
        this.portfolioId = portfolioId;
    }

    public String getRequestData() {
        return requestData;
    }

    public void setRequestData(String requestData) {
        this.requestData = requestData;
    }

    public String getRequestCode() {
        return requestCode;
    }

    public void setRequestCode(String requestCode) {
        this.requestCode = requestCode;
    }

    public Long getAuthorizedAt() {
        return authorizedAt;
    }

    public void setAuthorizedAt(Long authorizedAt) {
        this.authorizedAt = authorizedAt;
    }
}
