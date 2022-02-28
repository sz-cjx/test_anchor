package com.arbfintech.microservice.customer.object.enumerate;

import com.arbfintech.framework.component.core.base.BaseEnum;

public enum VOBTypeEnum implements BaseEnum {
    BANK_CALL(1, "Bank Call"),
    DECISION_LOGIC(2, "Decision Logic"),
    DOCUMENTS(3, "Documents");

    private final Integer value;
    private final String text;

    VOBTypeEnum(Integer value, String text) {
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
