package com.sztus.azeroth.microservice.customer.server.converter;

import com.sztus.azeroth.microservice.customer.client.object.parameter.request.SaveIbvAuthorizationRequest;
import com.sztus.azeroth.microservice.customer.client.object.parameter.response.SaveIbvAuthorizationResponse;
import com.sztus.azeroth.microservice.customer.client.object.view.IbvAuthorizationView;
import com.sztus.azeroth.microservice.customer.server.object.domain.CustomerIbvAuthorizationRecord;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface CustomerIbvAuthorizationConverter {

    CustomerIbvAuthorizationConverter INSTANCE = Mappers.getMapper(CustomerIbvAuthorizationConverter.class);

    CustomerIbvAuthorizationRecord ibvRequestToData(SaveIbvAuthorizationRequest ibvAuthorizationRequest);

    SaveIbvAuthorizationResponse ibvViewToResponse(CustomerIbvAuthorizationRecord ibvAuthorizationRecord);

    List<IbvAuthorizationView> ibvDataListToView(List<CustomerIbvAuthorizationRecord> ibvAuthorizationRecordList);

}
