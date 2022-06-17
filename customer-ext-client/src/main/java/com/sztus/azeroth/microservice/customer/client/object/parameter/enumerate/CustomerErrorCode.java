package com.sztus.azeroth.microservice.customer.client.object.parameter.enumerate;

import com.sztus.framework.component.core.base.BaseError;

public enum CustomerErrorCode implements BaseError {

    PARAMETER_IS_INCOMPLETE(95280001, "Missing Required Information"),
    FAILURE_TO_SAVE_DATA(95280002, "failed to save data"),
    FAILURE_ADD_CUSTOMER_USERNAME_HAS_EXISTS(95280003, "The account already exists."),
    SSN_ALREADY_EXISTS(95280004,"ssn already exists"),
    CUSTOMER_IS_NOT_EXISTED(95280005,"customer is not existed"),
    UNKNOWN_CONTACT_TYPE (95280006,"Incorrect Contact Info"),
    CUSTOMER_IS_EXISTED(95280007,"customer is existed"),
    CONTACT_INFORMATION_USED(95280008,"contact information has already been used")
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
