package com.arbfintech.microservice.customer.restapi.controller;

import com.arbfintech.microservice.customer.restapi.service.CustomerRestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

/**
 * @author CAVALIERS
 * 2019/9/23 12:36
 */
@RestController
@RequestMapping("/api")
public class CustomerRestController {

    @Autowired(required = true)
    private CustomerRestService customerRestService;

    @PostMapping("/customers")
    public CompletableFuture<Long> addCustomer(@RequestBody String customerStr) {
        return CompletableFuture.supplyAsync(
                () -> customerRestService.addCustomer(customerStr)
        );
    }

    @GetMapping("/customers/{id}")
    public CompletableFuture<String> getCustomerById(@PathVariable("id") Long id) {
        return CompletableFuture.supplyAsync(
                () -> customerRestService.getCustomerById(id)
        );
    }

    @PutMapping("/customers/{id}")
    public CompletableFuture<Integer> setCustomerById(@PathVariable("id") Long id,
                                                      @RequestBody String customerStr) {
        return CompletableFuture.supplyAsync(
                () -> customerRestService.setCustomerById(id, customerStr)
        );
    }

    @PostMapping("/customer/query")
    public CompletableFuture<String> listCustomerByConditions(@RequestParam("conditionStr") String conditionStr,
                                    @RequestParam("conditionType") String conditionType) {
        return CompletableFuture.supplyAsync(
                () -> customerRestService.listCustomerByConditions(conditionStr, conditionType)
        );
    }

    @GetMapping("/customers/ssn")
    public CompletableFuture<String> listCustomerBySSN(@RequestParam("ssn")Long ssn){
        return CompletableFuture.supplyAsync(
                () -> customerRestService.listCustomerBySSN(ssn)
        );
    }


    @PostMapping("/customers/jdbc")
    public CompletableFuture<Long> saveCustomerByJDBC(@RequestBody String customerStr){
        return CompletableFuture.supplyAsync(
                () -> customerRestService.saveCustomerByJDBC(customerStr)
        );
    }
}
