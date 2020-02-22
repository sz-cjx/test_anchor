package com.arbfintech.microservice.customer.restapi.controller;

import com.arbfintech.microservice.customer.domain.entity.Customer;
import com.arbfintech.microservice.customer.restapi.future.GeneralFuture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

/**
 * @author CAVALIERS
 * 2019/9/23 12:36
 */
@RestController
@RequestMapping("/customer")
public class CustomerRestController {

    @Autowired
    private GeneralFuture generalFuture;

    @PostMapping("/customers")
    public CompletableFuture<Long> replaceCustomer(
            @RequestBody String dataStr
    ) {
        return generalFuture.save(Customer.class, dataStr);
    }

    @GetMapping("/customers/{id}")
    public CompletableFuture<String> getCustomerById(
            @PathVariable("id") Long id
    ) {
        return generalFuture.findById(Customer.class, id, null);
    }

    @GetMapping("/customers")
    public CompletableFuture<String> listCustomerByOptions(
            @RequestParam("options") String optionStr
    ) {
        return generalFuture.findAllByOptions(Customer.class, optionStr);
    }

}
