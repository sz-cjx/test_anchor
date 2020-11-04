package com.arbfintech.microservice.customer.object.enumerate;

import com.arbfintech.framework.component.core.base.BaseEnum;

public enum CustomerOptInTypeEnum implements BaseEnum {

    /**
     * email
     */
    EMAIL(1, "Email"),

    /**
     * telephone
     */
    CELL_PHONE(2, "Telephone");

    CustomerOptInTypeEnum(Integer value, String text) {
        this.value = value;
        this.text = text;
    }

    @Override
    public Integer getValue() {
        return this.value;
    }

    @Override
    public String getText() {
        return this.text;
    }

    private Integer value;
    private String text;

}
