package com.arbfintech.microservice.customer.object.enumerate;

import com.arbfintech.framework.component.core.base.BaseEnum;

public enum CustomerOptInType implements BaseEnum {

    /**
     * email
     */
    EMAIL(1, "Email", "email"),

    /**
     * homephone
     */
    HOME_PHONE(2, "Home Phone", "HOME_PHONE"),

    /**
     * cellphone
     */
    CELL_PHONE (3, "Cell Phone", "CELL_PHONE");

    CustomerOptInType(Integer value, String text, String code) {
        this.value = value;
        this.text = text;
        this.code = code;
    }

    @Override
    public Integer getValue() {
        return this.value;
    }

    @Override
    public String getText() {
        return this.text;
    }

    public String getCode() {
        return code;
    }

    private Integer value;
    private String text;
    private String code;

}
