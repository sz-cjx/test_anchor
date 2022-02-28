package com.arbfintech.microservice.customer.object.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.arbfintech.framework.component.core.util.JsonUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Objects;

public class ExtentionJsonUtil extends JsonUtil {

    public static Boolean containsKey(Object object, List<String> list) {
        Boolean result = true;

        if (Objects.nonNull(object)) {
            JSONObject dataJson = JSON.parseObject(JSONObject.toJSONString(object));
            for (String key : list) {
                if (!dataJson.containsKey(key) || StringUtils.isBlank(dataJson.getString(key))) {
                    result = false;
                    break;
                }
            }
        } else {
            result = false;
        }

        return result;
    }
}
