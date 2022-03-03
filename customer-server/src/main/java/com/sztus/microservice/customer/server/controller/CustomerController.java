package com.sztus.microservice.customer.server.controller;

import com.sztus.framework.component.core.type.AjaxResult;
import com.sztus.microservice.customer.client.constant.CustomerAction;
import com.sztus.microservice.customer.client.object.parameter.request.GetCustomerByConditionsRequest;
import com.sztus.microservice.customer.client.object.parameter.response.GetCustomerByConditionsResponse;
import com.sztus.microservice.customer.server.converter.CustomerMapper;
import com.sztus.microservice.customer.server.domain.CustomerAccountData;
import com.sztus.microservice.customer.server.domain.CustomerProfile;
import com.sztus.microservice.customer.server.service.CustomerAccountService;
import com.sztus.microservice.customer.server.service.CustomerProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@RequestMapping
public class CustomerController {

    @GetMapping(CustomerAction.GET_CUSTOMER_BY_CONDITIONS)
    public String getCustomerByConditions(
            GetCustomerByConditionsRequest request
    ) {
        String email = request.getEmail();

        CustomerProfile customerProfile = customerProfileService.getCustomerProfileByConditions(email);

        GetCustomerByConditionsResponse response = null;
        if (Objects.nonNull(customerProfile)) {
            response = CustomerMapper.INSTANCE.convertCustomerProfileToResponse(customerProfile);
        }

        return AjaxResult.success(response);
    }

    @Autowired
    private CustomerProfileService customerProfileService;
}
