package com.arbfintech.microservice.customer.object.enumerate;

import com.arbfintech.framework.component.core.base.BaseEnum;

public enum CustomerEventTypeEnum implements BaseEnum {

    CUSTOMER_EVENT(90000, "Customer Event"),
    CUSTOMER_REGISTRY_CHANGE(90001, "Customer Registry Change"),
    CUSTOMER_SIGN_IN(90002, "Customer Sign In"),
    CUSTOMER_NOTE(90003, "Customer NOote");

    CustomerEventTypeEnum(Integer value, String text) {
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
