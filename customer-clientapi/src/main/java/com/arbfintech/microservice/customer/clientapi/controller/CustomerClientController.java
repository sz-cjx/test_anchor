package com.arbfintech.microservice.customer.clientapi.controller;

import com.arbfintech.microservice.customer.clientapi.future.CustomerClientFuture;
import com.arbfintech.microservice.customer.object.constant.ClientApiUrl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping(ClientApiUrl.API_CUSTOMER)
public class CustomerClientController {

    @Autowired
    private CustomerClientFuture customerClientFuture;

    @PostMapping(ClientApiUrl.REPLACE_CUSTOMER)
    public CompletableFuture<Long> replaceCustomer(
            @RequestBody String dataStr
    ) {
        return customerClientFuture.replaceCustomer(dataStr);
    }

    @GetMapping(ClientApiUrl.GET_CUSTOMER_BY_ID)
    public CompletableFuture<String> getCustomerById(
            @PathVariable("id") Long id
    ) {
        return customerClientFuture.getCustomerById(id);
    }

    @GetMapping(ClientApiUrl.LIST_CUSTOMER_BY_SSN_OR_EMAIL_OR_NUM)
    public CompletableFuture<String> listCustomerBySsnOrEmailOrNo(
            @RequestParam("conditions") String conditionStr
    ) {
        return customerClientFuture.listCustomerBySsnOrEmailOrNo(conditionStr);
    }

    @GetMapping(ClientApiUrl.GET_CUSTOMER_BY_CONDITIONS)
    public CompletableFuture<String> getCustomerByCondition(
            @RequestParam("conditions") String conditionStr
    ) {
        return customerClientFuture.getCustomerByCondition(conditionStr);
    }
}