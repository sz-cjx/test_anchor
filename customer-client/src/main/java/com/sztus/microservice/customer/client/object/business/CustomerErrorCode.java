package com.sztus.microservice.customer.client.object.business;

import com.sztus.framework.component.core.base.BaseError;

public enum CustomerErrorCode implements BaseError {

    CUSTOMER_IS_NOT_EXISTED(95091001, "expected customer is not existed"),
    PARAMETER_IS_INCOMPLETE(95091002, "parameter is incomplete"),

    ;

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
