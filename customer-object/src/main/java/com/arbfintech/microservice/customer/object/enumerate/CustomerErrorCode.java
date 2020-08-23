package com.arbfintech.microservice.customer.object.enumerate;

import com.arbfintech.framework.component.core.base.BaseError;

public enum CustomerErrorCode implements BaseError {

    SUCCESS(1, "Success"),
    UNKNOWN(0, "Unknown"),
    FAILURE(-1, "Failure"),

    CUSTOMER_IS_NOT_EXISTED(-81020031, "The assigned customer is not existed."),
    QUERY_FAILURE_NO_CONDITION(-1, "Query failure, at least set one condition."),
    ;

    private Integer code;
    private String message;

    CustomerErrorCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

}
