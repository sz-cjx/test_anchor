package com.arbfintech.microservice.customer.restapi.future;

import com.arbfintech.framework.component.core.annotation.AsyncTimed;
import com.arbfintech.microservice.customer.restapi.service.CustomerRestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
public class CustomerFuture {
    @AsyncTimed
    public <E> CompletableFuture<Long> save(Class<E> entityClass, String entityStr) {
        return CompletableFuture.supplyAsync(
                () -> customerRestService.save(entityClass, entityStr)
        );
    }

    @AsyncTimed
    public <E> CompletableFuture<String> findById(Class<E> entityClass, Long id, String optionStr) {
        return CompletableFuture.supplyAsync(
                () -> customerRestService.findById(entityClass, id, optionStr)
        );
    }

    @AsyncTimed
    public <E> CompletableFuture<String> findAllByOptions(Class<E> entityClass, String optionStr) {
        return CompletableFuture.supplyAsync(
                () -> customerRestService.findAllByOptions(entityClass, optionStr)
        );
    }

    @Autowired
    private CustomerRestService customerRestService;
}
