package com.arbfintech.microservice.customer.object.enumerate;

import com.arbfintech.framework.component.core.base.BaseError;

public enum CustomerErrorCode implements BaseError {

    SUCCESS(1, "Success"),
    UNKNOWN(0, "Unknown"),
    FAILURE(-1, "Failure"),

    FAILURE_LACK_OF_INPUT_DATA(-81020001, "Lack of input data"),
    FAILURE_FAILED_TO_UPDATE_DATA(-81020002, "Failed to update data"),

    OPT_IN_DATA_FAILED_TO_SAVE_DATA(-81020020, "Failed to save opt-in data"),
    OPT_IN_DATA_FAILED_TO_UPDATE_DATA(-81020021, "Failed to update opt-in data");

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
