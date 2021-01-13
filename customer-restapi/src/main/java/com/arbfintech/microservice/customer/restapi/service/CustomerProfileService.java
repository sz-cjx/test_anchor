package com.arbfintech.microservice.customer.restapi.service;

import com.alibaba.fastjson.JSONArray;
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

    public JSONArray searchCustomer(Long id, String email, String openId) {
        return customerReader.findByEmailOrOpenId(id, email, openId);
    }
}
