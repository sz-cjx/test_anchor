package com.arbfintech.microservice.customer.object.enumerate;

import com.arbfintech.framework.component.core.base.BaseEnum;

public enum ChangePasswordTypeEnum implements BaseEnum {

    LOGIN(10001, "Login"),
    PAYMENT(10002, "Payment");

    ChangePasswordTypeEnum(Integer value, String text) {
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
