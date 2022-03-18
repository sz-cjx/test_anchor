package com.sztus.dalaran.microservice.customer.client.object.parameter.request;

public class GetCustomerRequest {

    private String email;

    private String openId;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }
}
