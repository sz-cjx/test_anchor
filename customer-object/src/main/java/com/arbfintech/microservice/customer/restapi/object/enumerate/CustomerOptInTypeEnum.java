package com.arbfintech.microservice.customer.restapi.object.enumerate;

public enum CustomerOptInTypeEnum implements CustomerBaseEnum {

    /**
     * email
     */
    EMAIL(1, "Email", "email"),

    /**
     * telephone
     */
    TELEPHONE(2, "Telephone", "telephone");

    CustomerOptInTypeEnum(Integer value, String text, String code) {
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
        return code;
    }

    private Integer value;
    private String text;
    private String code;

}
