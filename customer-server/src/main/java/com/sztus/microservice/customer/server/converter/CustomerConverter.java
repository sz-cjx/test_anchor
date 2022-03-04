package com.sztus.microservice.customer.server.converter;

import com.sztus.microservice.customer.client.object.parameter.response.GetCustomerByConditionsResponse;
import com.sztus.microservice.customer.client.object.view.CustomerAccountView;
import com.sztus.microservice.customer.server.domain.Customer;
import com.sztus.microservice.customer.server.domain.CustomerProfile;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CustomerConverter {

    CustomerProfile convertViewToCustomer(CustomerAccountView customerAccountView);

    GetCustomerByConditionsResponse convertCustomerProfileToResponse(Customer customer);

    CustomerAccountView convertCustomerToView(CustomerProfile customerProfile);

    CustomerConverter INSTANCE = Mappers.getMapper(CustomerConverter.class);

}
