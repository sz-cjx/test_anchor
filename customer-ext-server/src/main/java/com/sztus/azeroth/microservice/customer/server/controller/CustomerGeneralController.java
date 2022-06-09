package com.sztus.azeroth.microservice.customer.server.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sztus.azeroth.microservice.customer.client.object.constant.CustomerAction;
import com.sztus.azeroth.microservice.customer.client.object.parameter.enumerate.CustomerErrorCode;
import com.sztus.azeroth.microservice.customer.client.object.parameter.request.*;
import com.sztus.azeroth.microservice.customer.client.object.parameter.response.*;
import com.sztus.azeroth.microservice.customer.client.object.view.CustomerBankAccountDataView;
import com.sztus.azeroth.microservice.customer.client.object.view.CustomerContactInfoView;
import com.sztus.azeroth.microservice.customer.server.converter.CustomerContactDataConverter;
import com.sztus.azeroth.microservice.customer.server.converter.CustomerConverter;
import com.sztus.azeroth.microservice.customer.server.object.domain.*;
import com.sztus.azeroth.microservice.customer.server.service.CustomerGeneralService;
import com.sztus.azeroth.microservice.customer.server.util.CustomerUtil;
import com.sztus.framework.component.core.type.ProcedureException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
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
        String ssn = request.getSsn();
        String routingNo = request.getRoutingNo();
        String accountNo = request.getAccountNo();

        if (Objects.isNull(customerId) && StringUtils.isBlank(contactInformation)
                && StringUtils.isBlank(openId) && StringUtils.isBlank(ssn)
                && StringUtils.isBlank(routingNo) && StringUtils.isBlank(accountNo)) {
            throw new ProcedureException(CustomerErrorCode.PARAMETER_IS_INCOMPLETE);
        }

        Customer customer = null;
        if (Objects.nonNull(customerId)) {
            customer = generalService.getCustomerByCustomerId(customerId);
        }

        if (Objects.isNull(customer) && StringUtils.isNotBlank(contactInformation)) {
            CustomerContactInfo customerContactInfo = generalService.getCustomerContactByContact(contactInformation);
            if (Objects.nonNull(customerContactInfo)) {
                customer = generalService.getCustomerByCustomerId(customerContactInfo.getCustomerId());
            }
        }

        if (Objects.isNull(customer) && StringUtils.isNotBlank(openId)) {
            customer = generalService.getCustomerByOpenId(openId);
        }

        if (Objects.isNull(customer) && (
                StringUtils.isNotBlank(ssn)
                        || StringUtils.isNotBlank(routingNo)
                        || StringUtils.isNotBlank(accountNo)
        )) {
            CustomerUtil.generateUniqueCode(ssn, routingNo, accountNo);
            customer = generalService.getCustomerByUniqueCode(openId);
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
        JSONObject pretreatment = CustomerUtil.pretreatment(JSON.parseObject(JSON.toJSONString(request)));
        request = JSON.parseObject(JSON.toJSONString(pretreatment), SaveCustomerRequest.class);
        Customer customer = CustomerConverter.INSTANCE.CustomerViewToCustomer(request);
        generalService.saveCustomer(customer);

        return CustomerConverter.INSTANCE.CustomerToSaveCustomerResponse(customer);
    }

    @GetMapping(CustomerAction.GET_PERSONAL)
    public GetCustomerIdentityResponse getCustomerPersonalData(
            GetCustomerRelatedRequest request
    ) throws ProcedureException {
        Long customerId = request.getCustomerId();

        CustomerIdentityInfo customerIdentityInfo = generalService.getEntity(CustomerIdentityInfo.class, customerId);
        return CustomerConverter.INSTANCE.PersonalToPersonalView(customerIdentityInfo);
    }

    @PostMapping(CustomerAction.SAVE_PERSONAL)
    public SaveCustomerIdentityResponse saveCustomerIdentity(
            @RequestBody SaveCustomerIdentityRequest request
    ) throws ProcedureException {
        JSONObject pretreatment = CustomerUtil.pretreatment(JSON.parseObject(JSON.toJSONString(request)));
        request = JSON.parseObject(JSON.toJSONString(pretreatment), SaveCustomerIdentityRequest.class);
        CustomerIdentityInfo identityInfo = CustomerConverter.INSTANCE.PersonalViewToPersonal(request);

        if (Objects.isNull(identityInfo.getCustomerId())) {
            throw new ProcedureException(CustomerErrorCode.PARAMETER_IS_INCOMPLETE);
        }
        generalService.saveCustomerPersonal(identityInfo);
        return CustomerConverter.INSTANCE.PersonalDataToSaveResponse(identityInfo);
    }

    @GetMapping(CustomerAction.LIST_BANK_ACCOUNT)
    public ListBankAccountResponse listBankAccount(
            GetCustomerRelatedRequest request
    ) throws ProcedureException {
        Long customerId = request.getCustomerId();
        if (Objects.isNull(customerId)) {
            throw new ProcedureException(CustomerErrorCode.PARAMETER_IS_INCOMPLETE);
        }

        List<CustomerBankAccount> bankAccountDataList = generalService.listBankAccountByCustomerId(customerId);
        List<CustomerBankAccountDataView> items = CustomerConverter.INSTANCE.BankAccountListToViewList(bankAccountDataList);
        ListBankAccountResponse response = new ListBankAccountResponse();
        response.setCount(items.size());
        response.setItems(items);
        return response;
    }

    @PostMapping(CustomerAction.SAVE_BANK_ACCOUNT)
    public CustomerBankAccountDataView saveBankAccount(
            @RequestBody SaveCustomerBankAccountRequest request
    ) throws ProcedureException {
        if (Objects.isNull(request.getCustomerId())) {
            throw new ProcedureException(CustomerErrorCode.PARAMETER_IS_INCOMPLETE);
        }
        JSONObject pretreatment = CustomerUtil.pretreatment(JSON.parseObject(JSON.toJSONString(request)));
        request = JSON.parseObject(JSON.toJSONString(pretreatment), SaveCustomerBankAccountRequest.class);

        CustomerBankAccount bankAccountData = CustomerConverter.INSTANCE.ViewToBankAccountData(request);
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
        CustomerBankAccount dbBankAccountData = generalService.getEntity(CustomerBankAccount.class, id);
        return CustomerConverter.INSTANCE.BankAccountDataToView(dbBankAccountData);
    }

    @GetMapping("/general/bank-account/get-by-precedence")
    public CustomerBankAccountDataView getBankByPrecedence(
            @RequestParam("customerId") Long customerId
    ) {
        CustomerBankAccount dbBankAccountData = generalService.getBankByPrecedence(customerId);
        return CustomerConverter.INSTANCE.BankAccountDataToView(dbBankAccountData);
    }

    @GetMapping(CustomerAction.GET_EMPLOYMENT)
    public GetCustomerEmploymentResponse getCustomerEmployment(
            GetCustomerRelatedRequest request
    ) throws ProcedureException {
        Long customerId = request.getCustomerId();
        CustomerEmploymentInfo employmentData = generalService.getEntity(CustomerEmploymentInfo.class, customerId);
        return CustomerConverter.INSTANCE.CusEmploymentToView(employmentData);
    }

    @PostMapping(CustomerAction.SAVE_EMPLOYMENT)
    public SaveCustomerEmploymentResponse saveCustomerEmployment(
            @RequestBody SaveCustomerEmploymentRequest request
    ) throws ProcedureException {
        JSONObject pretreatment = CustomerUtil.pretreatment(JSON.parseObject(JSON.toJSONString(request)));
        request = JSON.parseObject(JSON.toJSONString(pretreatment), SaveCustomerEmploymentRequest.class);

        CustomerEmploymentInfo employmentData = CustomerConverter.INSTANCE.CusEmploymentViewToData(request);
        generalService.saveCustomerEmployment(employmentData);

        return CustomerConverter.INSTANCE.CusEmploymentToSaveCusEmploymentResponse(employmentData);
    }

    @GetMapping(CustomerAction.GET_PAYROLL)
    public GetCustomerPayrollResponse getCustomerPayroll(
            GetCustomerRelatedRequest request
    ) throws ProcedureException, ParseException {
        Long customerId = request.getCustomerId();
        CustomerPayrollInfo payrollData = generalService.getEntity(CustomerPayrollInfo.class, customerId);
        return CustomerConverter.INSTANCE.CusPayrollToView(payrollData);
    }

    @PostMapping(CustomerAction.SAVE_PAYROLL)
    public SaveCustomerPayrollResponse saveCustomerPayroll(
            @RequestBody SaveCustomerPayrollRequest request
    ) throws ProcedureException, ParseException {
        CustomerPayrollInfo payrollData = CustomerConverter.INSTANCE.CusPayrollViewToData(request);
        generalService.saveCustomerPayroll(payrollData);

        return CustomerConverter.INSTANCE.CusPayrollToSaveCusPayrollResponse(payrollData);
    }

    @GetMapping(CustomerAction.LIST_CUSTOMER_CONTACT)
    public ListCustomerContactResponse listCustomerContact(
            GetCustomerRelatedRequest request
    ) throws ProcedureException {
        Long customerId = request.getCustomerId();
        if (Objects.isNull(customerId)) {
            throw new ProcedureException(CustomerErrorCode.PARAMETER_IS_INCOMPLETE);
        }

        List<CustomerContactInfo> list = generalService.listCustomerContact(customerId);
        List<CustomerContactInfoView> viewList =
                CustomerContactDataConverter.INSTANCE.ListCustomerContactDataToView(list);

        ListCustomerContactResponse response = new ListCustomerContactResponse();
        response.setList(viewList);

        return response;
    }

    @PostMapping(CustomerAction.SAVE_CUSTOMER_CONTACT)
    public void saveCustomerContactData(
            @RequestBody SaveCustomerContactInfoRequest request
    ) throws ProcedureException {
        CustomerContactInfo customerContactInfo = CustomerContactDataConverter.INSTANCE.CustomerContactViewToData(request);

        generalService.saveCustomerContactData(customerContactInfo);
    }

    @PostMapping(CustomerAction.BATCH_SAVE_CUSTOMER_CONTACT)
    public void batchSaveCustomerContact(
            @RequestBody BatchSaveContactRequest request
    ) throws ProcedureException {
        List<CustomerContactInfoView> list = request.getList();
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        List<CustomerContactInfo> customerContactList = CustomerContactDataConverter.INSTANCE.ListCustomerContactViewToDate(list);

        for (CustomerContactInfo customerContactInfo : customerContactList) {
            generalService.saveCustomerContactData(customerContactInfo);
        }
    }


    @GetMapping("/general/credit-evaluation/get")
    public GetCreditEvaluationResponse getCreditEvaluation(
            @RequestParam("customerId") Long customerId
    )throws ProcedureException {

        CustomerCreditEvaluation creditEvaluation = generalService.getEntity(CustomerCreditEvaluation.class, customerId);

        return CustomerConverter.INSTANCE.CustomerCreditEvaluationToView(creditEvaluation);
    }

    @PostMapping("/general/credit-evaluation/save")
    public void saveCreditEvaluation(
            @RequestBody SaveCreditEvaluationRequest request
    ) throws ProcedureException {
        Long customerId = request.getCustomerId();
        if (Objects.isNull(customerId)) {
            throw new ProcedureException(CustomerErrorCode.PARAMETER_IS_INCOMPLETE);
        }

        CustomerCreditEvaluation customerCreditEvaluation = CustomerConverter.INSTANCE.CustomerCreditEvaluationViewToData(request);
        generalService.saveCreditEvaluation(customerCreditEvaluation);
    }

    @GetMapping(CustomerAction.GET_CUSTOMER_CONTACT)
    public GetCustomerContactDataResponse getCustomerContactData(
            GetCustomerContactDataRequest request
    ) throws ProcedureException {
        Long customerId = request.getCustomerId();
        Integer type = request.getType();

        CustomerContactInfo customerContactData = generalService.getCustomerContactData(customerId, type);
        return CustomerContactDataConverter.INSTANCE.CustomerContactDataToView(customerContactData);
    }

    @GetMapping(CustomerAction.GET_CUSTOMER_BY_CONDITION)
    public GetCustomerByConditionResponse getCustomerByCondition(
            GetCustomerByConditionRequest request
    ) throws ProcedureException {
        String phone = request.getPhone();
        String email = request.getEmail();
        Customer customer = generalService.getCustomerByCondition(phone, email);
        return CustomerConverter.INSTANCE.CustomerToGetCustomerByConditionResponse(customer);
    }

    @GetMapping(CustomerAction.GET_CUSTOMER_ACCOUNT)
    public GetCustomerAccountResponse getCustomerAccount(
            GetCustomerRelatedRequest request
    ) throws ProcedureException {
        Long customerId = request.getCustomerId();
        CustomerAccount customerAccount = generalService.getEntity(CustomerAccount.class, customerId);
        return CustomerConverter.INSTANCE.CusAccountToView(customerAccount);
    }

    @PostMapping(CustomerAction.SAVE_CUSTOMER_ACCOUNT)
    public SaveCustomerAccountResponse saveCustomerAccount(
            @RequestBody SaveCustomerAccountRequest request
    ) throws ProcedureException {
        CustomerAccount customerAccount = CustomerConverter.INSTANCE.CusAccountViewToData(request);
        generalService.saveCustomerAccount(customerAccount);
        return CustomerConverter.INSTANCE.CusAccountToSaveCusEmploymentResponse(customerAccount);
    }

    @PostMapping("/customer/profiles")
    public void saveCustomerProfiles(
            @RequestBody SaveCustomerProfilesRequest request
    ) throws ProcedureException, ParseException {
        Long customerId = request.getCustomerId();

        SaveCustomerBankAccountRequest bankAccount = request.getBankAccount();
        if (Objects.nonNull(bankAccount)) {
            bankAccount.setCustomerId(customerId);
            saveBankAccount(bankAccount);
        }

        SaveCustomerEmploymentRequest employment = request.getEmployment();
        if (Objects.nonNull(employment)) {
            employment.setCustomerId(customerId);
            saveCustomerEmployment(employment);
        }

        SaveCustomerIdentityRequest identity = request.getIdentity();
        if (Objects.nonNull(identity)) {
            identity.setCustomerId(customerId);
            saveCustomerIdentity(identity);
        }

        SaveCustomerPayrollRequest payroll = request.getPayroll();
        if (Objects.nonNull(payroll)) {
            payroll.setCustomerId(customerId);
            saveCustomerPayroll(payroll);
        }

        BatchSaveContactRequest contactInfo = request.getContactInfo();
        if (Objects.nonNull(contactInfo)) {
            List<CustomerContactInfoView> list = contactInfo.getList();
            for (CustomerContactInfoView view : list) {
                view.setCustomerId(customerId);
            }
            contactInfo.setList(list);
            batchSaveCustomerContact(contactInfo);
        }
    }

}
