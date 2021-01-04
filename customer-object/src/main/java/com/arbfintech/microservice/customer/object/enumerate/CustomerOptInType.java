package com.arbfintech.microservice.customer.object.enumerate;

import com.arbfintech.framework.component.core.base.BaseEnum;

public enum CustomerOptInType implements BaseEnum {

    /**
     * email
     */
    EMAIL(1, "Email", "email"),

    /**
     * telephone
     */
    TELEPHONE(2, "Telephone", "telephone");

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
