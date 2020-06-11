package com.arbfintech.microservice.customer.clientapi.future;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.arbfintech.framework.component.core.annotation.AsyncTimed;
import com.arbfintech.framework.component.core.constant.ConditionTypeConst;
import com.arbfintech.framework.component.core.constant.JsonKeyConst;
import com.arbfintech.framework.component.core.type.SqlOption;
import com.arbfintech.microservice.customer.domain.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

/**
 * @author Baron
 */
@Component
public class CustomerClientFuture {

    @Autowired
    private CustomerRepository customerRepository;

    @AsyncTimed
    public CompletableFuture<Long> replaceCustomer(String dataStr) {
        return CompletableFuture.supplyAsync(() -> replaceCustomers(dataStr));
    }

    @AsyncTimed
    public CompletableFuture<String> getCustomerById(Long id) {
        return CompletableFuture.supplyAsync(() -> findCustomerById(id));
    }

    @AsyncTimed
    public CompletableFuture<String> listCustomerBySsnOrEmailOrNo(String conditionStr) {
        return CompletableFuture.supplyAsync(() -> findCustomerListBySsnOrEmailOrNo(conditionStr));
    }

    @AsyncTimed
    public CompletableFuture<String> getCustomerByCondition(String conditionStr) {
        return CompletableFuture.supplyAsync(() -> findCustomerByCondition(conditionStr));
    }

    private Long replaceCustomers(String dataStr) {
        return customerRepository.replaceCustomer(dataStr);
    }

    private String findCustomerById(Long id) {
        return customerRepository.getCustomerById(id);
    }

    private String findCustomerListBySsnOrEmailOrNo(String conditionStr) {
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
        return customerRepository.listCustomerByOptions(sqlOption.toString());
    }

    private String findCustomerByCondition(String conditionStr) {
        return customerRepository.findCustomerByOptions(conditionStr);
    }
}
