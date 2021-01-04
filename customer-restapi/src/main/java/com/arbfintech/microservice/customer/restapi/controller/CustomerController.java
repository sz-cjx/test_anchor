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
public class CustomerController {

    @Autowired
    private CustomerFuture customerFuture;

    @PostMapping("/customer/create")
    public CompletableFuture<String> createCustomer(
            @RequestBody JSONObject dataJson
    ) {
        return customerFuture.createCustomer(dataJson);
    }

    @GetMapping("/customer/search")
    public CompletableFuture<String> searchCustomer(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String email,
            @RequestParam List<String> features
    ) {
        return customerFuture.searchCustomer(id, email, features);
    }
}
