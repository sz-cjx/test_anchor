package com.sztus.azeroth.microservice.customer.server.converter;

import com.sztus.azeroth.microservice.customer.client.object.parameter.response.*;
import com.sztus.azeroth.microservice.customer.client.object.view.*;
import com.sztus.azeroth.microservice.customer.server.object.domain.*;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

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

    GetCustomerPayrollResponse CusPayrollToView(CustomerPayrollInfo payrollData);

    CustomerPayrollInfo CusPayrollViewToData(CustomerPayrollView payrollView);

    SaveCustomerPayrollResponse CusPayrollToSaveCusPayrollResponse(CustomerPayrollInfo payrollData);

    SaveCustomerEmploymentResponse CusEmploymentToToSaveCusEmploymentResponse(CustomerEmploymentInfo customerEmploymentInfo);

    GetCustomerByConditionResponse customerToGetCustomerByConditionResponse(Customer customer);
}
