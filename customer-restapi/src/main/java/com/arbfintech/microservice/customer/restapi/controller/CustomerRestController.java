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
    public CompletableFuture<String> listCustomerBySSN(@RequestParam("ssn") String ssn){
        return CompletableFuture.supplyAsync(
                () -> customerRestService.listCustomerBySSN(ssn)
        );
    }

    @GetMapping("/customers/latest")
    public CompletableFuture<Long> getLatestCustomerId(@RequestParam("ssn") String ssn,
                                                          @RequestParam("email") String email,
                                                          @RequestParam("bankUniqueKey") String bankUniqueKey){
        return CompletableFuture.supplyAsync(
                () -> customerRestService.getLatestCustomerId(ssn, email, bankUniqueKey)
        );
    }

    @GetMapping("/customers/latest/ssn")
    public CompletableFuture<Long> getTheLatestCustomerIdBySSN(@RequestParam("ssn") String ssn){
        return CompletableFuture.supplyAsync(
                () -> customerRestService.getTheLatestCustomerIdBySSN(ssn)
        );
    }

    @GetMapping("/customers/latest/account/routing")
    public CompletableFuture<Long> getLatestCustomerIdByUniqueKey(@RequestParam("unique") String uniqueKey){
        return CompletableFuture.supplyAsync(
                () -> customerRestService.getLatestCustomerIdByUniqueKey(uniqueKey)
        );
    }

    @GetMapping("/customers/latest/email/ssn")
    public CompletableFuture<Long> getLatestCustomerIdByEmailOrSSN(@RequestParam("email") String email,
                                                                   @RequestParam("ssn") String ssn){
        return CompletableFuture.supplyAsync(
                () -> customerRestService.getLatestCustomerIdByEmailOrSsn(email, ssn)
        );
    }

    @PostMapping("/customers/verify")
    public CompletableFuture<String> verifyCustomerLoginData(@RequestBody String loginData) {
        return CompletableFuture.supplyAsync(() -> customerRestService.verifyCustomerLoginData(loginData));
    }

    @PostMapping("/customers/sign-up")
    public CompletableFuture<String> doCustomerSignUp(@RequestBody String signUpData) {
        return CompletableFuture.supplyAsync(() -> customerRestService.doCustomerSignUp(signUpData));
    }

    @PostMapping("/customers/update")
    public CompletableFuture<String> doCustomerUpdate(@RequestBody String customers) {
        return CompletableFuture.supplyAsync(() -> customerRestService.doCustomerUpdate(customers));
    }

//    @PostMapping("/customers/jdbc")
//    public CompletableFuture<Long> saveCustomerByJDBC(@RequestBody String customerStr){
//        return CompletableFuture.supplyAsync(
//                () -> customerRestService.saveCustomerByJDBC(customerStr)
//        );
//    }
}
