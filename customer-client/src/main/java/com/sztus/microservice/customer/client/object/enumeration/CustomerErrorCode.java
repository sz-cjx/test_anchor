package com.sztus.microservice.customer.client.object.enumeration;

import com.sztus.framework.component.core.base.BaseError;

public enum CustomerErrorCode implements BaseError {

    CUSTOMER_IS_NOT_EXISTED(95091001, "Expected customer is not existed"),
    FAILURE_PARAMETER_IS_INCOMPLETE(95091002, "Parameter(s) is(are) incomplete."),;

    private Integer code;
    private String message;

    CustomerErrorCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    };

    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
