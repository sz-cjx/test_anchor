package com.sztus.dalaran.microservice.customer.server.converter;

import com.sztus.dalaran.microservice.customer.client.object.parameter.response.GetCustomerContactDataResponse;
import com.sztus.dalaran.microservice.customer.client.object.view.CustomerContactInfoView;
import com.sztus.dalaran.microservice.customer.server.object.domain.CustomerContactInfo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface CustomerContactDataConverter {

    CustomerContactDataConverter INSTANCE = Mappers.getMapper(CustomerContactDataConverter.class);

    CustomerContactInfo CustomerContactViewToData(CustomerContactInfoView customerContactInfoView);

    GetCustomerContactDataResponse CustomerContactDataToView(CustomerContactInfo customerContactInfo);

    List<CustomerContactInfoView> ListCustomerContactDataToView(List<CustomerContactInfo> customerContactInfoList);

    List<CustomerContactInfo> ListCustomerContactViewToDate(List<CustomerContactInfoView> customerContactInfoViewList);

}
