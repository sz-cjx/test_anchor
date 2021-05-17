package com.arbfintech.microservice.customer.object.enumerate;

import com.arbfintech.framework.component.core.base.BaseError;

public enum CustomerErrorCode implements BaseError {

    FAILURE_MISS_REQUIRED_PARAM(-81010100, "Miss required param."),

    QUERY_FAILURE_CUSTOMER_IS_EXISTED(-81010101, "Customer is existed."),
    QUERY_FAILURE_CUSTOMER_NOT_EXISTED(-81010102, "Customer is not existed."),
    QUERY_FAILURE_SEARCH_FAILED(-81010103, "Search failed, please enter at least one condition."),
    QUERY_FAILURE_MISS_REQUIRED_PARAM(-81010104, "Query failure, Miss required param"),
    QUERY_FAILURE_NO_DATA_WAS_QUERIED(-81010105, "Query failure, the assigned condition cannot query any data"),

    CREATE_FAILURE_CUSTOMER_SAVE(-81010201, "Create failed, save customer"),
    CREATE_FAILURE_CUSTOMER_PROFILE_SAVE(-81010202, "Create failed, save customer profile"),
    CREATE_FAILURE_OPT_IN_SAVE(-81010203, "Create failed, save opt in"),

    UPDATE_FAILURE_MISS_ID(-81010301, "Update failed, miss id"),
    UPDATE_FAILURE_CUSTOMER_SAVE(-81010302, "Update failed, save customer"),
    UPDATE_FAILURE_CUSTOMER_PROFILE_SAVE(-81010303, "Update failed, save customer profile"),
    UPDATE_FAILURE_OPT_IN_SAVE(-81010304, "Update failed, save opt in"),
    UPDATE_FAILURE_MISS_OPEN_ID(-81010305, "Update failed, miss open id"),
    UPDATE_FAILURE_HAD_UNSUBSCRIBE(-81010306, "Update failed, customer had unsubscribe");

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
