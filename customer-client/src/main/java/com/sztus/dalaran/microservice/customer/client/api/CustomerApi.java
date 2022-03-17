package com.sztus.dalaran.microservice.customer.client.api;

import com.sztus.dalaran.microservice.customer.client.object.parameter.request.GetCustomerRequest;
import com.sztus.dalaran.microservice.customer.client.object.parameter.request.SaveCustomerRequest;
import com.sztus.dalaran.microservice.customer.client.object.type.CustomerAction;
import com.sztus.framework.component.core.type.AjaxResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(value = "microservice-oauth", path = "/v4", contextId = "oauth-api")
public interface CustomerApi {

    @GetMapping(CustomerAction.GET_CUSTOMER)
    AjaxResult getCustomer(
            @SpringQueryMap GetCustomerRequest request
    );

    @PostMapping(CustomerAction.SAVE_CUSTOMER)
    AjaxResult saveCustomer(
            @SpringQueryMap SaveCustomerRequest request
    );
}
