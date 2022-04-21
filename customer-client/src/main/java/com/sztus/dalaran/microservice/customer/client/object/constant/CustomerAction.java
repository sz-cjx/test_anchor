package com.sztus.dalaran.microservice.customer.client.object.constant;

public interface CustomerAction {

    String GET_CUSTOMER = "/general/customer/get";
    String SAVE_CUSTOMER = "/general/customer/save";


    String SAVE_PERSONAL = "/general/personal/save";
    String GET_PERSONAL = "/general/personal/get";

    String LIST_BANK_ACOUNT = "/general/bank-acount/list";
    String SAVE_BANK_ACOUNT = "/general/bank-acount/save";
}
