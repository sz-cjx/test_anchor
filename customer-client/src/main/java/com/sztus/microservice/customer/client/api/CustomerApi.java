package com.sztus.microservice.customer.client.api;

import com.sztus.framework.component.core.base.BaseClientApi;
import com.sztus.framework.component.core.component.SimpleRestCaller;
import com.sztus.framework.component.core.type.AjaxResult;
import com.sztus.framework.component.core.type.ProcedureException;
import com.sztus.microservice.customer.client.object.business.CustomerAction;
import com.sztus.microservice.customer.client.object.business.Re;
import com.sztus.microservice.customer.client.object.parameter.request.GetCustomerRequest;
import com.sztus.microservice.customer.client.object.parameter.response.GetCustomerResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CustomerApi extends BaseClientApi {

    public GetCustomerResponse getCustomer(GetCustomerRequest request) throws ProcedureException {
        AjaxResult ajaxResult = restCaller.get(
                generateUrl(Re.ROOT, CustomerAction.GET_CUSTOMER),
                request
        );

        return fetchResultDataObject(ajaxResult, GetCustomerResponse.class);
    }

    @Autowired
    private SimpleRestCaller restCaller;
}
