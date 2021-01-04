package com.arbfintech.microservice.customer.object.enumerate;

import com.arbfintech.framework.component.core.base.BaseEnum;

/**
 * @author Fly_Roushan
 * @date 2020/11/30
 */
public enum CustomerOptInValue implements BaseEnum {

    /**
     * Do Not Call
     */
    DO_NOT_CALL(2, "Do Not Call", "doNotCall"),

    /**
     * Un Contactable
     */
    UN_CONTACTABLE(4, "Un Contactable", "unContactable"),

    /**
     * Is Marketing
     */
    IS_MARKETING(8, "Is Marketing", "isMarketing"),

    /**
     * Is Operation
     */
    IS_OPERATION(16, "Is Operation", "isOperation");

    CustomerOptInValue(Integer value, String text, String code) {
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
        return this.code;
    }

    private Integer value;
    private String text;
    private String code;
}
