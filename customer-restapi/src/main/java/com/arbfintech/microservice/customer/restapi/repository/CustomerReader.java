package com.arbfintech.microservice.customer.restapi.repository;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.arbfintech.framework.component.core.util.DateUtil;
import com.arbfintech.framework.component.database.core.BaseJdbcReader;
import com.arbfintech.microservice.customer.object.constant.CustomerJsonKey;
import com.arbfintech.microservice.customer.object.entity.CustomerOperationLog;
import com.arbfintech.microservice.customer.object.entity.CustomerOptIn;
import com.arbfintech.microservice.customer.object.entity.CustomerProfile;
import com.arbfintech.microservice.customer.object.util.AESCryptoUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * @author Fly_Roushan
 * @date 2021/1/4
 */
@Repository
public class CustomerReader extends BaseJdbcReader {
    public JSONArray findByEmailOrOpenId(Long id, String email, String openId) {
        Map<String, Object> paramMap = new HashMap<>(3);
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT * ");
        sb.append("FROM ");
        sb.append("customer c ");
        sb.append("LEFT JOIN ");
        sb.append("customer_profile cp ");
        sb.append("ON ");
        sb.append("c.id = cp.id ");
        sb.append("WHERE ");

        if (Objects.nonNull(id)) {
            sb.append("c.id = :id");
        } else if (StringUtils.isNotBlank(openId)) {
            sb.append("c.open_id = :openId");
        } else {
            sb.append("cp.email = :email");
        }

        paramMap.put(CustomerJsonKey.ID, id);
        paramMap.put(CustomerJsonKey.EMAIL, email);
        paramMap.put(CustomerJsonKey.OPEN_ID, openId);

        return namedJdbcTemplate().query(sb.toString(), paramMap, this::returnArray);
    }

    public List<CustomerOperationLog> getOperationLogByCondition(Long customerId, Integer logType, Integer withinDays) {
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("SELECT * FROM customer_operation_log");
        sqlBuilder.append(" WHERE customer_id = :customerId");
        sqlBuilder.append(" AND log_type = :logType");
        sqlBuilder.append(" AND (operated_at BETWEEN :start AND :end)");
        sqlBuilder.append(" ORDER BY id ASC");

        Map<String, Object> condition = new HashMap<>();
        condition.put("customerId", customerId);
        condition.put("logType", logType);
        condition.put("start", DateUtil.getLastDaysDate(withinDays).getTime());
        condition.put("end", DateUtil.getCurrentTimestamp());

        JSONArray jsonArray = namedJdbcTemplate().query(sqlBuilder.toString(), condition, this::returnArray);
        List<CustomerOperationLog> list = new ArrayList<>();
        if (!CollectionUtils.isEmpty(jsonArray)) {
            list = jsonArray.toJavaList(CustomerOperationLog.class);
        }

        return list;
    }

    public CustomerOptIn getCustomerOptInByCondition(Long customerId, Integer optInType) {
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("SELECT * FROM customer_opt_in");
        sqlBuilder.append(" WHERE customer_id = :customerId");
        sqlBuilder.append(" AND opt_in_type = :optInType");

        Map<String, Object> condition = new HashMap<>();
        condition.put("customerId", customerId);
        condition.put("optInType", optInType);

        JSONObject jsonObject = namedJdbcTemplate().query(sqlBuilder.toString(), condition, this::returnJson);
        CustomerOptIn customerOptIn = null;
        if (!CollectionUtils.isEmpty(jsonObject)) {
            customerOptIn = jsonObject.toJavaObject(CustomerOptIn.class);
        }

        return customerOptIn;
    }

    public CustomerProfile getCustomerInfoByEmail(String email) {
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("SELECT * FROM customer_profile");
        sqlBuilder.append(" WHERE email = :email");

        Map<String, Object> condition = new HashMap<>();
        condition.put("email", AESCryptoUtil.AESEncrypt(email));

        JSONObject jsonObject = namedJdbcTemplate().query(sqlBuilder.toString(), condition, this::returnJson);
        CustomerProfile customerProfile = null;
        if (!CollectionUtils.isEmpty(jsonObject)) {
            customerProfile = Objects.requireNonNull(AESCryptoUtil.decryptData(jsonObject)).toJavaObject(CustomerProfile.class);
        }

        return customerProfile;
    }
}
