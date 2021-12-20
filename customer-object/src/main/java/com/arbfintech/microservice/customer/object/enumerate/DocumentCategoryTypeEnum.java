package com.arbfintech.microservice.customer.object.enumerate;

import com.arbfintech.framework.component.core.base.BaseEnum;

public enum DocumentCategoryTypeEnum implements BaseEnum {

    ACCOUNT(30001, "Account"),
    LOAN(30002, "Loan");

    DocumentCategoryTypeEnum(Integer value, String text) {
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
