package com.sztus.dalaran.microservice.customer.server.controller;

import com.sztus.dalaran.microservice.customer.client.object.constant.CustomerAction;
import com.sztus.dalaran.microservice.customer.client.object.parameter.enumerate.CustomerErrorCode;
import com.sztus.dalaran.microservice.customer.client.object.parameter.request.GetCustomerPersonalDataRequest;
import com.sztus.dalaran.microservice.customer.client.object.parameter.request.GetCustomerRequest;
import com.sztus.dalaran.microservice.customer.client.object.parameter.request.SaveCustomerPersonalDataRequest;
import com.sztus.dalaran.microservice.customer.client.object.parameter.response.GetCustomerPersonalDataResponse;
import com.sztus.dalaran.microservice.customer.client.object.parameter.response.GetCustomerResponse;
import com.sztus.dalaran.microservice.customer.client.object.view.CustomerView;
import com.sztus.dalaran.microservice.customer.server.converter.CustomerConverter;
import com.sztus.dalaran.microservice.customer.server.domain.Customer;
import com.sztus.dalaran.microservice.customer.server.domain.CustomerContactData;
import com.sztus.dalaran.microservice.customer.server.domain.CustomerPersonalData;
import com.sztus.dalaran.microservice.customer.server.service.CustomerGeneralService;
import com.sztus.framework.component.core.type.ProcedureException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
public class CustomerGeneralController {

    @Autowired
    private CustomerGeneralService generalService;

    @GetMapping(CustomerAction.GET_CUSTOMER)
    public GetCustomerResponse getCustomer(
            GetCustomerRequest request
    ) throws ProcedureException {
        Long customerId = request.getCustomerId();
        String contactInformation = request.getContactInformation();

        if (Objects.isNull(customerId) && StringUtils.isBlank(contactInformation)) {
            throw new ProcedureException(CustomerErrorCode.PARAMETER_IS_INCOMPLETE);
        }

        Customer customer = null;
        if (Objects.nonNull(customerId)) {
            customer = generalService.getCustomerByCustomerId(customerId);
        }

        if (Objects.isNull(customer) && StringUtils.isNotBlank(contactInformation)) {
            CustomerContactData customerContactData = generalService.getCustomerContactByContact(contactInformation);
            if (Objects.nonNull(customerContactData)) {
                customer = generalService.getCustomerByCustomerId(customerContactData.getCustomerId());
            }
        }

        return CustomerConverter.INSTANCE.CustomerToCustomerView(customer);

    }

    @GetMapping(CustomerAction.GET_CUSTOMER_PERSONAL_DATA)
    public GetCustomerPersonalDataResponse getCustomerPersonalData(
            GetCustomerPersonalDataRequest request
    ) throws ProcedureException {
        Long customerId = request.getCustomerId();
        CustomerPersonalData customerPersonalData = generalService.getCustomerPersonalDataByCustomerId(customerId);
        return CustomerConverter.INSTANCE.PersonalDataToPersonalDataView(customerPersonalData);
    }

    @PostMapping(CustomerAction.SAVE_CUSTOMER_PERSONAL_DATA)
    public Long saveCustomerPersonalData(
            @RequestBody SaveCustomerPersonalDataRequest request
    ) throws ProcedureException {
        CustomerPersonalData personalData = CustomerConverter.INSTANCE.PersonalDataViewToPersonalData(request);
        return generalService.saveCustomerPersonalData(personalData);
    }
}
