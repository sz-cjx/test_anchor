package com.arbfintech.microservice.customer.clientapi.api;

import com.arbfintech.framework.component.core.base.BaseClientApi;
import com.arbfintech.framework.component.core.type.AjaxResult;
import com.arbfintech.microservice.customer.object.dto.CustomerDocumentDTO;
import org.springframework.stereotype.Component;

@Component
public class CustomerDocumentClientApi extends BaseClientApi {

    private static final String CUSTOMER_REST_API = "/customer-restapi";

    public AjaxResult listDocument(CustomerDocumentDTO customerDocumentDTO) {
        return simpleRestCaller.post(
                generateUrl(CUSTOMER_REST_API, "/document"),
                customerDocumentDTO
        );
    }
}