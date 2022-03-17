package com.sztus.dalaran.microservice.customer.server.controller;

import com.sztus.framework.component.core.type.AjaxResult;
import com.sztus.framework.component.core.type.ProcedureException;
import com.sztus.dalaran.microservice.customer.client.object.type.CustomerAction;
import com.sztus.dalaran.microservice.customer.client.object.parameter.request.GetCustomerRequest;
import com.sztus.dalaran.microservice.customer.client.object.parameter.request.SaveCustomerRequest;
import com.sztus.dalaran.microservice.customer.client.object.parameter.response.GetCustomerResponse;
import com.sztus.dalaran.microservice.customer.client.object.parameter.response.SaveCustomerResponse;
import com.sztus.dalaran.microservice.customer.server.converter.CustomerConverter;
import com.sztus.dalaran.microservice.customer.server.domain.Customer;
import com.sztus.dalaran.microservice.customer.server.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
public class CustomerController {

    @GetMapping(CustomerAction.GET_CUSTOMER)
    public String getCustomer(
            GetCustomerRequest request
    ) {
        try {
            String email = request.getEmail();
            String openId = request.getOpenId();

            Customer customer = customerService.getCustomer(email, openId);

            GetCustomerResponse response = CustomerConverter.INSTANCE.CustomerToGetCustomerResponse(customer);

            return AjaxResult.success(response);
        } catch (ProcedureException e) {
            return AjaxResult.failure(e);
        }
    }

    @PostMapping(CustomerAction.SAVE_CUSTOMER)
    public String saveCustomer(
            @RequestBody SaveCustomerRequest request
    ) {
        try {
            Customer customer = CustomerConverter.INSTANCE.CustomerViewToCustomer(request);
            Customer customerInDb = customerService.saveCustomer(customer);

            SaveCustomerResponse response = CustomerConverter.INSTANCE.CustomerToSaveCustomerResponse(customerInDb);
            return AjaxResult.success(response);
        } catch (ProcedureException e) {
            return AjaxResult.failure(e);
        }
    }

    @Autowired
    private CustomerService customerService;
}
