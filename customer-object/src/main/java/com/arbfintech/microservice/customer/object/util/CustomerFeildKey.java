package com.arbfintech.microservice.customer.object.util;

import com.arbfintech.microservice.customer.object.constant.CustomerJsonKey;

import java.util.Arrays;
import java.util.List;

public class CustomerFeildKey {

    /**
     * 定义包含电活号码的list
     */
    private static final List<String> CONTAIN_PHONE_NUMBER_LIST = Arrays.asList(
            CustomerJsonKey.HOME_PHONE,
            CustomerJsonKey.ALTERNATIVE_PHONE,
            CustomerJsonKey.EMPLOYER_PHONE,
            CustomerJsonKey.SUPERVISOR_PHONE
    );

    /**
     * 定义需要时间字符串转为时间戳的list
     */
    private static final List<String> TIME_CONVERSION_LIST = Arrays.asList(
            CustomerJsonKey.BIRTHDAY,
            CustomerJsonKey.LAST_PAYDAY
    );

    /**
     * 定义包含name的list
     */
    private static final List<String> CONVERSION_LOWERCASE_LIST = Arrays.asList(
            CustomerJsonKey.FIRST_NAME,
            CustomerJsonKey.MIDDLE_NAME,
            CustomerJsonKey.LAST_NAME,
            CustomerJsonKey.EMPLOYER_NAME,
            CustomerJsonKey.SUPERVISOR_NAME,
            CustomerJsonKey.EMAIL,
            CustomerJsonKey.ALTERNATIVE_EMAIL
    );

    /**
     * 获取包含电话号码的list
     * @return
     */
    public static List<String> getContainPhoneNumberList() {
        return CONTAIN_PHONE_NUMBER_LIST;
    }

    /**
     * 获取时间字符串转时间戳的list
     * @return
     */
    public static List<String> getTimeConversionList() {
        return TIME_CONVERSION_LIST;
    }

    public static List<String> getConversionLowercaseList() {
        return CONVERSION_LOWERCASE_LIST;
    }
}
