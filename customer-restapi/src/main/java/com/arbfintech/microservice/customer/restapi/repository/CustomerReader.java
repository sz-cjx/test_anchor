package com.arbfintech.microservice.customer.restapi.repository;

import com.alibaba.fastjson.JSONObject;
import com.arbfintech.framework.component.core.constant.ConditionTypeConst;
import com.arbfintech.framework.component.core.type.SqlOption;
import com.arbfintech.framework.component.database.core.BaseJdbcReader;
import com.arbfintech.microservice.customer.object.constant.CustomerJsonKey;
import com.arbfintech.microservice.customer.object.entity.CustomerOptIn;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Fly_Roushan
 * @date 2021/1/4
 */
@Repository
public class CustomerReader extends BaseJdbcReader {
    public JSONObject findByIdOrEmail(Long id, String email) {
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
        if (id != null) {
            sb.append("cp.id =:id");
        } else {
            sb.append("cp.email =:email");
        }

        paramMap.put(CustomerJsonKey.ID, id);
        paramMap.put(CustomerJsonKey.EMAIL, email);

        return namedJdbcTemplate().query(sb.toString(), paramMap, this::returnJson);
    }

    public CustomerOptIn getCustomerOptInByCondition(Long customerId, Long optInType) {
        SqlOption optInOption = SqlOption.getInstance();
        optInOption.whereFormat(ConditionTypeConst.AND, "customer_id = '%d'", customerId);
        optInOption.whereFormat(ConditionTypeConst.AND, "opt_in_type = '%d'", optInType);
        return findByOptions(CustomerOptIn.class, optInOption.toString());
    }

}
