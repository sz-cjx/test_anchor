package com.sztus.dalaran.microservice.customer.client.api;

import com.sztus.dalaran.microservice.customer.client.object.parameter.request.CreateCustomerRequest;
import com.sztus.dalaran.microservice.customer.client.object.parameter.request.GetCustomerByUniqueRequest;
import com.sztus.dalaran.microservice.customer.client.object.parameter.request.GetCustomerRequest;
import com.sztus.dalaran.microservice.customer.client.object.parameter.request.SaveCustomerRequest;
import com.sztus.dalaran.microservice.customer.client.object.parameter.response.CreateCustomerResponse;
import com.sztus.dalaran.microservice.customer.client.object.parameter.response.GetCustomerByUniqueResponse;
import com.sztus.dalaran.microservice.customer.client.object.parameter.response.GetCustomerResponse;
import com.sztus.dalaran.microservice.customer.client.object.parameter.response.SaveCustomerResponse;
import com.sztus.dalaran.microservice.customer.client.object.type.CustomerAction;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "microservice-customer", path = "/v4", contextId = "customer-api")
public interface CustomerApi {

    @GetMapping(CustomerAction.GET_CUSTOMER)
    GetCustomerResponse getCustomer(
            @SpringQueryMap GetCustomerRequest request
    );

    @PostMapping(CustomerAction.SAVE_CUSTOMER)
    SaveCustomerResponse saveCustomer(
            @RequestBody SaveCustomerRequest request
    );

    @GetMapping(CustomerAction.GET_CUSTOMER_BY_UNIQUE)
    GetCustomerByUniqueResponse getCustomerByUnique(
            @SpringQueryMap GetCustomerByUniqueRequest request
    );

    @PostMapping(CustomerAction.CREATE_CUSTOMER)
    CreateCustomerResponse createCustomer(
            @RequestBody CreateCustomerRequest request
    );
}
