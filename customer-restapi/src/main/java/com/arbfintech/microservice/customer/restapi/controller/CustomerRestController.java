package com.arbfintech.microservice.customer.restapi.controller;

import com.arbfintech.microservice.customer.domain.service.CustomerRestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class CustomerRestController {

    @Autowired(required = true)
    private CustomerRestService customerRestService;

    @PostMapping("/customers")
    Long addCustomer(@RequestBody String dataStr){
        return customerRestService.addCustomer(dataStr);
    }

    @GetMapping("/customers/{id}")
    String getCustomerById(@PathVariable("id") Long id){
        return customerRestService.getCustomerById(id);
    }

    @PutMapping("/customers/{id}")
    Integer setCustomerById(@PathVariable("id") Long id,
                            @RequestBody String customerStr){
    return customerRestService.setCustomerById(id,customerStr);
    }

    @PostMapping("/customer/query")
    String listCustomerByConditions( @RequestParam("conditionStr") String conditionStr,
                                     @RequestParam("conditionType") String conditionType){
    return customerRestService.listCustomerByConditions(conditionStr,conditionType);
    }

}
