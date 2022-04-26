package com.sztus.azeroth.microservice.customer.client.object.parameter.request;

public class GetCustomerPayrollRequest {

    Long customerId;

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }
}
