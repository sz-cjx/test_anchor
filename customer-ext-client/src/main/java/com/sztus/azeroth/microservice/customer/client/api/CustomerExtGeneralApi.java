package com.sztus.azeroth.microservice.customer.client.api;

import com.sztus.azeroth.microservice.customer.client.object.constant.CustomerAction;
import com.sztus.azeroth.microservice.customer.client.object.parameter.request.*;
import com.sztus.azeroth.microservice.customer.client.object.parameter.response.*;
import com.sztus.azeroth.microservice.customer.client.object.view.CustomerBankAccountDataView;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "microservice-customer-ext", path = "/v4", contextId = "customer-ext-api")
public interface CustomerExtGeneralApi {

    @GetMapping(CustomerAction.GET_CUSTOMER)
    GetCustomerResponse getCustomer(
            @SpringQueryMap GetCustomerRequest request
    );

    @PostMapping(CustomerAction.SAVE_CUSTOMER)
    SaveCustomerResponse saveCustomer(
            @RequestBody SaveCustomerRequest request
    );

    @GetMapping(CustomerAction.GET_PERSONAL)
    GetCustomerIdentityResponse getCustomerPersonalData(
            @SpringQueryMap GetCustomerPersonalRequest request
    );

    @PostMapping(CustomerAction.SAVE_PERSONAL)
    SaveCustomerIdentityResponse saveCustomerPersonalData(
            @RequestBody SaveCustomerIdentityRequest request
    );

    @GetMapping(CustomerAction.LIST_BANK_ACCOUNT)
    ListBankAccountResponse listBankAccount(
            @SpringQueryMap ListBankAccountRequest request
    );

    @PostMapping(CustomerAction.SAVE_BANK_ACCOUNT)
    SaveBankAccountResponse saveBankAccount(
            @RequestBody SaveBankAccountRequest request
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

    @GetMapping(CustomerAction.LIST_CUSTOMER_CONTACT)
    ListCustomerContactResponse listCustomerContact(
            @SpringQueryMap ListCustomerContactRequest request
    );

    @PostMapping(CustomerAction.SAVE_CUSTOMER_CONTACT)
    void saveCustomerContactData(
            @RequestBody SaveCustomerContactInfoRequest request
    );

    @GetMapping(CustomerAction.GET_CUSTOMER_CONTACT)
    GetCustomerContactDataResponse getCustomerContact(
            @SpringQueryMap GetCustomerContactDataRequest request
    );

    @GetMapping(CustomerAction.GET_BANK_ACCOUNT)
    CustomerBankAccountDataView getBankAccount(
            @SpringQueryMap GetBankAccountRequest request
    );

    @PostMapping(CustomerAction.BATCH_SAVE_CUSTOMER_CONTACT)
    void batchSaveCustomerContact(
            @RequestBody BatchSaveContactRequest request
    );

    @GetMapping(CustomerAction.GET_CUSTOMER_BY_CONDITION)
    GetCustomerByConditionResponse getCustomerByCondition(
            @SpringQueryMap GetCustomerByConditionRequest request
    );

    @GetMapping(CustomerAction.GET_CREDIT_EVALUATION)
    GetCreditEvaluationResponse getCreditEvaluation(
            @SpringQueryMap GetCustomerRelatedRequest request
    );

    @PostMapping(CustomerAction.SAVE_CREDIT_EVALUATION)
    void saveCreditEvaluation(
            @RequestBody SaveCreditEvaluationRequest request
    );

}
