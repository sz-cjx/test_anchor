package com.sztus.dalaran.microservice.customer.client.api;

import com.sztus.dalaran.microservice.customer.client.object.parameter.request.GetCustomerRequest;
import com.sztus.dalaran.microservice.customer.client.object.parameter.request.SaveCustomerRequest;
import com.sztus.dalaran.microservice.customer.client.object.parameter.response.GetCustomerResponse;
import com.sztus.dalaran.microservice.customer.client.object.parameter.response.SaveCustomerResponse;
import com.sztus.dalaran.microservice.customer.client.object.type.CustomerAction;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "microservice-customer", path = "/v4", contextId = "customer-api")
public interface CustomerApi {

    @PostMapping(CustomerAction.GET_CUSTOMER)
    GetCustomerResponse getCustomer(
            @RequestBody GetCustomerRequest request
    );

    @PostMapping(CustomerAction.SAVE_CUSTOMER)
    SaveCustomerResponse saveCustomer(
            @RequestBody SaveCustomerRequest request
    );
}
