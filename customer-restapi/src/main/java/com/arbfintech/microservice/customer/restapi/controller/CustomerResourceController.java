package com.arbfintech.microservice.customer.restapi.controller;

import com.arbfintech.microservice.customer.object.dto.CustomerOptInDTO;
import com.arbfintech.microservice.customer.object.dto.CustomerProfileDTO;
import com.arbfintech.microservice.customer.restapi.future.CustomerResourceFuture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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

    @PutMapping("/profile")
    public CompletableFuture<String> saveProfileByFeature(
            @RequestBody CustomerProfileDTO customerProfileDTO,
            HttpServletRequest request
    ) {
        return customerResourceFuture.saveProfileByFeature(customerProfileDTO, request);
    }

    @PostMapping("/operation-log")
    public CompletableFuture<String> getOperationLog(
            @RequestBody String dataStr
    ) {
        return customerResourceFuture.getOperationLog(dataStr);
    }

    @GetMapping("/opt-in")
    public CompletableFuture<String> getCustomerOptIn(
            @RequestParam Long customerId
    ) {
        return customerResourceFuture.getCustomerOptIn(customerId);
    }

    @PostMapping("/opt-in")
    public CompletableFuture<String> saveCustomerOptIn(
            @RequestBody CustomerOptInDTO customerOptInDTO,
            HttpServletRequest request
    ) {
        return customerResourceFuture.saveCustomerOptIn(customerOptInDTO);
    }
}
