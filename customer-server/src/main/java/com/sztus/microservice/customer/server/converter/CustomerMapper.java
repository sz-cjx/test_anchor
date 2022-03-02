package com.sztus.microservice.customer.server.converter;

import com.sztus.microservice.customer.client.object.view.CustomerAccountView;
import com.sztus.microservice.customer.server.domain.CustomerAccountData;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CustomerMapper {

    CustomerAccountData convertViewToCustomer(CustomerAccountView customerAccountView);

    CustomerAccountView convertCustomerToView(CustomerAccountData customerAccountData);

    CustomerMapper INSTANCE = Mappers.getMapper(CustomerMapper.class);

}
