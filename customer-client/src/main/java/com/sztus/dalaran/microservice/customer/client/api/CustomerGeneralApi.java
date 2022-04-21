package com.sztus.dalaran.microservice.customer.client.api;

import com.sztus.dalaran.microservice.customer.client.object.constant.CustomerAction;
import com.sztus.dalaran.microservice.customer.client.object.parameter.request.GetCustomerPersonalDataRequest;
import com.sztus.dalaran.microservice.customer.client.object.parameter.request.GetCustomerRequest;
import com.sztus.dalaran.microservice.customer.client.object.parameter.request.SaveCustomerPersonalDataRequest;
import com.sztus.dalaran.microservice.customer.client.object.parameter.response.GetCustomerPersonalDataResponse;
import com.sztus.dalaran.microservice.customer.client.object.parameter.response.GetCustomerResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "microservice-customer", path = "/v4", contextId = "customer-api")
public interface CustomerGeneralApi {

    @GetMapping(CustomerAction.GET_CUSTOMER)
    GetCustomerResponse getCustomer(
            @SpringQueryMap GetCustomerRequest request
    );

    @GetMapping(CustomerAction.GET_CUSTOMER_PERSONAL_DATA)
    GetCustomerPersonalDataResponse getCustomerPersonalData(
            GetCustomerPersonalDataRequest request
    );

    @PostMapping(CustomerAction.SAVE_CUSTOMER_PERSONAL_DATA)
    Long saveCustomerPersonalData(
            @RequestBody SaveCustomerPersonalDataRequest request
    );
}
