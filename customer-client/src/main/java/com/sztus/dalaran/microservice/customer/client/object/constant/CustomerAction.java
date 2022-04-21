package com.sztus.dalaran.microservice.customer.client.object.constant;

public interface CustomerAction {

    String GET_CUSTOMER = "/general/customer/get";
    String SAVE_CUSTOMER = "/general/customer/save";


    String SAVE_PERSONAL = "/general/personal/save";
    String GET_PERSONAL = "/general/personal/get";

    String GET_EMPLOYMENT = "/general/employment/get";
    String SAVE_EMPLOYMENT = "/general/employment/save";

    String GET_PAYROLL = "/general/payroll/get";
    String SAVE_PAYROLL = "/general/payroll/save";

}
