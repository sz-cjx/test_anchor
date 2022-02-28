package com.arbfintech.microservice.customer.restapi.controller;

import com.arbfintech.microservice.customer.object.dto.CustomerDocumentDTO;
import com.arbfintech.microservice.customer.restapi.future.CustomerDocumentFuture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@RestController
public class CustomerDocumentController {

    @Autowired
    private CustomerDocumentFuture customerDocumentFuture;

    @PostMapping("/document")
    public CompletableFuture<String> listDocument(
            @RequestBody CustomerDocumentDTO customerDocumentDTO
    ) {
        return customerDocumentFuture.listDocument(customerDocumentDTO);
    }
}
