package com.arbfintech.microservice.customer.restapi.controller;

import com.arbfintech.microservice.customer.object.dto.ContactVerifyDTO;
import com.arbfintech.microservice.customer.object.dto.PaymentScheduleDTO;
import com.arbfintech.microservice.customer.restapi.future.CustomerOperationFuture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
public class CustomerOperationConteroller {

    @Autowired
    private CustomerOperationFuture operationFuture;

    @GetMapping("/loan-amount/calculate")
    public CompletableFuture<String> calculateLoanAmount (
            @RequestParam Long customerId
    ) {
        return operationFuture.calculateLoanAmount(customerId);
    }

    @PostMapping("/payment-schedule/pre-calculate")
    public CompletableFuture<String> preCalculateInstallment(
            @RequestBody PaymentScheduleDTO paymentScheduleDTO
    ) {
        return operationFuture.preCalculateInstallment(paymentScheduleDTO);
    }

    @PostMapping("/contact/verify")
    public CompletableFuture<String> verifyContactInformation(
            @RequestBody ContactVerifyDTO contactVerifyDTO
    ){
        return operationFuture.verifyContactInformation(contactVerifyDTO);
    }
}
