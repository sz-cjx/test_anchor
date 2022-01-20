package com.arbfintech.microservice.customer.object.enumerate;

import com.arbfintech.framework.component.core.base.BaseEnum;

public enum IBVRequestCodeStatusEnum implements BaseEnum {

    NOT_STARTED(0, "Not Started"),
    STARTED(1, "Started"),
    VERIFIED(3, "Verified"),
    ACCOUNT_ERROR(-1, "Account Error"),
    BANK_ERROR(-2, "Bank Error");

    IBVRequestCodeStatusEnum(Integer value, String text) {
        this.value = value;
        this.text = text;
    }

    private Integer value;
    private String text;

    @Override
    public Integer getValue() {
        return value;
    }
    @Override
    public String getText() {
        return text;
    }

}
