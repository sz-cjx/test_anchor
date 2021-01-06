package com.arbfintech.microservice.customer.restapi.repository;

import com.alibaba.fastjson.JSONArray;
import com.arbfintech.framework.component.database.core.BaseJdbcWriter;
import com.arbfintech.microservice.customer.object.entity.CustomerOptIn;
import org.springframework.stereotype.Repository;

@Repository
public class CustomerWriter extends BaseJdbcWriter {

    public Long updateCustomerOptInData(JSONArray dataArray) {
        return save(CustomerOptIn.class, dataArray.toJSONString());
    }
}
