package com.arbfintech.microservice.customer.object.enumerate;

import com.arbfintech.framework.component.core.base.BaseEnum;

/**
 * Verify枚举
 * 1、customer contact中的verify
 */
public enum VerifyEnum implements BaseEnum {
    NOT_VERIFIED(0, "Not Verified"),
    VERIFIED(1, "Verified");

    private Integer value;
    private String text;

    VerifyEnum(Integer value, String text) {
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
