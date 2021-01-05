package com.arbfintech.microservice.customer.restapi.controller;

import com.arbfintech.microservice.customer.restapi.future.CustomerOptInFuture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @PutMapping("/update")
    public CompletableFuture<String> updateCustomerOptInData(
            @RequestBody String dataStr
    ) {
        return customerOptInFuture.updateCustomerOptInData(dataStr);
    }
}