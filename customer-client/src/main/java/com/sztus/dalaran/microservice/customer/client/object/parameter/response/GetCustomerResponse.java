package com.sztus.dalaran.microservice.customer.client.object.parameter.response;

import com.sztus.dalaran.microservice.customer.client.object.view.CustomerView;

public class GetCustomerResponse extends CustomerView {

    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
