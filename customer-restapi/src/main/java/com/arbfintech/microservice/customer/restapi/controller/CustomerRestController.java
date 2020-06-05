package com.arbfintech.microservice.customer.restapi.controller;

import com.arbfintech.framework.component.database.core.GeneralFuture;
import com.arbfintech.microservice.customer.domain.entity.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

/**
 * @author Baron
 * 2020/06/05
 */
@RestController
@RequestMapping("/customer")
public class CustomerRestController {

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

    @GetMapping("/customer/single")
    public CompletableFuture<String> findCustomerByOptions(
            @RequestParam("options") String optionStr
    ) {
        return generalFuture.findByOptions(Customer.class, optionStr);
    }

    @Autowired
    private GeneralFuture generalFuture;

}
