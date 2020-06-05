package com.arbfintech.microservice.customer.clientapi.future;

import com.arbfintech.framework.component.core.annotation.AsyncTimed;
import com.arbfintech.microservice.customer.clientapi.client.CustomerHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

/**
 * @author Baron
 *
 */
@Component
public class CustomerClientFuture {

    @Autowired
    private CustomerHttpClient customerHttpClient;

    @AsyncTimed
    public CompletableFuture<Long> replaceCustomer(String dataStr) {
        return CompletableFuture.supplyAsync(() -> customerHttpClient.replaceCustomer(dataStr));
    }

    public CompletableFuture<String> getCustomerById(Long id) {
        return CompletableFuture.supplyAsync(() -> customerHttpClient.getCustomerById(id).toJSONString());
    }

    public CompletableFuture<String> listCustomerBySsnOrEmailOrNo(String conditionStr) {
        return CompletableFuture.supplyAsync(() -> customerHttpClient.listCustomerBySsnOrEmailOrNo(conditionStr).toJSONString());
    }

    public CompletableFuture<String> findCustomerByCondition(String conditionStr) {
        return CompletableFuture.supplyAsync(() -> customerHttpClient.findCustomerByOptions(conditionStr).toJSONString());
    }
}
