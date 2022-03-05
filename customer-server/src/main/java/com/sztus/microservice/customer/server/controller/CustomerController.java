package com.sztus.microservice.customer.server.controller;

import com.sztus.framework.component.core.type.AjaxResult;
import com.sztus.framework.component.core.type.ProcedureException;
import com.sztus.microservice.customer.client.constant.CustomerAction;
import com.sztus.microservice.customer.client.object.parameter.request.GetCustomerByConditionsRequest;
import com.sztus.microservice.customer.server.converter.CustomerConverter;
import com.sztus.microservice.customer.server.domain.Customer;
import com.sztus.microservice.customer.server.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class CustomerController {

    @GetMapping(CustomerAction.GET_CUSTOMER_BY_CONDITIONS)
    public String getCustomerRequest(
            GetCustomerByConditionsRequest request
    ) {

        try {
            Customer customer = customerService.getCustomerByConditions(request);
            return AjaxResult.success(CustomerConverter.INSTANCE.convertCustomerToGetCustomerResponse(customer));
        } catch (ProcedureException e) {
            return AjaxResult.failure(e);
        }
    }

    @Autowired
    private CustomerService customerService;
}
