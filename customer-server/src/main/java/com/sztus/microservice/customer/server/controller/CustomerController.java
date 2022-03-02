package com.sztus.microservice.customer.server.controller;

import com.sztus.framework.component.core.type.AjaxResult;
import com.sztus.microservice.customer.client.constant.CustomerAction;
import com.sztus.microservice.customer.client.object.parameter.request.GetCustomerByConditionsRequest;
import com.sztus.microservice.customer.client.object.parameter.response.GetCustomerByConditionsResponse;
import com.sztus.microservice.customer.server.converter.CustomerMapper;
import com.sztus.microservice.customer.server.domain.CustomerAccountData;
import com.sztus.microservice.customer.server.service.CustomerAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class CustomerController {

    @GetMapping(CustomerAction.GET_CUSTOMER_BY_CONDITIONS)
    public String getCustomerByConditions(
            GetCustomerByConditionsRequest request
    ) {
        String email = request.getEmail();

        CustomerAccountData customerAccountData = customerAccountService.getCustomerByConditions(email);

        GetCustomerByConditionsResponse response = (GetCustomerByConditionsResponse) CustomerMapper.INSTANCE.convertCustomerToView(customerAccountData);

        return AjaxResult.success(response);
    }

    @Autowired
    private CustomerAccountService customerAccountService;
}
