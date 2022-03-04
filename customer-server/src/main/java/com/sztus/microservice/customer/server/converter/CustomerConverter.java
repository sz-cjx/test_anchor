package com.sztus.microservice.customer.server.converter;

import com.sztus.microservice.customer.client.object.parameter.response.GetCustomerAccountByConditionsResponse;
import com.sztus.microservice.customer.client.object.parameter.response.GetCustomerByConditionsResponse;
import com.sztus.microservice.customer.client.object.view.CustomerAccountView;
import com.sztus.microservice.customer.server.domain.CustomerAccountData;
import com.sztus.microservice.customer.server.domain.CustomerProfile;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CustomerConverter {

    CustomerAccountData convertViewToCustomer(CustomerAccountView customerAccountView);

    GetCustomerByConditionsResponse convertCustomerProfileToResponse(CustomerProfile customerProfile);

    GetCustomerAccountByConditionsResponse convertCustomerAccountToResponse(CustomerAccountData customerAccountData);

    CustomerAccountView convertCustomerToView(CustomerAccountData customerAccountData);

    CustomerConverter INSTANCE = Mappers.getMapper(CustomerConverter.class);

}
