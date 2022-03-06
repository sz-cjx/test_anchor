package com.sztus.microservice.customer.server.controller;

import com.sztus.framework.component.core.type.AjaxResult;
import com.sztus.framework.component.core.type.ProcedureException;
import com.sztus.microservice.customer.client.object.business.CustomerAction;
import com.sztus.microservice.customer.client.object.parameter.request.GetCustomerRequest;
import com.sztus.microservice.customer.client.object.parameter.response.GetCustomerResponse;
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

    @GetMapping(CustomerAction.GET_CUSTOMER)
    public String getCustomer(
            GetCustomerRequest request
    ) {
        try {
            String email = request.getEmail();

            Customer customer = customerService.getCustomer(email);

            GetCustomerResponse response = CustomerConverter.INSTANCE.CustomerToGetCustomerResponse(customer);

            return AjaxResult.success(response);
        } catch (ProcedureException e) {
            return AjaxResult.failure(e);
        }
    }

    @Autowired
    private CustomerService customerService;
}
