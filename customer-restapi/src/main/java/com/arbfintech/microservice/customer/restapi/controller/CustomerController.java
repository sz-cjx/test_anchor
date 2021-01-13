package com.arbfintech.microservice.customer.restapi.controller;

import com.alibaba.fastjson.JSONObject;
import com.arbfintech.microservice.customer.restapi.future.CustomerFuture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * @author Fly_Roushan
 * @date 2020/12/17
 */
@RestController
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    private CustomerFuture customerFuture;

    @PostMapping("/create")
    public CompletableFuture<String> createCustomer(
            @RequestBody JSONObject dataJson
    ) {
        return customerFuture.createCustomer(dataJson);
    }

    @GetMapping("/search")
    public CompletableFuture<String> searchCustomer(
            @RequestParam(required = false) Long customerId,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String openId
    ) {
        return customerFuture.searchCustomer(customerId, email, openId);
    }

    @GetMapping("/unique")
    public CompletableFuture<String> getCustomerByUnique(
            @RequestParam String ssn,
            @RequestParam String email,
            @RequestParam String routingNo,
            @RequestParam String accountNo
    ) {
        return customerFuture.getCustomerByUnique(ssn, email, routingNo, accountNo);
    }

    @GetMapping("/load-features")
    public CompletableFuture<String> loadFeatures(
            @RequestParam Long customerId,
            @RequestParam List<String> features
    ) {
        return customerFuture.loadFeatures(customerId, features);
    }

    @PutMapping("/update-features")
    public CompletableFuture<String> updateFeatures(
            @RequestBody JSONObject dataJson
    ) {
        return customerFuture.updateFeatures(dataJson);
    }

    @PutMapping("/update")
    public CompletableFuture<String> updateCustomerProfile(
            @RequestBody JSONObject dataJson
    ) {
        return customerFuture.updateCustomerProfile(dataJson);
    }
}
