package com.arbfintech.microservice.customer.clientapi.service;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.arbfintech.microservice.customer.clientapi.client.CustomerFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerClientService {

    @Autowired
    CustomerFeignClient customerFeignClient;

    public Long replaceCustomer(JSONObject customerJson) {
        String customerStr = customerJson.toJSONString();
        return customerFeignClient.replaceCustomer(customerStr);
    }

    public JSONObject getCustomerById(Long id) {
        String customerStr = customerFeignClient.getCustomerById(id);
        return JSONObject.parseObject(customerStr);
    }

    public JSONArray listCustomerByOptions(String optionStr) {
        String customerStr = customerFeignClient.listCustomerByOptions(optionStr);
        return JSON.parseArray(customerStr);
    }

}
