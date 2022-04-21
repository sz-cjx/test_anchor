package com.sztus.dalaran.microservice.customer.client.api;

import com.sztus.dalaran.microservice.customer.client.object.constant.CustomerAction;
import com.sztus.dalaran.microservice.customer.client.object.parameter.request.*;
import com.sztus.dalaran.microservice.customer.client.object.parameter.response.*;
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

    @PostMapping(CustomerAction.SAVE_CUSTOMER)
    SaveCustomerResponse saveCustomer(
            @RequestBody SaveCustomerRequest request
    );

    @GetMapping(CustomerAction.GET_PERSONAL)
    GetCustomerPersonalResponse getCustomerPersonalData(
            GetCustomerPersonalDataRequest request
    );

    @PostMapping(CustomerAction.SAVE_PERSONAL)
    Long saveCustomerPersonalData(
            @RequestBody SaveCustomerPersonalRequest request
    );

    @GetMapping(CustomerAction.GET_EMPLOYMENT)
    GetCustomerEmploymentResponse getCustomerEmployment(
            @SpringQueryMap GetCustomerEmploymentRequest request
    );

    @PostMapping(CustomerAction.SAVE_EMPLOYMENT)
    SaveCustomerEmploymentResponse saveCustomerEmployment(
            @RequestBody SaveCustomerEmploymentRequest request
    );

    @GetMapping(CustomerAction.GET_PAYROLL)
    GetCustomerPayrollResponse getCustomerPayroll(
            @SpringQueryMap GetCustomerPayrollRequest request
    );

    @PostMapping(CustomerAction.SAVE_PAYROLL)
    SaveCustomerPayrollResponse saveCustomerPayroll(
            @RequestBody SaveCustomerPayrollRequest request
    );

}
