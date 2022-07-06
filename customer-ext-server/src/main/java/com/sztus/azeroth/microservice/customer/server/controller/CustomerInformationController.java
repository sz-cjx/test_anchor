package com.sztus.azeroth.microservice.customer.server.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sztus.azeroth.microservice.customer.client.object.parameter.enumerate.CustomerErrorCode;
import com.sztus.azeroth.microservice.customer.client.object.parameter.request.*;
import com.sztus.azeroth.microservice.customer.client.object.parameter.response.*;
import com.sztus.azeroth.microservice.customer.client.object.util.EncryptUtil;
import com.sztus.azeroth.microservice.customer.client.object.view.CustomerBankAccountDataView;
import com.sztus.azeroth.microservice.customer.client.object.view.CustomerContactInfoView;
import com.sztus.azeroth.microservice.customer.client.object.view.IbvAuthorizationView;
import com.sztus.azeroth.microservice.customer.server.converter.CustomerContactDataConverter;
import com.sztus.azeroth.microservice.customer.server.converter.CustomerConverter;
import com.sztus.azeroth.microservice.customer.server.converter.CustomerIbvAuthorizationConverter;
import com.sztus.azeroth.microservice.customer.server.object.domain.*;
import com.sztus.azeroth.microservice.customer.server.service.CustomerInformationService;
import com.sztus.azeroth.microservice.customer.server.util.CustomerUtil;
import com.sztus.framework.component.core.type.ProcedureException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
public class CustomerInformationController {

    @Autowired
    private CustomerInformationService informationService;

    @GetMapping("/general/customer/get")
    public GetCustomerResponse getCustomer(
            GetCustomerRequest request
    ) throws ProcedureException {
        Long customerId = request.getCustomerId();
        String contactInformation = request.getContactInformation();
        if (Objects.nonNull(contactInformation)) {
            contactInformation = contactInformation.toLowerCase();
        }
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
            customer = informationService.getCustomerByCustomerId(customerId);
        }

        if (Objects.isNull(customer) && StringUtils.isNotBlank(contactInformation)) {
            CustomerContactInfo customerContactInfo = informationService.getCustomerContactByContact(contactInformation);
            if (Objects.nonNull(customerContactInfo)) {
                customer = informationService.getCustomerByCustomerId(customerContactInfo.getCustomerId());
            }
        }

        if (Objects.isNull(customer) && StringUtils.isNotBlank(openId)) {
            customer = informationService.getCustomerByOpenId(openId);
        }

        if (Objects.isNull(customer) && (
                StringUtils.isNotBlank(ssn)
                        || StringUtils.isNotBlank(routingNo)
                        || StringUtils.isNotBlank(accountNo)
        )) {
            CustomerUtil.generateUniqueCode(ssn, routingNo, accountNo);
            //todo 是openid还是上面的结果
            customer = informationService.getCustomerByUniqueCode(openId);
        }

        if (Objects.isNull(customer)) {
            throw new ProcedureException(CustomerErrorCode.CUSTOMER_IS_NOT_EXISTED);
        }

