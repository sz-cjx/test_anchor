package com.arbfintech.microservice.customer.restapi.controller;

import com.arbfintech.microservice.customer.object.dto.CustomerAccountDTO;
import com.arbfintech.microservice.customer.restapi.future.CustomerAccountFuture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
public class CustomerAccountController {

    @Autowired
    private CustomerAccountFuture customerAcountFuture;

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
