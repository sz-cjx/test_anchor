package com.arbfintech.microservice.customer.object.enumerate;

import com.arbfintech.framework.component.core.base.BaseEnum;
import java.util.HashMap;
import java.util.Map;

public enum CustomerContactTypeEnum implements BaseEnum {

    HOME_PHONE(1, "Home Phone", "homePhone"),
    CELL_PHONE(2, "Cell Phone", "cellPhone"),
    EMAIL(3, "Email", "email"),
    ALTERNATIVE_PHONE(4, "Alternative Phone", "alternativePhone"),
    ALTERNATIVE_EMAIL(5, "Alternative Email", "alternativeEmail");

    CustomerContactTypeEnum(Integer value, String text, String key) {
        this.value = value;
        this.text = text;
        this.key = key;
    }

    private final Integer value;
    private final String text;
    private final String key;

    private static final Map<Integer, CustomerContactTypeEnum> enumContainer = new HashMap<>();

    static {
        for (CustomerContactTypeEnum typeEnum : CustomerContactTypeEnum.values()) {
            enumContainer.put(typeEnum.getValue(), typeEnum);
        }
    }

    @Override
    public Integer getValue() {
        return value;
    }

    @Override
    public String getText() {
        return text;
    }

    public String getKey() {
        return key;
    }

    public static CustomerContactTypeEnum getEnumByvalue(Integer contactType) {
        return enumContainer.get(contactType);
    }
}