        return CustomerConverter.INSTANCE.CustomerToCustomerView(customer);

    }

    @PostMapping("/general/customer/save")
    public SaveCustomerResponse saveCustomer(
            @RequestBody SaveCustomerRequest request
    ) throws ProcedureException {
        JSONObject pretreatment = CustomerUtil.pretreatment(JSON.parseObject(JSON.toJSONString(request)));
        request = JSON.parseObject(JSON.toJSONString(pretreatment), SaveCustomerRequest.class);
        Customer customer = CustomerConverter.INSTANCE.CustomerViewToCustomer(request);
        informationService.saveCustomer(customer);

        return CustomerConverter.INSTANCE.CustomerToSaveCustomerResponse(customer);
    }

    @GetMapping("/general/personal/get")
    public GetCustomerIdentityResponse getCustomerPersonalData(
            GetCustomerRelatedRequest request
    ) {
        Long customerId = request.getCustomerId();

        CustomerIdentityInfo customerIdentityInfo = informationService.getCustomerPersonalData(customerId);
        return CustomerConverter.INSTANCE.PersonalToPersonalView(customerIdentityInfo);
    }

    @PostMapping("/general/personal/save")
    public SaveCustomerIdentityResponse saveCustomerIdentity(
            @RequestBody SaveCustomerIdentityRequest request
    ) throws ProcedureException {
        JSONObject pretreatment = CustomerUtil.pretreatment(JSON.parseObject(JSON.toJSONString(request)));
        request = JSON.parseObject(JSON.toJSONString(pretreatment), SaveCustomerIdentityRequest.class);
        CustomerIdentityInfo identityInfo = CustomerConverter.INSTANCE.PersonalViewToPersonal(request);

        if (Objects.isNull(identityInfo.getCustomerId())) {
            throw new ProcedureException(CustomerErrorCode.PARAMETER_IS_INCOMPLETE);
        }
        informationService.saveCustomerPersonal(identityInfo);
        return CustomerConverter.INSTANCE.PersonalDataToSaveResponse(identityInfo);
    }

    @GetMapping("/general/bank-account/list")
    public ListBankAccountResponse listBankAccount(
            GetCustomerRelatedRequest request
    ) throws ProcedureException {
        Long customerId = request.getCustomerId();
        if (Objects.isNull(customerId)) {
            throw new ProcedureException(CustomerErrorCode.PARAMETER_IS_INCOMPLETE);
        }

        List<CustomerBankAccount> bankAccountDataList = informationService.listBankAccountByCustomerId(customerId);
        List<CustomerBankAccountDataView> items = CustomerConverter.INSTANCE.BankAccountListToViewList(bankAccountDataList);
        for (CustomerBankAccountDataView item : items) {
            item.setBankAccountNo(EncryptUtil.AESDecode(item.getBankAccountNo()));
        }
        ListBankAccountResponse response = new ListBankAccountResponse();
        response.setCount(items.size());
        response.setItems(items);
        return response;
    }

    @PostMapping("/general/bank-account/save")
    public CustomerBankAccountDataView saveBankAccount(
            @RequestBody SaveCustomerBankAccountRequest request
    ) throws ProcedureException {
        if (Objects.isNull(request.getCustomerId())) {
            throw new ProcedureException(CustomerErrorCode.PARAMETER_IS_INCOMPLETE);
        }
        if (StringUtils.isBlank(request.getBankAccountNo()) || StringUtils.isBlank(request.getBankRoutingNo())){
            throw new ProcedureException(CustomerErrorCode.PARAMETER_IS_INCOMPLETE);
        }
        JSONObject pretreatment = CustomerUtil.pretreatment(JSON.parseObject(JSON.toJSONString(request)));
        request = JSON.parseObject(JSON.toJSONString(pretreatment), SaveCustomerBankAccountRequest.class);

        CustomerBankAccount bankAccountData = CustomerConverter.INSTANCE.ViewToBankAccountData(request);
        Long result = informationService.saveBankAccount(bankAccountData);
        if (Objects.isNull(request.getId())) {
            bankAccountData.setId(result);
        }
        return CustomerConverter.INSTANCE.BankAccountDataToView(bankAccountData);
    }

    @GetMapping("/general/bank-account/get")
    public CustomerBankAccountDataView getBankAccount(
            GetBankAccountRequest request
    ) throws ProcedureException {
        Long id = request.getId();
        Long customerId = request.getCustomerId();
        CustomerBankAccount dbBankAccountData;

        if (Objects.isNull(id) && Objects.isNull(customerId)) {
            throw new ProcedureException(CustomerErrorCode.PARAMETER_IS_INCOMPLETE);
        }

        if (Objects.nonNull(id)) {
            dbBankAccountData = informationService.getEntity(CustomerBankAccount.class, id);
        } else {
            dbBankAccountData = informationService.getCustomerBankAccountByCustomerId(customerId);
        }
        if (Objects.nonNull(dbBankAccountData)) {
            if (StringUtils.isNotBlank(dbBankAccountData.getBankAccountNo())) {
                dbBankAccountData.setBankAccountNo(EncryptUtil.AESDecode(dbBankAccountData.getBankAccountNo()));
            }
        }
        return CustomerConverter.INSTANCE.BankAccountDataToView(dbBankAccountData);
    }

    @GetMapping("/general/bank-account/get-by-precedence")
    public CustomerBankAccountDataView getBankByPrecedence(
            @RequestParam("customerId") Long customerId
    ) {
        CustomerBankAccount dbBankAccountData = informationService.getBankByPrecedence(customerId);
        if (Objects.nonNull(dbBankAccountData)) {
            dbBankAccountData.setBankAccountNo(EncryptUtil.AESDecode(dbBankAccountData.getBankAccountNo()));
        }
        return CustomerConverter.INSTANCE.BankAccountDataToView(dbBankAccountData);
    }

    @GetMapping("/general/employment/get")
    public GetCustomerEmploymentResponse getCustomerEmployment(
            GetCustomerRelatedRequest request
    ) throws ProcedureException {
        Long customerId = request.getCustomerId();
        CustomerEmploymentInfo employmentData = informationService.getEntity(CustomerEmploymentInfo.class, customerId);
        return CustomerConverter.INSTANCE.CusEmploymentToView(employmentData);
    }

    @PostMapping("/general/employment/save")
    public SaveCustomerEmploymentResponse saveCustomerEmployment(
            @RequestBody SaveCustomerEmploymentRequest request
    ) throws ProcedureException {
        JSONObject pretreatment = CustomerUtil.pretreatment(JSON.parseObject(JSON.toJSONString(request)));
        request = JSON.parseObject(JSON.toJSONString(pretreatment), SaveCustomerEmploymentRequest.class);

        CustomerEmploymentInfo employmentData = CustomerConverter.INSTANCE.CusEmploymentViewToData(request);
        informationService.saveCustomerEmployment(employmentData);

        return CustomerConverter.INSTANCE.CusEmploymentToSaveCusEmploymentResponse(employmentData);
    }

    @GetMapping("/general/payroll/get")
    public GetCustomerPayrollResponse getCustomerPayroll(
            GetCustomerRelatedRequest request
    ) throws ProcedureException, ParseException {
        Long customerId = request.getCustomerId();
        CustomerPayrollInfo payrollData = informationService.getEntity(CustomerPayrollInfo.class, customerId);
        return CustomerConverter.INSTANCE.CusPayrollToView(payrollData);
    }

    @PostMapping("/general/payroll/save")
    public SaveCustomerPayrollResponse saveCustomerPayroll(
            @RequestBody SaveCustomerPayrollRequest request
    ) throws ProcedureException, ParseException {
        CustomerPayrollInfo payrollData = CustomerConverter.INSTANCE.CusPayrollViewToData(request);
        informationService.saveCustomerPayroll(payrollData);

        return CustomerConverter.INSTANCE.CusPayrollToSaveCusPayrollResponse(payrollData);
    }

    @GetMapping("/general/contact/list")
    public ListCustomerContactResponse listCustomerContact(
            GetCustomerRelatedRequest request
    ) throws ProcedureException {
        Long customerId = request.getCustomerId();
        if (Objects.isNull(customerId)) {
            throw new ProcedureException(CustomerErrorCode.PARAMETER_IS_INCOMPLETE);
        }

        List<CustomerContactInfo> list = informationService.listCustomerContact(customerId);
        List<CustomerContactInfoView> viewList =
                CustomerContactDataConverter.INSTANCE.ListCustomerContactDataToView(list);

        for (CustomerContactInfoView customerContactInfoView : viewList) {
            if (StringUtils.isNotBlank(customerContactInfoView.getValue())) {
                customerContactInfoView.setValue(EncryptUtil.AESDecode(customerContactInfoView.getValue()));
            }
        }

        ListCustomerContactResponse response = new ListCustomerContactResponse();
        response.setList(viewList);

        return response;
    }

    @PostMapping("/general/contact/save")
    public void saveCustomerContactData(
            @RequestBody SaveCustomerContactInfoRequest request
    ) throws ProcedureException {
        CustomerContactInfo customerContactInfo = CustomerContactDataConverter.INSTANCE.CustomerContactViewToData(request);

        informationService.saveCustomerContactData(customerContactInfo, request.getIsVerified());
    }

    @PostMapping("/general/contact/batch-save")
    public void batchSaveCustomerContact(
            @RequestBody BatchSaveContactRequest request
    ) throws ProcedureException {
        List<CustomerContactInfoView> list = request.getList();
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        Boolean isVerified = request.getIsVerified();
        List<CustomerContactInfo> customerContactList = CustomerContactDataConverter.INSTANCE.ListCustomerContactViewToDate(list);

        for (CustomerContactInfo customerContactInfo : customerContactList) {
            informationService.saveCustomerContactData(customerContactInfo, isVerified);
        }
    }


    @GetMapping("/general/credit-evaluation/get")
    public GetCreditEvaluationResponse getCreditEvaluation(
            @RequestParam("customerId") Long customerId
    ) throws ProcedureException {

        CustomerCreditEvaluation creditEvaluation = informationService.getEntity(CustomerCreditEvaluation.class, customerId);

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
        informationService.saveCreditEvaluation(customerCreditEvaluation);
    }

    @GetMapping("/general/contact/get")
    public GetCustomerContactDataResponse getCustomerContactData(
            GetCustomerContactDataRequest request
    ) throws ProcedureException {
        Long customerId = request.getCustomerId();
        Integer type = request.getType();

        CustomerContactInfo customerContactData = informationService.getCustomerContactData(customerId, type);
        //联系方式解密
        if (Objects.nonNull(customerContactData)) {
            customerContactData.setValue(EncryptUtil.AESDecode(customerContactData.getValue()));
        }
        return CustomerContactDataConverter.INSTANCE.CustomerContactDataToView(customerContactData);
    }

    @GetMapping("/general/customer/get/condition")
    public GetCustomerByConditionResponse getCustomerByCondition(
            GetCustomerByConditionRequest request
    ) throws ProcedureException {
        String phone = request.getPhone();
        String email = request.getEmail();
        Customer customer = informationService.getCustomerByCondition(phone, email);
        return CustomerConverter.INSTANCE.CustomerToGetCustomerByConditionResponse(customer);
    }

    @GetMapping("/general/customer-account/get")
    public GetCustomerAccountResponse getCustomerAccount(
            GetCustomerRelatedRequest request
    ) throws ProcedureException {
        Long customerId = request.getCustomerId();
        CustomerAccount customerAccount = informationService.getEntity(CustomerAccount.class, customerId);
        return CustomerConverter.INSTANCE.CusAccountToView(customerAccount);
    }

    @PostMapping("/general/customer-account/save")
    public SaveCustomerAccountResponse saveCustomerAccount(
            @RequestBody SaveCustomerAccountRequest request
    ) throws ProcedureException {
        CustomerAccount customerAccount = CustomerConverter.INSTANCE.CusAccountViewToData(request);
        informationService.saveCustomerAccount(customerAccount);
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

    @PostMapping("/emulator/delete")
    public void emulatorDelete(
            @RequestBody EmulatorDeleteRequest request
    ) {
        String email = request.getEmail();
        if (StringUtils.isBlank(email)) {
            return;
        }
        List<CustomerContactInfo> list = informationService.listCustomerContact(email);
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        List<Long> customerIds = list.stream().map(CustomerContactInfo::getCustomerId).collect(Collectors.toList());

        informationService.deleteCustomerInformation(customerIds);
    }

    @PostMapping("/ibv-authorization/save")
    public SaveIbvAuthorizationResponse saveIbvAuthorization(
            @RequestBody SaveIbvAuthorizationRequest request
    ) throws ProcedureException {
        Long customerId = request.getCustomerId();
        if (Objects.isNull(customerId)) {
            throw new ProcedureException(CustomerErrorCode.PARAMETER_IS_INCOMPLETE);
        }

        CustomerIbvAuthorizationRecord ibvAuthorizationRecord = CustomerIbvAuthorizationConverter.INSTANCE.ibvRequestToData(request);
        Long result = informationService.saveIbvAuthorization(ibvAuthorizationRecord);
        if (result > 1) {
            ibvAuthorizationRecord.setId(result);
        }

        return CustomerIbvAuthorizationConverter.INSTANCE.ibvViewToResponse(ibvAuthorizationRecord);
    }

    @GetMapping("/ibv-authorization/list")
    public ListIbvAuthorizationResponse listIbvAuthorization(
            @RequestParam(value = "customerId") Long customerId,
            @RequestParam(value = "portfolioId", required = false) Long portfolioId
    ) throws ProcedureException {
        if (Objects.isNull(customerId)) {
            throw new ProcedureException(CustomerErrorCode.PARAMETER_IS_INCOMPLETE);
        }

        List<CustomerIbvAuthorizationRecord> ibvAuthorizationRecords = informationService.listIbvAuthorization(customerId, portfolioId);
        List<IbvAuthorizationView> ibvAuthorizationViews = CustomerIbvAuthorizationConverter.INSTANCE.ibvDataListToView(ibvAuthorizationRecords);

        ListIbvAuthorizationResponse response = new ListIbvAuthorizationResponse();
        response.setCount(ibvAuthorizationViews.size());
        response.setItems(ibvAuthorizationViews);
        return response;
    }
}
