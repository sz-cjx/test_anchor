package com.arbfintech.microservice.customer.object.enumerate;

import com.arbfintech.framework.component.core.base.BaseError;

public enum CustomerErrorCode implements BaseError {

    SUCCESS(1, "Success"),
    DEFAULT(0, "Default"),
    FAILURE(-1, "Failure"),

    QUERY_FAILURE_CUSTOMER_IS_EXISTED(-81010101, "Customer is existed."),
    QUERY_FAILURE_CUSTOMER_NOT_EXISTED(-81010102, "Customer is not existed."),
    QUERY_FAILURE_SEARCH_FAILED(-81010103, "Search failed, please enter at least one condition."),
    QUERY_FAILURE_MISS_REQUIRED_PARAM(-81010104, "Miss required param"),
    QUERY_FAILURE_NO_DATA_WAS_QUERIED(-81010105, "Query failure, the assigned condition cannot query any data"),

    CREATE_FAILURE_CUSTOMER_SAVE(-81010201, "Create failed, save customer"),
    CREATE_FAILURE_CUSTOMER_PROFILE_SAVE(-81010202, "Create failed, save customer profile"),
    CREATE_FAILURE_OPT_IN_SAVE(-81010203, "Create failed, save opt in");

    private Integer code;
    private String message;

    CustomerErrorCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }


    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}