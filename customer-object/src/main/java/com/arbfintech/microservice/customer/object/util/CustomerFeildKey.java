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
     * 获取包含电话号码的list
     * @return
     */
    public static List<String> getContainPhoneNumberList() {
        return CONTAIN_PHONE_NUMBER_LIST;
    }
}
