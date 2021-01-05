package com.arbfintech.microservice.customer.restapi.controller;

import com.arbfintech.microservice.customer.restapi.future.CustomerOptInFuture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/customer/opt-in")
public class CustomerOptInController {

    @Autowired
    private CustomerOptInFuture customerOptInFuture;

    @PostMapping("/get")
    public CompletableFuture<String> getCustomerOptInByCondition(
            @RequestBody String condition
    ) {
        return customerOptInFuture.getCustomerOptInByCondition(condition);
    }

    @PostMapping("/update")
    public CompletableFuture<String> updateCustomerOptInData(
            @RequestBody String dataStr
    ) {
        return customerOptInFuture.updateCustomerOptInData(dataStr);
    }
}