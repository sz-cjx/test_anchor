package com.arbfintech.microservice.customer.restapi.future;

import com.arbfintech.framework.component.core.type.AjaxResult;
import com.arbfintech.framework.component.core.type.ProcedureException;
import com.arbfintech.microservice.customer.object.dto.CalculationProcessDTO;
import com.arbfintech.microservice.customer.object.dto.ContactVerifyDTO;
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
                        CalculationProcessDTO calculationProcessDTO = businessService.checkCalculationParam(customerId);
                        return businessService.calculateLoanAmount(customerId, calculationProcessDTO);
                    } catch (ProcedureException e) {
                        return AjaxResult.failure(e);
                    }
                }
        );
    }

    public CompletableFuture<String> verifyContactInformation (ContactVerifyDTO contactVerifyDTO) {
        return CompletableFuture.supplyAsync(
                () -> businessService.verifyContactInformation(contactVerifyDTO)
        );
    }

}
