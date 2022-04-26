package com.sztus.azeroth.microservice.customer.client.object.parameter.request;

public class GetCustomerContactDataRequest {

    private Long customerId;
    private Integer type;

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
