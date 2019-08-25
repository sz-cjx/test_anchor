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

    public Long addCustomer(JSONObject customerJson) {
        String customerStr = customerJson.toJSONString();
        return customerFeignClient.addCustomer(customerStr);
    }

    public JSONObject getCustomerById(Long id) {
        String customerStr = customerFeignClient.getCustomerById(id);
        return JSONObject.parseObject(customerStr);
    }

    public Integer setCustomerById(Long id, JSONObject json) {
        String customerStr = json.toJSONString();
        return customerFeignClient.setCustomerById(id, customerStr);

    }

    public JSONArray listCustomerByConditions(String conditionStr, String conditionType) {
        String customerStr = customerFeignClient.listCustomerByConditions(conditionStr, conditionType);
        return JSON.parseArray(customerStr);
    }

}
