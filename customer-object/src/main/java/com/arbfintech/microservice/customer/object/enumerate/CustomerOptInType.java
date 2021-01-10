package com.arbfintech.microservice.customer.object.enumerate;

import com.arbfintech.framework.component.core.base.BaseEnum;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Fly_Roushan
 * @date 2021/1/6
 */
public enum CustomerOptInType implements BaseEnum {

    /**
     * Email
     */
    EMAIL(1, "Email", "EMAIL", "email"),

    /**
     * Home Phone
     */
    HOME_PHONE(2, "Home Phone", "HOME_PHONE", "homePhone"),

    /**
     * Home Phone
     */
    CELL_PHONE(3, "Cell Phone", "CELL_PHONE", "cellPhone");

    CustomerOptInType(Integer value, String text, String code, String key) {
        this.value = value;
        this.text = text;
        this.code = code;
        this.key = key;
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

    public String getKey() {
        return key;
    }

    private Integer value;
    private String text;
    private String code;
    private String key;

}