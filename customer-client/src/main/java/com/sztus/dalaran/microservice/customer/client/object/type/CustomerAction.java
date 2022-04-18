package com.sztus.dalaran.microservice.customer.client.object.type;

public interface CustomerAction {
    String GET_CUSTOMER = "/customer/get";

    String SAVE_CUSTOMER = "/customer/save";

    String GET_CUSTOMER_BY_UNIQUE = "/customer/unique-get";

    String CREATE_CUSTOMER = "/customer/create";

    /**
     * opt-in
     */
    String GET_OPT_IN_AS_LIST = "/opt-in/list";
    String SAVE_OPT_IN = "/opt-in/save";
}
