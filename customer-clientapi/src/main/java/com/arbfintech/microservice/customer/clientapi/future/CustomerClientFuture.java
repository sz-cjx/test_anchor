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
        return customerRepository.listCustomerByOptions(conditionStr);
    }

    private String findCustomerByCondition(String conditionStr) {
        return customerRepository.findCustomerByOptions(conditionStr);
    }
}
