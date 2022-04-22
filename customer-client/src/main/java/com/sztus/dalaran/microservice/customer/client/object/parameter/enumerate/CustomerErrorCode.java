package com.sztus.dalaran.microservice.customer.client.object.parameter.enumerate;

import com.sztus.framework.component.core.base.BaseError;

public enum CustomerErrorCode implements BaseError {

    PARAMETER_IS_INCOMPLETE(95090001, "parameter is incomplete"),
    FAILURE_TO_SAVE_DATA(95090002, "failed to save data"),
    FAILURE_ADD_CUSTOMER_USERNAME_HAS_EXISTS(95090003, "username already exists"),
    SSN_ALREADY_EXISTS(95090004,"ssn already exists")
    ;

    CustomerErrorCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    private final Integer code;
    private final String message;

    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
