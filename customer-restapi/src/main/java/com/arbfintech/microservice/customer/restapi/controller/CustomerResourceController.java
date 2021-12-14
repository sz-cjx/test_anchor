package com.arbfintech.microservice.customer.restapi.controller;

import com.arbfintech.microservice.customer.object.dto.CustomerProfileDTO;
import com.arbfintech.microservice.customer.restapi.future.CustomerResourceFuture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@RestController
public class CustomerResourceController {

    @Autowired
    private CustomerResourceFuture customerResourceFuture;

    @PostMapping("/profile")
    public CompletableFuture<String> getProfileByFeature(
            @RequestBody CustomerProfileDTO customerProfileDTO
    ) {
        return customerResourceFuture.getProfileByFeature(customerProfileDTO);
    }
}
