package com.sztus.azeroth.microservice.customer.client.object.parameter.request;

public class GetBankAccountRequest {
    private Long id;

    private Long customerId;

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
