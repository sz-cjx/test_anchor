package com.sztus.dalaran.microservice.customer.client.object.parameter.request;

public class GetCustomerRequest {

    private Long customerId;

    private String contactInformation;

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getContactInformation() {
        return contactInformation;
    }

    public void setContactInformation(String contactInformation) {
        this.contactInformation = contactInformation;
    }
}
