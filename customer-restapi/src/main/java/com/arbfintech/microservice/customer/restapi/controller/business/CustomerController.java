package com.arbfintech.microservice.customer.restapi.controller.business;

import com.arbfintech.microservice.customer.restapi.future.business.CustomerFuture;
import com.arbfintech.microservice.customer.restapi.object.entity.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.concurrent.CompletableFuture;

/**
 * @author Fly_Roushan
 * @date 2020/12/17
 */
@Validated
@RestController
@RequestMapping("/business")
public class CustomerController {

    @Autowired
    private CustomerFuture customerFuture;

    @PostMapping("/operation/create-customer")
    public CompletableFuture<String> createCustomer(
            @Valid @RequestBody Customer customer
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
