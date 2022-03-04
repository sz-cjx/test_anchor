package com.sztus.microservice.customer.server.controller;

import com.sztus.framework.component.core.type.AjaxResult;
import com.sztus.framework.component.core.type.ProcedureException;
import com.sztus.framework.component.core.type.ResponseResult;
import com.sztus.microservice.customer.client.constant.CustomerAction;
import com.sztus.microservice.customer.client.object.parameter.request.GetCustomerAccountByConditionsRequest;
import com.sztus.microservice.customer.client.object.parameter.request.GetCustomerByConditionsRequest;
import com.sztus.microservice.customer.client.object.parameter.response.GetCustomerAccountByConditionsResponse;
import com.sztus.microservice.customer.client.object.parameter.response.GetCustomerByConditionsResponse;
import com.sztus.microservice.customer.server.converter.CustomerConverter;
import com.sztus.microservice.customer.server.domain.CustomerAccountData;
import com.sztus.microservice.customer.server.domain.CustomerProfile;
import com.sztus.microservice.customer.server.service.CustomerAccountService;
import com.sztus.microservice.customer.server.service.CustomerProfileService;
import com.sztus.microservice.customer.server.util.AESCryptoUtil;
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

        try {
            String email = request.getEmail();
            CustomerProfile customerProfile = customerProfileService.getCustomerProfileByConditions(AESCryptoUtil.AESEncrypt(email));
            return AjaxResult.success(CustomerConverter.INSTANCE.convertCustomerProfileToResponse(customerProfile));
        } catch (ProcedureException e) {
            return ResponseResult.failure(e);
        }
    }

    @GetMapping(CustomerAction.GET_CUSTOMER_ACCOUNT_BY_CONDITIONS)
    public String getCustomerAccountByConditions(
            GetCustomerAccountByConditionsRequest request
    ) {
        Long customerId = request.getId();

        CustomerAccountData customerAccountData = customerAccountService.getCustomerAccountByConditions(null, customerId);

        GetCustomerAccountByConditionsResponse response = null;
        if (Objects.nonNull(customerAccountData)) {
            response = CustomerConverter.INSTANCE.convertCustomerAccountToResponse(customerAccountData);
        }

        return AjaxResult.success(response);
    }

    @Autowired
    private CustomerProfileService customerProfileService;

    @Autowired
    private CustomerAccountService customerAccountService;
}
