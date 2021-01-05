package com.arbfintech.microservice.customer.restapi.repository;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.arbfintech.framework.component.core.util.DateUtil;
import com.arbfintech.framework.component.database.core.BaseJdbcWriter;
import com.arbfintech.microservice.customer.object.constant.CustomerJsonConst;
import com.arbfintech.microservice.customer.object.entity.CustomerOptIn;
import org.springframework.stereotype.Repository;

import java.util.Objects;

@Repository
public class CustomerWriter extends BaseJdbcWriter {

    public Long updateCustomerOptInData(String dataStr) {
        JSONArray dataArray = JSON.parseArray(dataStr);
        dataArray.forEach(dataObject -> {
            JSONObject dataJson = (JSONObject) dataObject;
            Long id = dataJson.getLong(CustomerJsonConst.ID);
            Long currentTimestamp = DateUtil.getCurrentTimestamp();
            if (Objects.isNull(id)) {
                dataJson.put(CustomerJsonConst.CREATED_AT, currentTimestamp);
            }
            dataJson.put(CustomerJsonConst.UPDATED_AT, currentTimestamp);
        });
        return save(CustomerOptIn.class, dataArray.toJSONString());
    }
}
