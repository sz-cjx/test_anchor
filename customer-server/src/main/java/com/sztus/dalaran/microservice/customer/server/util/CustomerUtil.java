package com.sztus.dalaran.microservice.customer.server.util;

import com.sztus.framework.component.core.constant.GlobalConst;
import com.sztus.framework.component.core.util.CryptUtil;
import org.apache.commons.lang3.StringUtils;

/**
 * @author Fly_Roushan
 * @date 2021/1/4
 */
public class CustomerUtil {

    /**
     * ssn
     * routingNo
     * accountNo
     *
     * @param valueArray
     * @return
     */
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
        return sb.replace(sb.lastIndexOf(cs), sb.length(), StringUtils.EMPTY).toString();
    }
}
