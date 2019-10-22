package com.arbfintech.microservice.customer.clientapi.client;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient("customer-restapi")
@RequestMapping("/api")
public interface CustomerFeignClient {

    @PostMapping("/customers")
    Long addCustomer(@RequestBody String customerStr);

    @GetMapping("/customers/{id}")
    String getCustomerById(@PathVariable("id") Long id);

    @PutMapping("/customers/{id}")
    Integer setCustomerById(@PathVariable("id") Long id,
                            @RequestBody String customerStr);

    @PostMapping("/customer/query")
    String listCustomerByConditions( @RequestParam("conditionStr") String conditionStr,
                                     @RequestParam("conditionType") String conditionType);

    @GetMapping("/customers/ssn")
    String listCustomerBySSN(@RequestParam("ssn")Long ssn);

    @GetMapping("/customers/latest/ssn")
    Long getTheLatestCustomerIdBySSN(@RequestParam("ssn")Long ssn);

    @PostMapping("/customers/verify")
    String verifyCustomerLoginData(@RequestBody String loginData);

//    @PostMapping("/customers/jdbc")
//    Long saveCustomerByJDBC(@RequestBody String customerStr);
}
