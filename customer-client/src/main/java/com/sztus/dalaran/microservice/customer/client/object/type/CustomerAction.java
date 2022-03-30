package com.sztus.dalaran.microservice.customer.client.object.type;

public interface CustomerAction {
    String GET_CUSTOMER = "/customer/get";

    String SAVE_CUSTOMER = "/customer/save";

    String GET_CUSTOMER_BY_UNIQUE = "/customer/unique-get";

    String CREATE_CUSTOMER = "/customer/create";
}
