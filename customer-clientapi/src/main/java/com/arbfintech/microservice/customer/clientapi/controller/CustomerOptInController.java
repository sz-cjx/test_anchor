package com.arbfintech.microservice.customer.clientapi.controller;

import com.arbfintech.framework.component.core.constant.ConditionTypeConst;
import com.arbfintech.framework.component.core.type.SqlOption;
import com.arbfintech.framework.component.database.core.SimpleFuture;
import com.arbfintech.microservice.customer.domain.entity.CustomerOptInData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/customer/opt-in")
public class CustomerOptInController {

    @Autowired
    private SimpleFuture simpleFuture;

    @GetMapping("/{customerId}")
    public CompletableFuture<String> listCustomerOptInData(
            @PathVariable("customerId") Long customerId
    ) {
        SqlOption sqlOption = SqlOption.getInstance();
        sqlOption.addWhere("customer_id = :customerId", ConditionTypeConst.AND, "customerId", customerId);

        return simpleFuture.findAllByOptions(CustomerOptInData.class, sqlOption.toString());
    }

    @PostMapping("/update")
    public CompletableFuture<String> updateCustomerOptInData(
            @RequestBody String dataStr
    ) {
        return simpleFuture.save(CustomerOptInData.class, dataStr);
    }
}