package com.arbfintech.microservice.customer.clientapi.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@FeignClient("customer-restapi")
@RequestMapping("/customer")
public interface CustomerFeignClient {

    @PostMapping("/customers")
    Long replaceCustomer(
            @RequestBody String dataStr
    );

    @GetMapping("/customers/{id}")
    String getCustomerById(
            @PathVariable("id") Long id
    );

    @GetMapping("/customers")
    String listCustomerByOptions(
            @RequestParam("options") String optionStr
    );

    @GetMapping("/customer/single")
    String findCustomerByOptions(
            @RequestParam("options") String optionStr
    );
}
