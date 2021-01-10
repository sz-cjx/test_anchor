package com.arbfintech.microservice.customer.restapi.util;

import com.alibaba.fastjson.JSONObject;
import com.arbfintech.framework.component.core.constant.GlobalConst;
import com.arbfintech.framework.component.core.util.CryptUtil;
import com.arbfintech.microservice.customer.object.constant.CustomerJsonKey;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author Fly_Roushan
 * @date 2021/1/4
 */
public class CustomerUtil {

    public static Boolean isValid(JSONObject dataJson) {
        if (CollectionUtils.isEmpty(dataJson)) {
            return false;
        }
        List<String> keyList = Lists.newArrayList(CustomerJsonKey.SSN, CustomerJsonKey.EMAIL, CustomerJsonKey.BANK_ROUTING_NO, CustomerJsonKey.BANK_ACCOUNT_NO);
        for (String key : keyList) {
            if (StringUtils.isBlank(dataJson.getString(key))) {
                return false;
            }
        }
        return true;
    }

    public static String generateUniqueCode(String... valueArray) {
        for (int i = 0; i < valueArray.length; i++) {
            valueArray[i] = valueArray[i].trim().toUpperCase();
        }
        return CryptUtil.md5(StringUtils.join(valueArray, GlobalConst.STR_POUND));
    }

    public static String removeLast(StringBuilder sb) {
        return removeLast(sb, GlobalConst.STR_COMMA);
    }

    public static String removeLast(StringBuilder sb, String cs) {
        return sb.replace(sb.lastIndexOf(cs), sb.length(), org.apache.commons.lang.StringUtils.EMPTY).toString();
    }
}
