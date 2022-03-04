package com.sztus.microservice.customer.server.converter;

import com.sztus.microservice.customer.client.object.parameter.response.GetCustomerByConditionsResponse;
import com.sztus.microservice.customer.client.object.view.CustomerAccountView;
import com.sztus.microservice.customer.server.domain.Customer;
import com.sztus.microservice.customer.server.domain.CustomerProfile;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CustomerConverter {

    GetCustomerByConditionsResponse convertCustomerToGetCustomerResponse(Customer customer);

    CustomerConverter INSTANCE = Mappers.getMapper(CustomerConverter.class);

}
