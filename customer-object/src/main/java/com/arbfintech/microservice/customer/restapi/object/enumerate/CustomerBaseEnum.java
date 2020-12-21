package com.arbfintech.microservice.customer.restapi.object.enumerate;

import java.util.Objects;

/**
 * 枚举对象基类
 *
 * @author Fly_Roushan
 */
public interface CustomerBaseEnum {

    /**
     * 获取值信息
     *
     * @return
     */
    Integer getValue();

    /**
     * 获取文本信息
     *
     * @return
     */
    String getText();

    /**
     * 获取Code信息
     *
     * @return
     */
    String getCode();

    /**
     * 根据值获取枚举对象
     *
     * @param enumClass
     * @param value
     * @return
     */
    static <T extends CustomerBaseEnum> T fromValue(Class<T> enumClass, Integer value) {
        if (value == null) {
            return null;
        }

        for (T e : enumClass.getEnumConstants()) {
            if (Objects.equals(value, e.getValue())) {
                return e;
            }
        }

        throw new IllegalArgumentException(String.format("No enum value %d of %s found.", value, enumClass.getCanonicalName()));
    }

    /**
     * 根据文本获取枚举对象
     *
     * @param enumClass
     * @param text
     * @return
     */
    static <T extends CustomerBaseEnum> T fromText(Class<T> enumClass, String text) {
        for (T e : enumClass.getEnumConstants()) {
            if (Objects.equals(text, e.getText())) {
                return e;
            }
        }

        throw new IllegalArgumentException(String.format("No enum text %s of %s found.", text, enumClass.getCanonicalName()));
    }

    /**
     * 根据文本获取枚举对象
     *
     * @param enumClass
     * @param code
     * @return
     */
    static <T extends CustomerBaseEnum> T fromCode(Class<T> enumClass, String code) {
        for (T e : enumClass.getEnumConstants()) {
            if (Objects.equals(code, e.getCode())) {
                return e;
            }
        }

        throw new IllegalArgumentException(String.format("No enum code %s of %s found.", code, enumClass.getCanonicalName()));
    }
}
