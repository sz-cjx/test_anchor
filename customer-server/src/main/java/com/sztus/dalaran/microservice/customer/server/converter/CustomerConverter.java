package com.sztus.dalaran.microservice.customer.server.converter;

import com.sztus.dalaran.microservice.customer.client.object.parameter.response.*;
import com.sztus.dalaran.microservice.customer.client.object.view.CustomerPayrollView;
import com.sztus.dalaran.microservice.customer.client.object.parameter.request.SaveBankAccountRequest;
import com.sztus.dalaran.microservice.customer.client.object.parameter.response.*;
import com.sztus.dalaran.microservice.customer.client.object.view.CustomerBankAccountDataView;
import com.sztus.dalaran.microservice.customer.client.object.parameter.response.GetCustomerPersonalResponse;
import com.sztus.dalaran.microservice.customer.client.object.parameter.response.GetCustomerEmploymentResponse;
import com.sztus.dalaran.microservice.customer.client.object.parameter.response.GetCustomerResponse;
import com.sztus.dalaran.microservice.customer.client.object.view.CustomerPersonalView;
import com.sztus.dalaran.microservice.customer.client.object.parameter.response.SaveCustomerEmploymentResponse;
import com.sztus.dalaran.microservice.customer.client.object.parameter.response.SaveCustomerResponse;
import com.sztus.dalaran.microservice.customer.client.object.view.CustomerEmploymentView;
import com.sztus.dalaran.microservice.customer.client.object.view.CustomerPersonalView;
import com.sztus.dalaran.microservice.customer.client.object.view.CustomerView;
import com.sztus.dalaran.microservice.customer.server.domain.*;
import com.sztus.dalaran.microservice.customer.server.domain.Customer;
import com.sztus.dalaran.microservice.customer.server.domain.CustomerBankAccountData;
import com.sztus.dalaran.microservice.customer.server.domain.CustomerPersonalData;
import com.sztus.dalaran.microservice.customer.server.domain.CustomerEmploymentData;
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
