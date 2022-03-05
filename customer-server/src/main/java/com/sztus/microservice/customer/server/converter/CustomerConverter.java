package com.sztus.microservice.customer.server.converter;

import com.sztus.microservice.customer.client.object.parameter.response.GetCustomerByConditionsResponse;
import com.sztus.microservice.customer.server.domain.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CustomerConverter {

    GetCustomerByConditionsResponse convertCustomerToGetCustomerResponse(Customer customer);

    CustomerConverter INSTANCE = Mappers.getMapper(CustomerConverter.class);

}
