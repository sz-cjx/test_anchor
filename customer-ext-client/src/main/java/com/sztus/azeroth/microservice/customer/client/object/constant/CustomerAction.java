package com.sztus.azeroth.microservice.customer.client.object.constant;

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

    String GET_PAYROLL = "/general/payroll/get";
    String SAVE_PAYROLL = "/general/payroll/save";

    String GET_CUSTOMER_BY_CONDITION = "/general/customer/get/condition";

    /**
     * customer contact data
     */
    String GET_CUSTOMER_CONTACT = "/general/contact/get";
    String LIST_CUSTOMER_CONTACT = "/general/contact/list";
    String SAVE_CUSTOMER_CONTACT = "/general/contact/save";
    String BATCH_SAVE_CUSTOMER_CONTACT = "/general/contact/batch-save";

    /**
     * customer ibv
     */
    String SAVE_IBV_AUTHORIZATION = "/ibv-authorization/save";
    String LIST_IBV_AUTHORIZATION = "/ibv-authorization/list";


}
