package com.sztus.dalaran.microservice.customer.server.converter;

import com.sztus.dalaran.microservice.customer.client.object.parameter.request.SaveBankAcountRequest;
import com.sztus.dalaran.microservice.customer.client.object.parameter.response.*;
import com.sztus.dalaran.microservice.customer.client.object.view.CustomerBankAccountDataView;
import com.sztus.dalaran.microservice.customer.client.object.view.CustomerPersonalView;
import com.sztus.dalaran.microservice.customer.client.object.view.CustomerView;
import com.sztus.dalaran.microservice.customer.server.domain.Customer;
import com.sztus.dalaran.microservice.customer.server.domain.CustomerBankAccountData;
import com.sztus.dalaran.microservice.customer.server.domain.CustomerPersonalData;
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

    SaveBankAcountResponse BankAccountDataToSaveResponse(CustomerBankAccountData bankAccountData);

    SaveCustomerPersonalResponse PersonalDataToSaveResponse(CustomerPersonalData personalData);
}
