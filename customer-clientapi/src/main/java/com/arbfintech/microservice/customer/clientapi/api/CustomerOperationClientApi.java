package com.arbfintech.microservice.customer.clientapi.api;

import com.arbfintech.framework.component.core.base.BaseClientApi;
import com.arbfintech.framework.component.core.type.AjaxResult;
import com.arbfintech.framework.component.core.type.HttpParamVariable;
import com.arbfintech.microservice.customer.object.constant.CustomerJsonKey;
import org.springframework.stereotype.Component;

@Component
public class CustomerOperationClientApi extends BaseClientApi {

    private static final String CUSTOMER_REST_API = "/customer-restapi";

    public AjaxResult calculateLoanAmount(Long customerId) {
        return simpleRestCaller.get(
                generateUrl(CUSTOMER_REST_API, "/loan-amount/calculate"),
                HttpParamVariable.getInstance().addParam(CustomerJsonKey.CUSTOMER_ID, customerId).getParamMap()
        );
    }
}
