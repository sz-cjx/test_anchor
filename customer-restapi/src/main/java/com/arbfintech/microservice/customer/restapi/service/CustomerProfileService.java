package com.arbfintech.microservice.customer.restapi.service;

import com.alibaba.fastjson.JSONObject;
import com.arbfintech.framework.component.database.core.SimpleJdbcReader;
import com.arbfintech.microservice.customer.restapi.repository.CustomerReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Fly_Roushan
 * @date 2021/1/4
 */
@Service
public class CustomerProfileService {

    @Autowired
    private CustomerReader customerReader;

    public JSONObject searchCustomerProfile(JSONObject requestParam) {
        return customerReader.findByEmailOrOpenId(requestParam);
    }
}
