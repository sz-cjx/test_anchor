package com.sztus.dalaran.microservice.customer.client.object.parameter.enumerate;

import com.sztus.framework.component.core.base.BaseError;

public enum CustomerErrorCode implements BaseError {

    PARAMETER_IS_INCOMPLETE(95090001, "parameter is incomplete"),
    FAILURE_TO_SAVE_DATA(95090002, "failed to save data"),
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
