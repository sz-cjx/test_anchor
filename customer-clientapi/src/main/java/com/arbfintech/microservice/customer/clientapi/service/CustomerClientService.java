package com.arbfintech.microservice.customer.clientapi.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.arbfintech.framework.component.core.constant.ConditionTypeConst;
import com.arbfintech.framework.component.core.constant.JsonKeyConst;
import com.arbfintech.framework.component.core.type.SqlOption;
import com.arbfintech.microservice.customer.clientapi.client.CustomerHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerClientService {

    @Autowired
    private CustomerHttpClient customerHttpClient;

    public Long replaceCustomer(String dataStr) {
        return customerHttpClient.replaceCustomer(dataStr);
    }

    public String getCustomerById(Long id) {
        return customerHttpClient.getCustomerById(id).toJSONString();
    }

    public String listCustomerBySsnOrEmailOrNo(String conditionStr) {
        JSONObject paramJson = JSON.parseObject(conditionStr);
        String ssn = paramJson.getString(JsonKeyConst.SSN);
        String email = paramJson.getString(JsonKeyConst.EMAIL);
        String accountNo = paramJson.getString(JsonKeyConst.ACCOUNT_NO);
        String routingNo = paramJson.getString(JsonKeyConst.ROUTING_NO);

        SqlOption sqlOption = SqlOption.getInstance();
        sqlOption.addWhereFormat(ConditionTypeConst.OR, "ssn = '%s'", ssn);
        sqlOption.addWhereFormat(ConditionTypeConst.OR, "email = '%s'", email);
        sqlOption.addWhereFormat(ConditionTypeConst.OR, "(bank_account_no = '%s' AND bank_routing_no = '%s')", accountNo, routingNo);
        sqlOption.addField(JsonKeyConst.ID);
        sqlOption.addOrder("create_time DESC");
        sqlOption.addPage("LIMIT 1");
        return customerHttpClient.listCustomerByOptions(sqlOption.toString()).toJSONString();
    }

    public String findCustomerByCondition(String conditionStr) {
        return customerHttpClient.findCustomerByOptions(conditionStr).toJSONString();
    }
}
