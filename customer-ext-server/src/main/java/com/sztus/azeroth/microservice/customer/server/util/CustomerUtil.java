package com.sztus.azeroth.microservice.customer.server.util;

import com.sztus.framework.component.core.constant.GlobalConst;
import com.sztus.framework.component.core.util.CryptUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.regex.Pattern;

public class CustomerUtil {

    /**
     * SSN, RoutingNo, AccountNo
     * @param valueArray
     * @return
     */
    public static String generateUniqueCode(String... valueArray) {
        for (int i = 0; i < valueArray.length; i++) {
            if (StringUtils.isNotBlank(valueArray[i])) {
                valueArray[i] = valueArray[i].trim().toUpperCase();
            }
        }
        return CryptUtil.md5(StringUtils.join(valueArray, GlobalConst.STR_POUND));
    }

    public static String formatString(String data){
        String result = data.replaceAll(" ","").toLowerCase();
        return result;
    }

    public static String formatNumber(String data){
        String REGEX = "[^0-9]";
        String result = Pattern.compile(REGEX).matcher(data).replaceAll("").trim();
       return result;
    }
}
