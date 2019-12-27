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

    public JSONArray listCustomerBySSN(String ssn) {
        String customerStr = customerFeignClient.listCustomerBySSN(ssn);
        return JSON.parseArray(customerStr);
    }

    public Long getTheLatestCustomerIdBySSN(String ssn) {
        return customerFeignClient.getTheLatestCustomerIdBySSN(ssn);
    }

    public Long getLatestCustomerIdByEmailOrSSN(String email, String ssn) {
        return customerFeignClient.getLatestCustomerIdByEmailOrSSN(email, ssn);
    }

    public String verifyCustomerLoginData(String loginData) {
        return customerFeignClient.verifyCustomerLoginData(loginData);
    }

    public String doCustomerSignUp(String signUpData) {
        return customerFeignClient.doCustomerSignUp(signUpData);
    }

    public Long getLatestCustomerIdByUniqueKey(String cryptAccountAndRouting) {
        return customerFeignClient.getLatestCustomerIdByUniqueKey(cryptAccountAndRouting);
    }

    public String updateCustomer(String customers) {
        return customerFeignClient.updateCustomer(customers);
    }

    //    public Long saveCustomerByJDBC(JSONObject customerJson){
//        String customerStr = customerJson.toJSONString();
//        return customerFeignClient.saveCustomerByJDBC(customerStr);
//    }


}
