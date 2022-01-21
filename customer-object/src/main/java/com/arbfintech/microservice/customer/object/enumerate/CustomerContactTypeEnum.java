package com.arbfintech.microservice.customer.object.enumerate;

import com.arbfintech.framework.component.core.base.BaseEnum;

public enum CustomerContactTypeEnum implements BaseEnum {

    HOME_PHONE(1, "Home Phone"),
    CELL_PHONE(2, "Cell Phone"),
    EMAIL(3, "Email"),
    ALTERNATIVE_PHONE(4, "Alternative Phone"),
    ALTERNATIVE_EMAIL(5, "Alternative Email");

    CustomerContactTypeEnum(Integer value, String text) {
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
