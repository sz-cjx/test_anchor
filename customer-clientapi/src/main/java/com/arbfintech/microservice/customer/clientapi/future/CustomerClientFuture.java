package com.arbfintech.microservice.customer.clientapi.future;

import com.arbfintech.framework.component.core.annotation.AsyncTimed;
import com.arbfintech.microservice.customer.clientapi.service.CustomerClientService;
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
    private CustomerClientService customerClientService;

    @AsyncTimed
    public CompletableFuture<Long> replaceCustomer(String dataStr) {
        return CompletableFuture.supplyAsync(() -> customerClientService.replaceCustomer(dataStr));
    }

    @AsyncTimed
    public CompletableFuture<String> getCustomerById(Long id) {
        return CompletableFuture.supplyAsync(() -> customerClientService.getCustomerById(id));
    }

    @AsyncTimed
    public CompletableFuture<String> listCustomerBySsnOrEmailOrNo(String conditionStr) {
        return CompletableFuture.supplyAsync(() -> customerClientService.listCustomerBySsnOrEmailOrNo(conditionStr));
    }

    @AsyncTimed
    public CompletableFuture<String> findCustomerByCondition(String conditionStr) {
        return CompletableFuture.supplyAsync(() -> customerClientService.findCustomerByCondition(conditionStr));
    }
}
