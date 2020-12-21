package com.arbfintech.microservice.customer.restapi.object.enumerate;

/**
 * @author Fly_Roushan
 * @date 2020/11/30
 */
public enum CustomerOptInValueEnum implements CustomerBaseEnum {

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

    CustomerOptInValueEnum(Integer value, String text, String code) {
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

    @Override
    public String getCode() {
        return this.code;
    }

    private Integer value;
    private String text;
    private String code;
}
