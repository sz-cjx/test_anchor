package com.sztus.microservice.customer.server.converter;

import com.sztus.microservice.customer.client.object.parameter.response.GetCustomerByConditionsResponse;
import com.sztus.microservice.customer.client.object.view.CustomerAccountView;
import com.sztus.microservice.customer.server.domain.CustomerAccountData;
import com.sztus.microservice.customer.server.domain.CustomerProfile;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CustomerMapper {

    CustomerAccountData convertViewToCustomer(CustomerAccountView customerAccountView);

    GetCustomerByConditionsResponse convertCustomerProfileToResponse(CustomerProfile customerProfile);

    CustomerAccountView convertCustomerToView(CustomerAccountData customerAccountData);

    CustomerMapper INSTANCE = Mappers.getMapper(CustomerMapper.class);

}
