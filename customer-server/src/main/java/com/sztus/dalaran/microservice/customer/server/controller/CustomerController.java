package com.sztus.dalaran.microservice.customer.server.controller;

import com.sztus.dalaran.microservice.customer.client.object.parameter.request.*;
import com.sztus.dalaran.microservice.customer.client.object.parameter.response.*;
import com.sztus.dalaran.microservice.customer.client.object.type.CustomerAction;
import com.sztus.dalaran.microservice.customer.client.object.view.CustomerOptInDataView;
import com.sztus.dalaran.microservice.customer.server.converter.CustomerConverter;
import com.sztus.dalaran.microservice.customer.server.converter.CustomerOptInDataConverter;
import com.sztus.dalaran.microservice.customer.server.domain.Customer;
import com.sztus.dalaran.microservice.customer.server.domain.CustomerOptInData;
import com.sztus.dalaran.microservice.customer.server.service.CustomerOptInDataService;
import com.sztus.dalaran.microservice.customer.server.service.CustomerService;
import com.sztus.framework.component.core.type.ProcedureException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
public class CustomerController {

    @GetMapping(CustomerAction.GET_CUSTOMER)
    public GetCustomerResponse getCustomer(
            GetCustomerRequest request
    ) throws ProcedureException {
        String email = request.getEmail();
        String openId = request.getOpenId();

        Customer customer = customerService.getCustomer(email, openId);

        return CustomerConverter.INSTANCE.CustomerToGetCustomerResponse(customer);
    }

    @PostMapping(CustomerAction.SAVE_CUSTOMER)
    public SaveCustomerResponse saveCustomer(
            @RequestBody SaveCustomerRequest request
    ) throws ProcedureException {
        Customer customer = CustomerConverter.INSTANCE.CustomerViewToCustomer(request);
        Customer customerInDb = customerService.saveCustomer(customer);

        return CustomerConverter.INSTANCE.CustomerToSaveCustomerResponse(customerInDb);
    }

    @GetMapping(CustomerAction.GET_CUSTOMER_BY_UNIQUE)
    public GetCustomerByUniqueResponse getCustomerByUnique(
            GetCustomerByUniqueRequest request
    ) throws ProcedureException {
        Customer customerInDb = customerService.getCustomerByUnique(request);

        return CustomerConverter.INSTANCE.CustomerToGetCustomerByUniqueResponse(customerInDb);
    }

    @PostMapping(CustomerAction.CREATE_CUSTOMER)
    public CreateCustomerResponse createCustomer(
            @RequestBody CreateCustomerRequest request
    ) throws ProcedureException {
        Customer customerInDb = customerService.createCustomer(request);

        return CustomerConverter.INSTANCE.CustomerToCreateCustomerResponse(customerInDb);
    }

    @GetMapping(CustomerAction.GET_OPT_IN_AS_LIST)
    public GetCustomerOptInDataAsListResponse getOptInDataAsList(
            GetCustomerOptInDataRequest request
    ) throws ProcedureException {
        Long customerId = request.getCustomerId();

        List<CustomerOptInData> optInDataAsList = customerOptInDataService.getOptInDataAsList(customerId);
        List<CustomerOptInDataView> optInDataViewAsList = CustomerOptInDataConverter.INSTANCE.CustomerOptInDataListToView(optInDataAsList);

        GetCustomerOptInDataAsListResponse response = new GetCustomerOptInDataAsListResponse();
        response.setItems(optInDataViewAsList);

        return response;
    }

    @Autowired
    private CustomerService customerService;

    @Autowired
    private CustomerOptInDataService customerOptInDataService;
}
