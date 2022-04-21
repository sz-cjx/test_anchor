package com.sztus.dalaran.microservice.customer.server.converter;

import com.sztus.dalaran.microservice.customer.client.object.parameter.response.*;
import com.sztus.dalaran.microservice.customer.client.object.view.*;
import com.sztus.dalaran.microservice.customer.server.domain.*;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface CustomerConverter {

    CustomerConverter INSTANCE = Mappers.getMapper(CustomerConverter.class);

    GetCustomerResponse CustomerToCustomerView(Customer customer);

    Customer CustomerViewToCustomer(CustomerView customerView);

    SaveCustomerResponse CustomerToSaveCustomerResponse(Customer customer);

    GetCustomerPersonalResponse PersonalToPersonalView(CustomerPersonalData customerPersonalData);

    CustomerPersonalData PersonalViewToPersonal(CustomerPersonalView view);

    List<CustomerBankAccountDataView> BankAccountListToViewList(List<CustomerBankAccountData> list);

    CustomerBankAccountData ViewToBankAccountData(CustomerBankAccountDataView view);

    CustomerBankAccountDataView BankAccountDataToView(CustomerBankAccountData bankAccountData);

    SaveCustomerPersonalResponse PersonalDataToSaveResponse(CustomerPersonalData personalData);

    GetCustomerEmploymentResponse CusEmploymentToView(CustomerEmploymentData employmentData);

    CustomerEmploymentData CusEmploymentViewToData(CustomerEmploymentView customerEmploymentView);

    SaveCustomerEmploymentResponse CusEmploymentToSaveCusEmploymentResponse(CustomerEmploymentData customerEmploymentData);

    GetCustomerPayrollResponse CusPayrollToView(CustomerPayrollData payrollData);

    CustomerPayrollData CusPayrollViewToData(CustomerPayrollView payrollView);

    SaveCustomerPayrollResponse CusPayrollToSaveCusPayrollResponse(CustomerPayrollData payrollData);

    SaveCustomerEmploymentResponse CusEmploymentToToSaveCusEmploymentResponse(CustomerEmploymentData customerEmploymentData);
}
