package com.arbfintech.microservice.customer.restapi.repository;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.arbfintech.framework.component.database.core.BaseJdbcReader;
import com.arbfintech.microservice.customer.object.constant.CustomerJsonKey;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Fly_Roushan
 * @date 2021/1/4
 */
@Repository
public class CustomerReader extends BaseJdbcReader {
    public JSONArray findByEmailOrOpenId(String email, String openId) {
        Map<String, Object> paramMap = new HashMap<>(2);
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT * ");
        sb.append("FROM ");
        sb.append("customer c ");
        sb.append("LEFT JOIN ");
        sb.append("customer_profile cp ");
        sb.append("ON ");
        sb.append("c.id = cp.id ");
        sb.append("WHERE ");

        if (StringUtils.isBlank(email)) {
            sb.append("c.open_id = :openId");
        } else {
            sb.append("cp.email = :email");
        }

        paramMap.put(CustomerJsonKey.EMAIL, email);
        paramMap.put(CustomerJsonKey.OPEN_ID, openId);

        return namedJdbcTemplate().query(sb.toString(), paramMap, this::returnArray);
    }
}
