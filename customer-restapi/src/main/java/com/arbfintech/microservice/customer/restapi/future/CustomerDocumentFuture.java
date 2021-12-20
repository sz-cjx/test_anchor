package com.arbfintech.microservice.customer.restapi.future;

import com.arbfintech.framework.component.core.type.AjaxResult;
import com.arbfintech.framework.component.core.type.ProcedureException;
import com.arbfintech.microservice.customer.object.dto.CustomerDocumentDTO;
import com.arbfintech.microservice.customer.restapi.service.CustomerDocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
public class CustomerDocumentFuture {

    @Autowired
    private CustomerDocumentService customerDocumentService;

    public CompletableFuture<String> listDocument(CustomerDocumentDTO customerDocumentDTO) {
        return CompletableFuture.supplyAsync(
                () -> {
                    try {
                        return customerDocumentService.listDocument(customerDocumentDTO);
                    } catch (ProcedureException e) {
                        return AjaxResult.failure(e);
                    }
                }
        );
    }
}
