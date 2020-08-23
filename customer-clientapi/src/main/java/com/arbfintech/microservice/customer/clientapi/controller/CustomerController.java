package com.arbfintech.microservice.customer.clientapi.controller;

import com.arbfintech.framework.component.database.core.SimpleFuture;
import com.arbfintech.microservice.customer.domain.entity.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    private SimpleFuture simpleFuture;

    private static final List<String> CUSTOMER_CONDITION_KEY_SET = Arrays.asList("ssn", "email");

    @PostMapping("/add")
    public CompletableFuture<String> addCustomer(
            @RequestBody String dataStr
    ) {
        return simpleFuture.save(Customer.class, dataStr);
    }

    @PostMapping("/update")
    public CompletableFuture<String> updateCustomer(
            @RequestBody String dataStr
    ) {
        return simpleFuture.save(Customer.class, dataStr);
    }

    @GetMapping("/{id}")
    public CompletableFuture<String> getCustomerById(
            @PathVariable("id") Long id
    ) {
        return simpleFuture.findById(Customer.class, id);
    }

    @GetMapping("/get")
    public CompletableFuture<String> getCustomerByConditions(
            @RequestParam(name = "ssn", required = false) String ssn,
            @RequestParam(name = "email", required = false) String email
    ) {
        return simpleFuture.findByConditions(Customer.class, CUSTOMER_CONDITION_KEY_SET, ssn, email);
    }

    @GetMapping("/list")
    public CompletableFuture<String> listCustomerByConditions(
            @RequestParam(name = "ssn", required = false) String ssn,
            @RequestParam(name = "email", required = false) String email
    ) {
        return simpleFuture.findAllByConditions(Customer.class, CUSTOMER_CONDITION_KEY_SET, ssn, email);
    }
}