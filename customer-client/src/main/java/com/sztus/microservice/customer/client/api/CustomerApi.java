package com.sztus.microservice.customer.client.api;

import com.sztus.framework.component.core.base.BaseClientApi;
import com.sztus.framework.component.core.component.SimpleRestCaller;
import com.sztus.framework.component.core.type.AjaxResult;
import com.sztus.framework.component.core.type.ProcedureException;
import com.sztus.microservice.customer.client.constant.CustomerAction;
import com.sztus.microservice.customer.client.constant.Re;
import com.sztus.microservice.customer.client.object.parameter.request.GetCustomerAccountByConditionsRequest;
import com.sztus.microservice.customer.client.object.parameter.request.GetCustomerByConditionsRequest;
import com.sztus.microservice.customer.client.object.parameter.response.GetCustomerAccountByConditionsResponse;
import com.sztus.microservice.customer.client.object.parameter.response.GetCustomerByConditionsResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CustomerApi extends BaseClientApi {

    public GetCustomerByConditionsResponse getCustomerByConditions(GetCustomerByConditionsRequest request) throws ProcedureException {
        AjaxResult ajaxResult = restCaller.get(
                generateUrl(Re.ROOT, CustomerAction.GET_CUSTOMER_BY_CONDITIONS),
                request
        );

        return fetchResultDataObject(ajaxResult, GetCustomerByConditionsResponse.class);
    }

    @Autowired
    private SimpleRestCaller restCaller;
}
