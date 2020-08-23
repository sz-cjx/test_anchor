package com.arbfintech.microservice.customer.clientapi.controller;

import com.arbfintech.microservice.customer.clientapi.future.CustomerClientFuture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/customer")
public class CustomerClientController {

    @Autowired
    private CustomerClientFuture customerClientFuture;

    @GetMapping("/get")
    public CompletableFuture<String> getCustomerByEmail(
            @RequestParam("email") String email
    ) {
        return CompletableFuture.supplyAsync(
                () -> customerClientFuture.getCustomerByEmail(email)
        );
    }

    @PostMapping("/replace-customer")
    public CompletableFuture<String> replaceCustomer(
            @RequestBody String dataStr
    ) {
        return CompletableFuture.supplyAsync(
                () -> customerClientFuture.replaceCustomer(dataStr)
        );
    }

    @GetMapping("/get-customer/{id}")
    public CompletableFuture<String> getCustomerById(
            @PathVariable("id") Long id
    ) {
        return CompletableFuture.supplyAsync(
                () -> customerClientFuture.getCustomerById(id)
        );
    }

    @GetMapping("/list-customer-ssn-email-num")
    public CompletableFuture<String> listCustomerBySsnOrEmailOrNo(
            @RequestParam("conditions") String conditionStr
    ) {
        return CompletableFuture.supplyAsync(
                () -> customerClientFuture.listCustomerBySsnOrEmailOrNo(conditionStr)
        );
    }

    @GetMapping("/get-customer")
    public CompletableFuture<String> getCustomerByCondition(
            @RequestParam("conditions") String conditionStr
    ) {
        return CompletableFuture.supplyAsync(
                () -> customerClientFuture.getCustomerByCondition(conditionStr)
        );
    }

    @GetMapping("/opt-in/{customerId}")
    public CompletableFuture<String> getCustomerOptInData(
            @PathVariable("customerId") Long customerId
    ) {
        return CompletableFuture.supplyAsync(
                () -> customerClientFuture.getCustomerOptInDataByCustomerId(customerId)
        );
    }

    @PostMapping("/opt-in/{customerId}")
    public CompletableFuture<String> addCustomerOptInData(
            @PathVariable("customerId") Long customerId,
            @RequestBody String dataStr
    ) {
        return CompletableFuture.supplyAsync(
                () -> customerClientFuture.addCustomerOptInData(customerId, dataStr)
        );
    }

    @PutMapping("/opt-in/{customerId}")
    public CompletableFuture<String> updateCustomerOptInData(
            @PathVariable("customerId") Long customerId,
            @RequestBody String dataStr
    ) {
        return CompletableFuture.supplyAsync(
                () -> customerClientFuture.updateCustomerOptInData(customerId, dataStr)
        );
    }
}