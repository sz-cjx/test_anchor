package com.arbfintech.microservice.customer.object.constant;

public interface ClientApiUrl {
    String API_CUSTOMER = "/api/customer";
    String REPLACE_CUSTOMER = "/replace-customer";
    String GET_CUSTOMER_BY_ID = "/get-customer/{id}";
    String LIST_CUSTOMER_BY_SSN_OR_EMAIL_OR_NUM = "/list-customer-ssn-email-num";
    String GET_CUSTOMER_BY_CONDITIONS = "/get-customer";

}
