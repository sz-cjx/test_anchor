package com.arbfintech.microservice.customer.clientapi.controller;

import com.arbfintech.framework.component.core.type.KeyValuePair;
import com.arbfintech.framework.component.database.core.SimpleFuture;
import com.arbfintech.microservice.customer.clientapi.future.CustomerFuture;
import com.arbfintech.microservice.customer.domain.entity.Customer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/customer")
public class CustomerController {

    private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);

    @Autowired
    private SimpleFuture simpleFuture;

    @Autowired
    private CustomerFuture customerFuture;

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
            @RequestParam(name = "id", required = false) Long id,
            @RequestParam(name = "ssn", required = false) String ssn,
            @RequestParam(name = "email", required = false) String email,
            @RequestParam(name = "openId", required = false) String openId
    ) {
        return simpleFuture.findByConditions(
                Customer.class,
                new KeyValuePair("id", id),
                new KeyValuePair("ssn", ssn),
                new KeyValuePair("email", email),
                new KeyValuePair("openId", openId)
        );
    }

    @GetMapping("/list")
    public CompletableFuture<String> listCustomerByConditions(
            @RequestParam(name = "ssn", required = false) String ssn,
            @RequestParam(name = "email", required = false) String email
    ) {
        return simpleFuture.findAllByConditions(
                Customer.class,
                new KeyValuePair("ssn", ssn),
                new KeyValuePair("email", email)
        );
    }

    @PostMapping("/get-latest")
    public CompletableFuture<String> getLatestCustomer(@RequestBody String dataStr) {
        return CompletableFuture.supplyAsync(() -> customerFuture.getLatestCustomer(dataStr));
    }
}