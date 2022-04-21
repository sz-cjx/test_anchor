package com.sztus.dalaran.microservice.customer.server.converter;

import com.sztus.dalaran.microservice.customer.client.object.parameter.request.SaveCustomerPersonalDataRequest;
import com.sztus.dalaran.microservice.customer.client.object.parameter.response.GetCustomerPersonalDataResponse;
import com.sztus.dalaran.microservice.customer.client.object.parameter.response.GetCustomerResponse;
import com.sztus.dalaran.microservice.customer.client.object.view.CustomerPersonalDataView;
import com.sztus.dalaran.microservice.customer.client.object.parameter.response.SaveCustomerResponse;
import com.sztus.dalaran.microservice.customer.client.object.view.CustomerView;
import com.sztus.dalaran.microservice.customer.server.domain.Customer;
import com.sztus.dalaran.microservice.customer.server.domain.CustomerPersonalData;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CustomerConverter {

    CustomerConverter INSTANCE = Mappers.getMapper(CustomerConverter.class);

    GetCustomerResponse CustomerToCustomerView(Customer customer);

    GetCustomerPersonalDataResponse PersonalDataToPersonalDataView(CustomerPersonalData customerPersonalData);

    CustomerPersonalData PersonalDataViewToPersonalData(CustomerPersonalDataView view);
    Customer CustomerViewToCustomer(CustomerView customerView);

    SaveCustomerResponse CustomerToSaveCustomerResponse(Customer customer);

}
