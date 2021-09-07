package com.arbfintech.microservice.customer.restapi.controller;

import com.alibaba.fastjson.JSONObject;
import com.arbfintech.microservice.customer.object.entity.CustomerOptInData;
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
        return CompletableFuture.supplyAsync(() -> customerFuture.createCustomer(dataJson));
    }

    @GetMapping("/search")
    public CompletableFuture<String> searchCustomer(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String openId
    ) {
        return customerFuture.searchCustomer(id, email, openId);
    }

    @GetMapping("/unique")
    public CompletableFuture<String> getCustomerByUnique(
            @RequestParam String ssn,
            @RequestParam String routingNo,
            @RequestParam String accountNo
    ) {
        return customerFuture.getCustomerByUnique(ssn, routingNo, accountNo);
    }

    @GetMapping("/load-features")
    public CompletableFuture<String> loadFeatures(
            @RequestParam Long customerId,
            @RequestParam Long portfolioId,
            @RequestParam List<String> features
    ) {
        return customerFuture.loadFeatures(customerId, portfolioId, features);
    }

    @PutMapping("/update-features")
    public CompletableFuture<String> updateFeatures(
            @RequestBody JSONObject dataJson
    ) {
        return customerFuture.updateFeatures(dataJson);
    }

    @PutMapping("/update")
    public CompletableFuture<String> updateCustomer(
            @RequestBody JSONObject dataJson
    ) {
        return customerFuture.updateCustomer(dataJson);
    }

    @GetMapping("/unsubscribe/{openId}/{type}/{value}")
    public CompletableFuture<String> unsubscribeCustomer(
            @PathVariable String openId,
            @PathVariable Integer type,
            @PathVariable Integer value
    ) {
        return customerFuture.unsubscribeCustomer(openId, type, value);
    }

    @PostMapping("/create/opt-in")
    public CompletableFuture<String> createCustomerOptIn(
            @RequestBody List<CustomerOptInData> optInDataList
    ) {
        return CompletableFuture.supplyAsync(() -> customerFuture.createCustomerOptIn(optInDataList));
    }
}
