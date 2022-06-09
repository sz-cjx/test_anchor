package com.sztus.azeroth.microservice.customer.server.object.util;

import com.sztus.azeroth.microservice.customer.server.type.constant.CustomerJsonKey;

import java.util.Arrays;
import java.util.List;

public class CustomerEncryptedFieldUtil {

    /**
     * 存入数据库时需要加密，取出时需要解密的字段
     */
    private static final List<String> ENCODE_FIELD_LIST = Arrays.asList(
            CustomerJsonKey.SSN,
            CustomerJsonKey.EMAIL,
            CustomerJsonKey.BIRTHDAY,
            CustomerJsonKey.FIRST_NAME,
            CustomerJsonKey.MIDDLE_NAME,
            CustomerJsonKey.LAST_NAME,
            CustomerJsonKey.ADDRESS,
            CustomerJsonKey.ADDRESS_TWO,
            CustomerJsonKey.BANK_ACCOUNT_NO
    );

    /**
     * 获取数据库存取需要加解密的字段
     *
     * @return
     */
    public static List<String> getEncodeFieldList() {
        return ENCODE_FIELD_LIST;
    }
}
