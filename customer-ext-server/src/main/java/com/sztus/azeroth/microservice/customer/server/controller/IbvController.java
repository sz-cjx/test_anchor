package com.sztus.azeroth.microservice.customer.server.controller;

import com.sztus.azeroth.microservice.customer.client.object.constant.CustomerAction;
import com.sztus.azeroth.microservice.customer.client.object.parameter.enumerate.CustomerErrorCode;
import com.sztus.azeroth.microservice.customer.client.object.parameter.request.ListIbvAuthorizationRequest;
import com.sztus.azeroth.microservice.customer.client.object.parameter.request.SaveIbvAuthorizationRequest;
import com.sztus.azeroth.microservice.customer.client.object.parameter.response.ListIbvAuthorizationResponse;
import com.sztus.azeroth.microservice.customer.client.object.parameter.response.SaveIbvAuthorizationResponse;
import com.sztus.azeroth.microservice.customer.client.object.view.IbvAuthorizationView;
import com.sztus.azeroth.microservice.customer.server.converter.CustomerIbvAuthorizationConverter;
import com.sztus.azeroth.microservice.customer.server.object.domain.CustomerIbvAuthorizationRecord;
import com.sztus.azeroth.microservice.customer.server.service.IbvService;
import com.sztus.framework.component.core.type.ProcedureException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

@RestController
public class IbvController {

    @Autowired
    private IbvService ibvService;

    @PostMapping(CustomerAction.SAVE_IBV_AUTHORIZATION)
    public SaveIbvAuthorizationResponse saveIbvAuthorization(
            @RequestBody SaveIbvAuthorizationRequest request
    ) throws ProcedureException {
        Long customerId = request.getCustomerId();
        if (Objects.isNull(customerId)) {
            throw new ProcedureException(CustomerErrorCode.PARAMETER_IS_INCOMPLETE);
        }

        CustomerIbvAuthorizationRecord ibvAuthorizationRecord = CustomerIbvAuthorizationConverter.INSTANCE.ibvRequestToData(request);
        ibvService.saveIbvAuthorization(ibvAuthorizationRecord);

        return CustomerIbvAuthorizationConverter.INSTANCE.ibvViewToResponse(ibvAuthorizationRecord);
    }

    @PostMapping(CustomerAction.LIST_IBV_AUTHORIZATION)
    public ListIbvAuthorizationResponse saveIbvAuthorization(
            @RequestBody ListIbvAuthorizationRequest request
    ) throws ProcedureException {
        Long customerId = request.getCustomerId();
        Long portfolioId = request.getPortfolioId();
        if (Objects.isNull(customerId)) {
            throw new ProcedureException(CustomerErrorCode.PARAMETER_IS_INCOMPLETE);
        }

        List<CustomerIbvAuthorizationRecord> ibvAuthorizationRecords = ibvService.listIbvAuthorization(customerId, portfolioId);
        List<IbvAuthorizationView> ibvAuthorizationViews = CustomerIbvAuthorizationConverter.INSTANCE.ibvDataListToView(ibvAuthorizationRecords);

        ListIbvAuthorizationResponse response = new ListIbvAuthorizationResponse();
        response.setCount(ibvAuthorizationViews.size());
        response.setItems(ibvAuthorizationViews);
        return response;
    }


}
