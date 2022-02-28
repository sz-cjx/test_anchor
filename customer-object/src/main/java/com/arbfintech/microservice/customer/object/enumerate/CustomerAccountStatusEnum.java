package com.arbfintech.microservice.customer.object.enumerate;

import com.arbfintech.framework.component.core.base.BaseEnum;

public enum CustomerAccountStatusEnum implements BaseEnum {

    INACTIVE(1, "Inactive"),
    ACTIVE(2, "Active"),
    DISABLED(3, "Disabled");

    CustomerAccountStatusEnum(Integer value, String text) {
        this.value = value;
        this.text = text;
    }

    private final Integer value;
    private final String text;

    @Override
    public Integer getValue() {
        return value;
    }

    @Override
    public String getText() {
        return text;
    }

}
