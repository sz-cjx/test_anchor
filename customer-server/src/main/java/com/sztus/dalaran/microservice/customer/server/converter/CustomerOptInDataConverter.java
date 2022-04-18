package com.sztus.dalaran.microservice.customer.server.converter;

import com.sztus.dalaran.microservice.customer.client.object.view.CustomerOptInDataView;
import com.sztus.dalaran.microservice.customer.server.domain.CustomerOptInData;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface CustomerOptInDataConverter {

    List<CustomerOptInDataView> CustomerOptInDataListToView(List<CustomerOptInData> customerOptInDataList);

    CustomerOptInDataConverter INSTANCE = Mappers.getMapper(CustomerOptInDataConverter.class);

}
