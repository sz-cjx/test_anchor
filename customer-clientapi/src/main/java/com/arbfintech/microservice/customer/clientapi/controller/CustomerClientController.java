package com.arbfintech.microservice.customer.clientapi.controller;

import com.arbfintech.microservice.customer.clientapi.future.CustomerClientFuture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/customer")
public class CustomerClientController {

    @Autowired
    private CustomerClientFuture customerClientFuture;

    @PostMapping("/replace-customer")
    public CompletableFuture<Long> replaceCustomer(
            @RequestBody String dataStr
    ) {
        return customerClientFuture.replaceCustomer(dataStr);
    }

    @GetMapping("/get-customer/{id}")
    public CompletableFuture<String> getCustomerById(
            @PathVariable("id") Long id
    ) {
        return customerClientFuture.getCustomerById(id);
    }

    @GetMapping("/list-customer-ssn-email-num")
    public CompletableFuture<String> listCustomerBySsnOrEmailOrNo(
            @RequestParam("options") String optionStr
    ) {
        return customerClientFuture.listCustomerBySsnOrEmailOrNo(optionStr);
    }

    @GetMapping("/get-customer")
    public CompletableFuture<String> findCustomerByOptions(
            @RequestParam("options") String optionStr
    ) {
        return customerClientFuture.findCustomerByCondition(optionStr);
    }
}