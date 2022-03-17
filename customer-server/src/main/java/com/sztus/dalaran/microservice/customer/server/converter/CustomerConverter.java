package com.sztus.dalaran.microservice.customer.server.converter;

import com.sztus.dalaran.microservice.customer.client.object.parameter.response.GetCustomerResponse;
import com.sztus.dalaran.microservice.customer.client.object.parameter.response.SaveCustomerResponse;
import com.sztus.dalaran.microservice.customer.client.object.view.CustomerView;
import com.sztus.dalaran.microservice.customer.server.domain.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CustomerConverter {

    GetCustomerResponse CustomerToGetCustomerResponse(Customer customer);

    SaveCustomerResponse CustomerToSaveCustomerResponse(Customer customer);

    Customer CustomerViewToCustomer(CustomerView customerView);

    CustomerConverter INSTANCE = Mappers.getMapper(CustomerConverter.class);

}
