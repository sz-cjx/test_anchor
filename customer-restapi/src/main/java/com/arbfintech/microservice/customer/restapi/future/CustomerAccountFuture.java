package com.arbfintech.microservice.customer.restapi.future;

import com.arbfintech.framework.component.core.type.AjaxResult;
import com.arbfintech.framework.component.core.type.ProcedureException;
import com.arbfintech.microservice.customer.object.dto.ActivateAccountDTO;
import com.arbfintech.microservice.customer.object.dto.CustomerAccountDTO;
import com.arbfintech.microservice.customer.object.dto.CustomerAccountPasswordDTO;
import com.arbfintech.microservice.customer.restapi.service.CustomerAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
public class CustomerAccountFuture {

    @Autowired
    private CustomerAccountService customerAccountService;

    public CompletableFuture<String> getAccountInfo(Long id) {
        return CompletableFuture.supplyAsync(
                () -> customerAccountService.getAccountInfo(id)
        );
    }

    public CompletableFuture<String> saveAccountInfo(CustomerAccountDTO customerAccountDTO) {
        return CompletableFuture.supplyAsync(
                () -> {
                    try {
                        return customerAccountService.saveAccountInfo(customerAccountDTO);
                    } catch (ProcedureException e) {
                        return AjaxResult.failure(e);
                    }
                }
        );
    }

    public CompletableFuture<String> changePassword(CustomerAccountPasswordDTO customerAccountPasswordDTO) {
        return CompletableFuture.supplyAsync(
                () -> {
                    try {
                        return customerAccountService.changePassword(customerAccountPasswordDTO);
                    } catch (ProcedureException e) {
                        return AjaxResult.failure(e);
                    }
                }
        );
    }

    public CompletableFuture<String> forgotPassword(CustomerAccountPasswordDTO customerAccountPasswordDTO) {
        return CompletableFuture.supplyAsync(
                () -> {
                    try {
                        return customerAccountService.forgotPassword(customerAccountPasswordDTO);
                    } catch (ProcedureException e) {
                        return AjaxResult.failure(e);
                    }
                }
        );
    }

    public CompletableFuture<String> activateAccount(ActivateAccountDTO activateAccountDTO){
        return CompletableFuture.supplyAsync(
                () -> {
                    try {
                        return customerAccountService.activateAccount(activateAccountDTO);
                    } catch (ProcedureException e) {
                        return AjaxResult.failure(e);
                    }
                }
        );
    }

    public CompletableFuture<String> signIn(ActivateAccountDTO activateAccountDTO){
        return CompletableFuture.supplyAsync(
                () -> {
                    try {
                        return customerAccountService.signIn(activateAccountDTO);
                    } catch (ProcedureException e) {
                        return AjaxResult.failure(e);
                    }
                }
        );
    }
}
