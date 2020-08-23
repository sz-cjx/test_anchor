package com.arbfintech.microservice.customer.clientapi.controller;

import com.arbfintech.framework.component.core.constant.ConditionTypeConst;
import com.arbfintech.framework.component.core.type.ProcedureException;
import com.arbfintech.framework.component.core.type.SqlOption;
import com.arbfintech.framework.component.core.util.StringUtil;
import com.arbfintech.framework.component.database.core.SimpleFuture;
import com.arbfintech.microservice.customer.clientapi.future.CustomerFuture;
import com.arbfintech.microservice.customer.domain.entity.Customer;
import com.arbfintech.microservice.customer.object.enumerate.CustomerErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    private CustomerFuture customerFuture;

    @Autowired
    private SimpleFuture simpleFuture;

    @PostMapping("/add")
    public CompletableFuture<String> addCustomer(
            @RequestBody String dataStr
    ) {
        return simpleFuture.save(Customer.class, dataStr);
    }

    @PostMapping("/update")
    public CompletableFuture<String> updateCustomer(
            @RequestBody String dataStr
    ) {
        return simpleFuture.save(Customer.class, dataStr);
    }

    @GetMapping("/{id}")
    public CompletableFuture<String> getCustomerById(
            @PathVariable("id") Long id
    ) {
        return simpleFuture.findById(Customer.class, id);
    }

    @GetMapping("/get")
    public CompletableFuture<String> getCustomerByConditions(
            @RequestParam(name = "ssn", required = false) String ssn,
            @RequestParam(name = "email", required = false) String email
    ) {
        return customerFuture.getCustomerByConditions(ssn, email);
    }

    @GetMapping("/list")
    public CompletableFuture<String> listCustomerByConditions(
            @RequestParam(name = "ssn", required = false) String ssn,
            @RequestParam(name = "email", required = false) String email
    ) {
        return customerFuture.listCustomerByConditions(ssn, email);
    }
}