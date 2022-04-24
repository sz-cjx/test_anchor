package com.sztus.dalaran.microservice.customer.server.controller;

import com.sztus.dalaran.microservice.customer.client.object.constant.CustomerAction;
import com.sztus.dalaran.microservice.customer.client.object.parameter.enumerate.CustomerErrorCode;
import com.sztus.dalaran.microservice.customer.client.object.parameter.request.*;
import com.sztus.dalaran.microservice.customer.client.object.parameter.response.*;
import com.sztus.dalaran.microservice.customer.client.object.view.CustomerBankAccountDataView;
import com.sztus.dalaran.microservice.customer.client.object.view.CustomerContactDataView;
import com.sztus.dalaran.microservice.customer.server.converter.CustomerContactDataConverter;
import com.sztus.dalaran.microservice.customer.server.converter.CustomerConverter;
import com.sztus.dalaran.microservice.customer.server.object.domain.*;
import com.sztus.dalaran.microservice.customer.server.service.CustomerGeneralService;
import com.sztus.framework.component.core.type.ProcedureException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
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
        String openId = request.getOpenId();

        if (Objects.isNull(customerId) && StringUtils.isBlank(contactInformation) && StringUtils.isBlank(openId)) {
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

        if (Objects.isNull(customer) && StringUtils.isNotBlank(openId)) {
            customer = generalService.getCustomerByOpenId(openId);
        }

        if (Objects.isNull(customer)) {
            throw new ProcedureException(CustomerErrorCode.CUSTOMER_IS_NOT_EXISTED);
        }
        return CustomerConverter.INSTANCE.CustomerToCustomerView(customer);

    }

    @PostMapping(CustomerAction.SAVE_CUSTOMER)
    public SaveCustomerResponse saveCustomer(
            @RequestBody SaveCustomerRequest request
    ) throws ProcedureException {
        Customer customer = CustomerConverter.INSTANCE.CustomerViewToCustomer(request);
        generalService.saveCustomer(customer);

        return CustomerConverter.INSTANCE.CustomerToSaveCustomerResponse(customer);
    }

    @GetMapping(CustomerAction.GET_PERSONAL)
    public GetCustomerPersonalResponse getCustomerPersonalData(
            GetCustomerPersonalRequest request
    ) throws ProcedureException {
        Long customerId = request.getCustomerId();
        if (Objects.isNull(customerId)) {
            throw new ProcedureException(CustomerErrorCode.PARAMETER_IS_INCOMPLETE);
        }

        CustomerPersonalData customerPersonalData = generalService.getPersonalByCustomerId(customerId);
        return CustomerConverter.INSTANCE.PersonalToPersonalView(customerPersonalData);
    }

    @PostMapping(CustomerAction.SAVE_PERSONAL)
    public SaveCustomerPersonalResponse saveCustomerPersonalData(
            @RequestBody SaveCustomerPersonalRequest request
    ) throws ProcedureException {
        CustomerPersonalData personalData = CustomerConverter.INSTANCE.PersonalViewToPersonal(request);

        if (Objects.isNull(personalData.getCustomerId())) {
            throw new ProcedureException(CustomerErrorCode.PARAMETER_IS_INCOMPLETE);
        }
        generalService.saveCustomerPersonal(personalData);
        return CustomerConverter.INSTANCE.PersonalDataToSaveResponse(personalData);
    }

    @GetMapping(CustomerAction.LIST_BANK_ACCOUNT)
    public ListBankAccountResponse listBankAccount(
            ListBankAccountRequest request
    ) throws ProcedureException {
        Long customerId = request.getCustomerId();
        if (Objects.isNull(customerId)) {
            throw new ProcedureException(CustomerErrorCode.PARAMETER_IS_INCOMPLETE);
        }

        List<CustomerBankAccountData> bankAccountDataList = generalService.listBankAccountByCustomerId(customerId);
        List<CustomerBankAccountDataView> items = CustomerConverter.INSTANCE.BankAccountListToViewList(bankAccountDataList);
        ListBankAccountResponse response = new ListBankAccountResponse();
        response.setCount(items.size());
        response.setItems(items);
        return response;
    }

    @PostMapping(CustomerAction.SAVE_BANK_ACCOUNT)
    public CustomerBankAccountDataView saveBankAccount(
            @RequestBody SaveBankAccountRequest request
    ) throws ProcedureException {
        if (Objects.isNull(request.getCustomerId())) {
            throw new ProcedureException(CustomerErrorCode.PARAMETER_IS_INCOMPLETE);
        }

        CustomerBankAccountData bankAccountData = CustomerConverter.INSTANCE.ViewToBankAccountData(request);
        Long result = generalService.saveBankAccount(bankAccountData);
        if (Objects.isNull(request.getId())) {
            bankAccountData.setId(result);
        }
        return CustomerConverter.INSTANCE.BankAccountDataToView(bankAccountData);
    }

    @GetMapping(CustomerAction.GET_BANK_ACCOUNT)
    public CustomerBankAccountDataView getBankAccount(
            GetBankAccountRequest request
    ) throws ProcedureException {
        Long id = request.getId();
        if (Objects.isNull(id)) {
            throw new ProcedureException(CustomerErrorCode.PARAMETER_IS_INCOMPLETE);
        }
        CustomerBankAccountData dbBankAccountData = generalService.getBankAccountById(id);
        return CustomerConverter.INSTANCE.BankAccountDataToView(dbBankAccountData);
    }

    @GetMapping(CustomerAction.GET_EMPLOYMENT)
    public GetCustomerEmploymentResponse getCustomerEmployment(
            GetCustomerEmploymentRequest request
    ) throws ProcedureException {
        Long customerId = request.getCustomerId();
        if (Objects.isNull(customerId)) {
            throw new ProcedureException(CustomerErrorCode.PARAMETER_IS_INCOMPLETE);
        }

        CustomerEmploymentData employmentData = generalService.getCustomerEmploymentByCustomerId(customerId);

        return CustomerConverter.INSTANCE.CusEmploymentToView(employmentData);
    }

    @PostMapping(CustomerAction.SAVE_EMPLOYMENT)
    public SaveCustomerEmploymentResponse saveCustomerEmployment(
            @RequestBody SaveCustomerEmploymentRequest request
    ) throws ProcedureException {
        CustomerEmploymentData employmentData = CustomerConverter.INSTANCE.CusEmploymentViewToData(request);
        generalService.saveCustomerEmployment(employmentData);

        return CustomerConverter.INSTANCE.CusEmploymentToSaveCusEmploymentResponse(employmentData);
    }

    @GetMapping(CustomerAction.GET_PAYROLL)
    public GetCustomerPayrollResponse getCustomerPayroll(
            GetCustomerPayrollRequest request
    ) throws ProcedureException {
        Long customerId = request.getCustomerId();
        if (Objects.isNull(customerId)) {
            throw new ProcedureException(CustomerErrorCode.PARAMETER_IS_INCOMPLETE);
        }

        CustomerPayrollData payrollData = generalService.getCustomerPayrollByCustomerId(customerId);
        return CustomerConverter.INSTANCE.CusPayrollToView(payrollData);
    }

    @PostMapping(CustomerAction.SAVE_PAYROLL)
    public SaveCustomerPayrollResponse saveCustomerPayroll(
            @RequestBody SaveCustomerPayrollRequest request
    ) throws ProcedureException {
        CustomerPayrollData payrollData = CustomerConverter.INSTANCE.CusPayrollViewToData(request);
        generalService.saveCustomerPayroll(payrollData);

        return CustomerConverter.INSTANCE.CusPayrollToSaveCusPayrollResponse(payrollData);
    }

    @GetMapping(CustomerAction.LIST_CUSTOMER_CONTACT)
    public ListCustomerContactResponse listCustomerContact(
            ListCustomerContactRequest request
    ) throws ProcedureException {
        Long customerId = request.getCustomerId();
        if (Objects.isNull(customerId)) {
            throw new ProcedureException(CustomerErrorCode.PARAMETER_IS_INCOMPLETE);
        }

        List<CustomerContactData> list = generalService.listCustomerContact(customerId);
        List<CustomerContactDataView> viewList =
                CustomerContactDataConverter.INSTANCE.ListCustomerContactDataToView(list);

        ListCustomerContactResponse response = new ListCustomerContactResponse();
        response.setList(viewList);

        return response;
    }

    @PostMapping(CustomerAction.SAVE_CUSTOMER_CONTACT)
    public void saveCustomerContactData(
            @RequestBody SaveCustomerContactDataRequest request
    ) throws ProcedureException {
        CustomerContactData customerContactData = CustomerContactDataConverter.INSTANCE.CustomerContactViewToData(request);

        generalService.saveCustomerContactData(customerContactData);
    }

    @PostMapping(CustomerAction.BATCH_SAVE_CUSTOMER_CONTACT)
    public void batchSaveCustomerContact(
            @RequestBody BatchSaveContactRequest request
    ) throws ProcedureException {
        List<CustomerContactDataView> list = request.getList();
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        List<CustomerContactData> customerContactList = CustomerContactDataConverter.INSTANCE.ListCustomerContactViewToDate(list);

        for (CustomerContactData customerContactData : customerContactList) {
            generalService.saveCustomerContactData(customerContactData);
        }
    }

}