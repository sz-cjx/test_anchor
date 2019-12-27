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
    String listCustomerBySSN(@RequestParam("ssn") String ssn);

    @GetMapping("/customers/latest/ssn")
    Long getTheLatestCustomerIdBySSN(@RequestParam("ssn") String ssn);

    @GetMapping("/customers/latest/email/ssn")
    Long getLatestCustomerIdByEmailOrSSN(@RequestParam("email") String email,
                                            @RequestParam("ssn") String ssn);

    @GetMapping("/customers/latest/account/routing")
    Long getLatestCustomerIdByUniqueKey(@RequestParam("unique") String unique);

    @PostMapping("/customers/verify")
    String verifyCustomerLoginData(@RequestBody String loginData);

    @PostMapping("/customers/sign-up")
    String doCustomerSignUp(@RequestBody String signUpData);

    @PostMapping("/customers/update")
    String updateCustomer(@RequestBody String customers);

//    @PostMapping("/customers/jdbc")
//    Long saveCustomerByJDBC(@RequestBody String customerStr);
}
