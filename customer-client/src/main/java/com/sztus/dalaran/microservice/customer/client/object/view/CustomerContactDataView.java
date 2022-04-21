package com.sztus.dalaran.microservice.customer.client.object.view;


public class CustomerContactDataView {

    private Long customerId;
    private Integer contactType;
    private String contactInformation;
    private Integer verifiedStatus;
    private Long verifiedAt;
    private Boolean marketingToggle;
    private Boolean operationToggle;
    private Long createdAt;
    private Long updatedAt;

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Integer getContactType() {
        return contactType;
    }

    public void setContactType(Integer contactType) {
        this.contactType = contactType;
    }

    public String getContactInformation() {
        return contactInformation;
    }

    public void setContactInformation(String contactInformation) {
        this.contactInformation = contactInformation;
    }

    public Integer getVerifiedStatus() {
        return verifiedStatus;
    }

    public void setVerifiedStatus(Integer verifiedStatus) {
        this.verifiedStatus = verifiedStatus;
    }

    public Long getVerifiedAt() {
        return verifiedAt;
    }

    public void setVerifiedAt(Long verifiedAt) {
        this.verifiedAt = verifiedAt;
    }

    public Boolean getMarketingToggle() {
        return marketingToggle;
    }

    public void setMarketingToggle(Boolean marketingToggle) {
        this.marketingToggle = marketingToggle;
    }

    public Boolean getOperationToggle() {
        return operationToggle;
    }

    public void setOperationToggle(Boolean operationToggle) {
        this.operationToggle = operationToggle;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    public Long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Long updatedAt) {
        this.updatedAt = updatedAt;
    }
}
