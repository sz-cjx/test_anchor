package com.sztus.azeroth.microservice.customer.server.util;

import com.sztus.azeroth.microservice.customer.server.type.constant.CustomerJsonKey;
import com.sztus.azeroth.microservice.customer.server.type.constant.DbKey;

import java.util.Arrays;
import java.util.List;

public class CustomerFeildKey {
    private static final List<String> FORMAT_STRING_LIST = Arrays.asList(
            CustomerJsonKey.EMAIL,
            CustomerJsonKey.EMPLOYER_EMAIL

    );
    private static final List<String> FORMAT_NUMBER_LIST = Arrays.asList(
            CustomerJsonKey.PHONE,
            CustomerJsonKey.SSN,
            CustomerJsonKey.BANK_ACCOUNT_NO,
            CustomerJsonKey.BANK_PHONE,
            CustomerJsonKey.BANK_ROUTING_NO,
            CustomerJsonKey.EMPLOYER_PHONE,
            CustomerJsonKey.SUPERVISOR_Phone
    );

    private static  final List<String> LOWER_CASE_LIST = Arrays.asList(
            CustomerJsonKey.FIRST_NAME,
            CustomerJsonKey.MIDDLE_NAME,
            CustomerJsonKey.LAST_NAME
    );

    public static List<String> getFormatStringList() {
        return FORMAT_STRING_LIST;
    }

    public static List<String> getFormatNumberList() {
        return FORMAT_NUMBER_LIST;
    }

    public static List<String> getLowerCaseList(){
        return LOWER_CASE_LIST;
    }
}
