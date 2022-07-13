package com.sztus.azeroth.microservice.customer.client.api;

import com.sztus.azeroth.microservice.customer.client.object.parameter.request.*;
import com.sztus.azeroth.microservice.customer.client.object.parameter.response.*;
import com.sztus.azeroth.microservice.customer.client.object.view.CustomerBankAccountDataView;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "microservice-customer-ext", path = "/v4", contextId = "customer-information-api")
public interface CustomerInformationApi {

    @GetMapping("/general/customer/get")
    GetCustomerResponse getCustomer(
            @SpringQueryMap GetCustomerRequest request
    );

    @PostMapping("/general/customer/save")
    SaveCustomerResponse saveCustomer(
            @RequestBody SaveCustomerRequest request
    );

    @GetMapping("/general/personal/get")
    GetCustomerIdentityResponse getCustomerPersonalData(
            @SpringQueryMap GetCustomerRelatedRequest request
    );

    @PostMapping("/general/personal/save")
    SaveCustomerIdentityResponse saveCustomerPersonalData(
            @RequestBody SaveCustomerIdentityRequest request
    );

    @GetMapping("/general/bank-account/list")
    ListBankAccountResponse listBankAccount(
            @SpringQueryMap GetCustomerRelatedRequest request
    );

    @PostMapping("/general/bank-account/save")
    SaveBankAccountResponse saveBankAccount(
            @RequestBody SaveCustomerBankAccountRequest request
    );

    @GetMapping("/general/bank-account/get-by-precedence")
    CustomerBankAccountDataView getBankByPrecedence(
            @RequestParam("customerId") Long customerId
    );

    @GetMapping("/general/employment/get")
    GetCustomerEmploymentResponse getCustomerEmployment(
            @SpringQueryMap GetCustomerRelatedRequest request
    );

    @PostMapping("/general/employment/save")
    SaveCustomerEmploymentResponse saveCustomerEmployment(
            @RequestBody SaveCustomerEmploymentRequest request
    );

    @GetMapping("/general/payroll/get")
    GetCustomerPayrollResponse getCustomerPayroll(
            @SpringQueryMap GetCustomerRelatedRequest request
    );

    @PostMapping("/general/payroll/save")
    SaveCustomerPayrollResponse saveCustomerPayroll(
            @RequestBody SaveCustomerPayrollRequest request
    );

    @GetMapping("/general/contact/list")
    ListCustomerContactResponse listCustomerContact(
            @SpringQueryMap GetCustomerRelatedRequest request
    );

    @PostMapping("/general/contact/save")
    void saveCustomerContactData(
            @RequestBody SaveCustomerContactInfoRequest request
    );

    @GetMapping("/general/contact/get")
    GetCustomerContactDataResponse getCustomerContact(
            @SpringQueryMap GetCustomerContactDataRequest request
    );

    @GetMapping("/general/bank-account/get")
    CustomerBankAccountDataView getBankAccount(
            @SpringQueryMap GetBankAccountRequest request
    );

    @PostMapping("/general/contact/batch-save")
    void batchSaveCustomerContact(
            @RequestBody BatchSaveContactRequest request
    );

    @GetMapping("/general/customer/get/condition")
    GetCustomerByConditionResponse getCustomerByCondition(
            @SpringQueryMap GetCustomerByConditionRequest request
    );

    @GetMapping("/general/bank-account/get")
    GetCustomerAccountResponse getCustomerAccount(
            @SpringQueryMap GetCustomerRelatedRequest request
    );

    @PostMapping("/general/customer-account/save")
    SaveCustomerAccountResponse saveCustomerAccount(
            @RequestBody SaveCustomerAccountRequest request
    );

    @PostMapping("/customer/profiles")
    void saveCustomerProfiles(
            @RequestBody SaveCustomerProfilesRequest request
    );

    @PostMapping("/emulator/delete")
    void emulatorDeleteCustomer(
            @RequestBody EmulatorDeleteRequest request
    );

    @PostMapping("/ibv-authorization/save")
    SaveIbvAuthorizationResponse saveIbvAuthorization(
            @RequestBody SaveIbvAuthorizationRequest request
    );

    @GetMapping("/ibv-authorization/list")
    ListIbvAuthorizationResponse listIbvAuthorization(
            @RequestParam(value = "customerId") Long customerId,
            @RequestParam(value = "portfolioId", required = false) Long portfolioId
    );

}
