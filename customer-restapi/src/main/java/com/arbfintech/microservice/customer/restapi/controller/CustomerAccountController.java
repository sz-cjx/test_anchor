package com.arbfintech.microservice.customer.restapi.controller;

import com.arbfintech.microservice.customer.object.dto.ActivateAccountDTO;
import com.arbfintech.microservice.customer.object.dto.CustomerAccountDTO;
import com.arbfintech.microservice.customer.object.dto.CustomerAccountPasswordDTO;
import com.arbfintech.microservice.customer.restapi.future.CustomerAccountFuture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
public class CustomerAccountController {

    @Autowired
    private CustomerAccountFuture customerAccountFuture;

    @GetMapping("/account")
    public CompletableFuture<String> getAccountInfo(
            @RequestParam Long id
    ) {
        return customerAccountFuture.getAccountInfo(id);
    }

    @PostMapping("/account")
    public CompletableFuture<String> saveAccountInfo(
            @RequestBody CustomerAccountDTO customerAccountDTO
    ) {
        return customerAccountFuture.saveAccountInfo(customerAccountDTO);
    }

    @PostMapping("/change-password")
    public CompletableFuture<String> changePassword(
            @RequestBody CustomerAccountPasswordDTO customerAccountPasswordDTO
    ) {
        return customerAccountFuture.changePassword(customerAccountPasswordDTO);
    }

    @PostMapping("/activate-account")
    public CompletableFuture<String> activateAccount(
            @RequestBody ActivateAccountDTO activateAccountDTO
    ) {
        return customerAccountFuture.activateAccount(activateAccountDTO);
    }
}
