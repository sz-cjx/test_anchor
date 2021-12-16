package com.arbfintech.microservice.customer.restapi.future;

import com.arbfintech.framework.component.core.type.AjaxResult;
import com.arbfintech.framework.component.core.type.ProcedureException;
import com.arbfintech.microservice.customer.object.dto.CustomerAccountDTO;
import com.arbfintech.microservice.customer.restapi.service.CustomerAcountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
public class CustomerAcountFuture {

    @Autowired
    private CustomerAcountService customerAcountService;

    public CompletableFuture<String> getAccountInfo(Long accountId) {
        return CompletableFuture.supplyAsync(
                () -> customerAcountService.getAccountInfo(accountId)
        );
    }

    public CompletableFuture<String> saveAccountInfo(CustomerAccountDTO customerAccountDTO) {
        return CompletableFuture.supplyAsync(
                () -> {
                    try {
                        return customerAcountService.saveAccountInfo(customerAccountDTO);
                    } catch (ProcedureException e) {
                        return AjaxResult.failure(e);
                    }
                }
        );
    }
}
