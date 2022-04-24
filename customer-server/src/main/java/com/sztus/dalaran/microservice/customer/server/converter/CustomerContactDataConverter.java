package com.sztus.dalaran.microservice.customer.server.converter;

import com.sztus.dalaran.microservice.customer.client.object.view.CustomerContactDataView;
import com.sztus.dalaran.microservice.customer.server.object.domain.CustomerContactData;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface CustomerContactDataConverter {

    CustomerContactDataConverter INSTANCE = Mappers.getMapper(CustomerContactDataConverter.class);

    CustomerContactData CustomerContactViewToData(CustomerContactDataView customerContactDataView);

    List<CustomerContactDataView> ListCustomerContactDataToView(List<CustomerContactData> customerContactDataList);

    List<CustomerContactData> ListCustomerContactViewToDate(List<CustomerContactDataView> customerContactDataViewList);

}
