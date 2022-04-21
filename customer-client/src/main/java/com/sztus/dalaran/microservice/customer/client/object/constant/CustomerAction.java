package com.sztus.dalaran.microservice.customer.client.object.constant;

public interface CustomerAction {

    String GET_CUSTOMER = "/general/customer/get";
    String SAVE_CUSTOMER = "/general/customer/save";


    String SAVE_PERSONAL = "/general/personal/save";
    String GET_PERSONAL = "/general/personal/get";

    String GET_BANK_ACCOUNT = "/general/bank-account/get";
    String LIST_BANK_ACCOUNT = "/general/bank-account/list";
    String SAVE_BANK_ACCOUNT = "/general/bank-account/save";

    String GET_EMPLOYMENT = "/general/employment/get";
    String SAVE_EMPLOYMENT = "/general/employment/save";

}
