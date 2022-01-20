package com.arbfintech.microservice.customer.restapi.controller;

import com.arbfintech.microservice.customer.restapi.future.CustomerOperationFuture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@RestController
public class CustomerOperationConteroller {

    @Autowired
    private CustomerOperationFuture operationFuture;

    @GetMapping("/loan-amount/calculate")
    public CompletableFuture<String> calculateLoanAmount (
            @RequestParam Long customerId
    ) {
        return operationFuture.calculateLoanAmount(customerId);
    }
}
