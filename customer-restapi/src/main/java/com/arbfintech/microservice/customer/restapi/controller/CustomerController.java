package com.arbfintech.microservice.customer.restapi.controller;

import com.arbfintech.microservice.customer.object.entity.Customer;
import com.arbfintech.microservice.customer.restapi.future.CustomerFuture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

/**
 * @author Fly_Roushan
 * @date 2020/12/17
 */
@RestController
public class CustomerController {

    @Autowired
    private CustomerFuture customerFuture;

    @PostMapping("/operation/create-customer")
    public CompletableFuture<String> createCustomer(
            @RequestBody Customer customer
    ) {
        return customerFuture.createCustomer(customer);
    }

    @GetMapping("/operation/search-customer")
    public CompletableFuture<String> searchCustomer(
            @RequestParam(required = false) String email
    ) {
        return customerFuture.searchCustomer(email);
    }
}
