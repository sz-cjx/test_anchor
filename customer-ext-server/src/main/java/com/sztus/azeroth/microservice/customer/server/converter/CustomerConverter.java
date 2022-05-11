package com.sztus.azeroth.microservice.customer.server.converter;

import com.sztus.azeroth.microservice.customer.client.object.parameter.response.*;
import com.sztus.azeroth.microservice.customer.client.object.view.*;
import com.sztus.azeroth.microservice.customer.server.object.domain.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.text.ParseException;
import java.util.List;

@Mapper
public interface CustomerConverter {

    CustomerConverter INSTANCE = Mappers.getMapper(CustomerConverter.class);

    GetCustomerResponse CustomerToCustomerView(Customer customer);

    Customer CustomerViewToCustomer(CustomerView customerView);

    SaveCustomerResponse CustomerToSaveCustomerResponse(Customer customer);

    GetCustomerIdentityResponse PersonalToPersonalView(CustomerIdentityInfo customerIdentityInfo);

    CustomerIdentityInfo PersonalViewToPersonal(CustomerIdentityView view);

    List<CustomerBankAccountDataView> BankAccountListToViewList(List<CustomerBankAccount> list);

    CustomerBankAccount ViewToBankAccountData(CustomerBankAccountDataView view);

    CustomerBankAccountDataView BankAccountDataToView(CustomerBankAccount bankAccountData);

    SaveCustomerIdentityResponse PersonalDataToSaveResponse(CustomerIdentityInfo personalData);

    GetCustomerEmploymentResponse CusEmploymentToView(CustomerEmploymentInfo employmentData);

    CustomerEmploymentInfo CusEmploymentViewToData(CustomerEmploymentView customerEmploymentView);

    SaveCustomerEmploymentResponse CusEmploymentToSaveCusEmploymentResponse(CustomerEmploymentInfo customerEmploymentInfo);

    @Mapping(
            target = "lastPayday",
            expression = "java(com.sztus.framework.component.core.util.DateUtil.timeStampToStr(payrollData.getLastPayday()," +
                    "com.sztus.framework.component.core.util.DateUtil.DEFAULT_DATE_PATTERN))"
    )
    GetCustomerPayrollResponse CusPayrollToView(CustomerPayrollInfo payrollData) throws ParseException;

    @Mapping(
            target = "lastPayday",
            expression = "java(com.sztus.framework.component.core.util.DateUtil.strToTimeStamp(payrollView.getLastPayday()," +
                    "com.sztus.framework.component.core.util.DateUtil.DEFAULT_DATE_PATTERN))"
    )
    CustomerPayrollInfo CusPayrollViewToData(CustomerPayrollView payrollView) throws ParseException;

    @Mapping(
            target = "lastPayday",
            expression = "java(com.sztus.framework.component.core.util.DateUtil.timeStampToStr(payrollData.getLastPayday()," +
                    "com.sztus.framework.component.core.util.DateUtil.DEFAULT_DATE_PATTERN))"
    )
    SaveCustomerPayrollResponse CusPayrollToSaveCusPayrollResponse(CustomerPayrollInfo payrollData) throws ParseException;

    SaveCustomerEmploymentResponse CusEmploymentToToSaveCusEmploymentResponse(CustomerEmploymentInfo customerEmploymentInfo);

    GetCustomerByConditionResponse CustomerToGetCustomerByConditionResponse(Customer customer);

    GetCreditEvaluationResponse CustomerCreditEvaluationToView(CustomerCreditEvaluation customerCreditEvaluation);

    CustomerCreditEvaluation CustomerCreditEvaluationViewToData(CustomerCreditEvaluationView creditEvaluationView);
}
