package com.arbfintech.microservice.customer.restapi.future;

import com.arbfintech.framework.component.core.annotation.AsyncTimed;
import com.arbfintech.microservice.customer.restapi.service.GeneralRestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
public class GeneralFuture {
    @AsyncTimed
    public <E> CompletableFuture<Long> save(Class<E> entityClass, String entityStr) {
        return CompletableFuture.supplyAsync(
                () -> generalRestService.save(entityClass, entityStr)
        );
    }

    @AsyncTimed
    public <E> CompletableFuture<String> findById(Class<E> entityClass, Long id, String optionStr) {
        return CompletableFuture.supplyAsync(
                () -> generalRestService.findById(entityClass, id, optionStr)
        );
    }

    @AsyncTimed
    public <E> CompletableFuture<String> findAllByOptions(Class<E> entityClass, String optionStr) {
        return CompletableFuture.supplyAsync(
                () -> generalRestService.findAllByOptions(entityClass, optionStr)
        );
    }

    @Autowired
    private GeneralRestService generalRestService;
}
