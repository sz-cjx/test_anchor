package com.arbfintech.microservice.customer.restapi.controller;

import com.arbfintech.microservice.customer.object.dto.CustomerAccountDTO;
import com.arbfintech.microservice.customer.restapi.future.CustomerAcountFuture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
public class CustomerAcountController {

    @Autowired
    private CustomerAcountFuture customerAcountFuture;

    @GetMapping("/account")
    public CompletableFuture<String> getAccountInfo(
            @RequestParam Long accountId
    ) {
        return customerAcountFuture.getAccountInfo(accountId);
    }

    @PostMapping("/account")
    public CompletableFuture<String> saveAccountInfo(
            @RequestBody CustomerAccountDTO customerAccountDTO
    ) {
        return customerAcountFuture.saveAccountInfo(customerAccountDTO);
    }
}
