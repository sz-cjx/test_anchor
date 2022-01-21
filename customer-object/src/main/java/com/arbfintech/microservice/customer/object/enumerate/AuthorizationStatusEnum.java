package com.arbfintech.microservice.customer.object.enumerate;

import com.arbfintech.framework.component.core.base.BaseEnum;

public enum AuthorizationStatusEnum implements BaseEnum {
    UNAUTHORIZED(0, "Unauthorized"),
    AUTHORIZE(1, "Authorize");


    private final Integer value;
    private final String text;

    AuthorizationStatusEnum(Integer value, String text) {
        this.value = value;
        this.text = text;
    }

    @Override
    public Integer getValue() {
        return value;
    }

    @Override
    public String getText() {
        return text;
    }
}
