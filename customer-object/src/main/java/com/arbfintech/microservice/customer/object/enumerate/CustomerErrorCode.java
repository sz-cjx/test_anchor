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
    UPDATE_FAILURE_HAD_UNSUBSCRIBE(-81010306, "Update failed, customer had unsubscribe"),

    FAILURE_PROFILE_NOT_EXIST(-81010400, "Profile feature is not exist"),
    FAILURE_FAILED_TO_UPDATE_DATA(-81010401, "The data was not updated successfully"),
    FAILURE_UPLOAD_FILE(-81010402, "Failure to upload file"),
    FAILURE_CHANGE_TYPE_NOT_EXIST(-81010403, "Change password type is not exist"),
    FAILURE_LOGIN_PASSWORD_INCORRECT(-81010404, "Current login password is incorrect"),
    FAILURE_PAYMENT_PASSWORD_INCORRECT(-81010405, "Current payment password is incorrect"),
    FAILURE_UPDATE_BANK_CARD_EXPIRATION_DATE(-81010406, "The expiration time of the bank card must be after the current time"),
    FAILURE_QUERY_DATA_IS_EXISTED(-81010407, "Data is not existed")
    ;

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
