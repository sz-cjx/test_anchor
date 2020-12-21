package com.arbfintech.microservice.customer.clientapi.api;

import com.arbfintech.framework.component.core.base.BaseClientApi;
import com.arbfintech.framework.component.core.type.AjaxResult;
import com.arbfintech.framework.component.core.type.MultiValueManager;
import com.arbfintech.framework.component.core.type.ProcedureException;
import com.arbfintech.microservice.customer.object.entity.Customer;
import org.springframework.stereotype.Component;

/**
 * @author Fly_Roushan
 * @date 2020/12/21
 */
@Component
public class CustomerClientApi extends BaseClientApi {

    private static final String CUSTOMER_REST_API = "/customer-restapi";

    public Long createCustomer(Customer customer) throws ProcedureException {
        AjaxResult ajaxResult = simpleRestTemplate.postByRequestBody(
                generateUrl(CUSTOMER_REST_API, "/business/operation/create-customer"),
                customer,
                AjaxResult.class
        );
        return fetchResultDataObject(ajaxResult, Long.class);
    }

    public Long searchCustomer(String email) throws ProcedureException {
        AjaxResult ajaxResult = simpleRestTemplate.getByQuery(
                generateUrl(CUSTOMER_REST_API, "/business/operation/search-customer"),
                new MultiValueManager().add("email", email).getMap(),
                AjaxResult.class
        );
        return fetchResultDataObject(ajaxResult, Long.class);
    }
}
