package com.sztus.dalaran.microservice.customer.client.object.parameter.request;

public class GetCustomerOptInDataRequest {

    private Long customerId;

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }
}
