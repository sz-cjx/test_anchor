package com.arbfintech.microservice.customer.restapi.future;

import com.arbfintech.framework.component.core.type.AjaxResult;
import com.arbfintech.framework.component.core.type.ProcedureException;
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
                    try {
                        return businessService.calculateLoanAmount(customerId);
                    } catch (ProcedureException e) {
                        return AjaxResult.failure(e);
                    }
                }
        );
    }

}
