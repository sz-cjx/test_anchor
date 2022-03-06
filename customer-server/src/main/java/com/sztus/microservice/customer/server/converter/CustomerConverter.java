package com.sztus.microservice.customer.server.converter;

import com.sztus.microservice.customer.client.object.parameter.response.GetCustomerResponse;
import com.sztus.microservice.customer.server.domain.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CustomerConverter {

    GetCustomerResponse CustomerToGetCustomerResponse(Customer customer);

    CustomerConverter INSTANCE = Mappers.getMapper(CustomerConverter.class);

}
