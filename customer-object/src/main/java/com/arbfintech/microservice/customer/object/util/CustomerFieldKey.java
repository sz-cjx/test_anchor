package com.arbfintech.microservice.customer.object.util;

import com.arbfintech.microservice.customer.object.constant.CustomerJsonKey;
import com.arbfintech.microservice.customer.object.entity.CustomerBankData;
import com.arbfintech.microservice.customer.object.entity.CustomerContactData;
import com.arbfintech.microservice.customer.object.entity.CustomerProfile;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CustomerFieldKey {

    /**
     * 定义包含电活号码和SSN的list
     */
    private static final List<String> REMOVE_MASK_LIST = Arrays.asList(
            CustomerJsonKey.HOME_PHONE,
            CustomerJsonKey.CELL_PHONE,
            CustomerJsonKey.ALTERNATIVE_PHONE,
            CustomerJsonKey.EMPLOYER_PHONE,
            CustomerJsonKey.SUPERVISOR_PHONE,
            CustomerJsonKey.BANK_PHONE,
            CustomerJsonKey.CARD_NO,
            CustomerJsonKey.SSN
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
     * 定义personal中影响计算的key
     */
    private static final List<String> PERSONAL_CALCULATE_FACTOR_LIST = Collections.singletonList(
            CustomerJsonKey.STATE

    );

    /**
     * 定义employment中影响计算的key
     */
    private static final List<String> EMPLOYMENT_CALCULATE_FACTOR_LIST = Arrays.asList(
            CustomerJsonKey.VOE,
            CustomerJsonKey.LAST_PAYDAY,
            CustomerJsonKey.PAYROLL_FREQUENCY
    );

    /**
     * 存入数据库时需要加密，取出时需要解密的字段
     */
    private static final List<String> ENCODE_FIELD_LIST = Arrays.asList(
            CustomerJsonKey.SSN,
            CustomerJsonKey.EMAIL,
            CustomerJsonKey.ALTERNATIVE_EMAIL,
            CustomerJsonKey.CELL_PHONE,
            CustomerJsonKey.HOME_PHONE,
            CustomerJsonKey.ALTERNATIVE_PHONE,
            CustomerJsonKey.ACCOUNT_NO,
            CustomerJsonKey.BIRTHDAY,
            CustomerJsonKey.FIRST_NAME,
            CustomerJsonKey.MIDDLE_NAME,
            CustomerJsonKey.LAST_NAME,
            CustomerJsonKey.ADDRESS
    );

    /**
     * 需要加密解密的class
     */
    private static final List<Class> ENCODE_CLASS_LIST = Arrays.asList(
            CustomerProfile.class,
            CustomerBankData.class,
            CustomerContactData.class
    );

    /**
     * 获取包含电话号码和SSN的list
     * @return
     */
    public static List<String> getRemoveMaskList() {
        return REMOVE_MASK_LIST;
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

    /**
     * 获取personal中影响计算的key
     * @return
     */
    public static List<String> getPersonalCalculateFactorList() {
        return PERSONAL_CALCULATE_FACTOR_LIST;
    }

    /**
     * 获取employment中影响计算的key
     * @return
     */
    public static List<String> getEmploymentCalculateFactorList() {
        return EMPLOYMENT_CALCULATE_FACTOR_LIST;
    }

    /**
     * 获取数据库存取需要加解密的字段
     * @return
     */
    public static List<String> getEncodeFieldList() {
        return ENCODE_FIELD_LIST;
    }

    /**
     * 获取需要加密解密的class
     * @return
     */
    public static List<Class> getEncodeClassList() {
        return ENCODE_CLASS_LIST;
    }
}