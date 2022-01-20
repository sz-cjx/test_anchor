package com.arbfintech.microservice.customer.restapi.future;

import com.arbfintech.microservice.customer.restapi.service.BusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
public class CustomerOperationFuture {

    @Autowired
    private BusinessService businessService;

    public CompletableFuture<String> calculateLoanAmount (Long customerId) {
        return CompletableFuture.supplyAsync(
                () -> {
                    return businessService.calculateLoanAmount(customerId);
                }
        );
    }

}
