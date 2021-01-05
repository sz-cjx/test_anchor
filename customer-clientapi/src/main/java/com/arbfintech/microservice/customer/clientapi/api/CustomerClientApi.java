package com.arbfintech.microservice.customer.clientapi.api;

import com.alibaba.fastjson.JSONObject;
import com.arbfintech.framework.component.core.base.BaseClientApi;
import com.arbfintech.framework.component.core.type.AjaxResult;
import com.arbfintech.framework.component.core.type.MultiValueManager;
import com.arbfintech.framework.component.core.type.ProcedureException;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Fly_Roushan
 * @date 2020/12/21
 */
@Component
public class CustomerClientApi extends BaseClientApi {

    private static final String CUSTOMER_REST_API = "/customer-restapi";

    public Long createCustomer(JSONObject dataJson) throws ProcedureException {
        AjaxResult ajaxResult = simpleRestTemplate.postByRequestBody(
                generateUrl(CUSTOMER_REST_API, "/customer/create"),
                dataJson,
                AjaxResult.class
        );
        return fetchResultDataObject(ajaxResult, Long.class);
    }

    public JSONObject searchCustomer(Long id, String email, List<String> features) throws ProcedureException {
        AjaxResult ajaxResult = simpleRestTemplate.getByQuery(
                generateUrl(CUSTOMER_REST_API, "/customer/search"),
                new MultiValueManager()
                        .add("id", id)
                        .add("email", email)
                        .add("features", features)
                        .getMap(),
                AjaxResult.class
        );
        return fetchResultDataObject(ajaxResult, JSONObject.class);
    }

    public String getCustomerOptInByCondition(String condition) {
        return simpleRestTemplate.postByRequestBody(
                generateUrl(CUSTOMER_REST_API, "/customer/opt-in/get"),
                condition,
                String.class
        );
    }

    public String updateCustomerOptInData(String dataStr) {
        return simpleRestTemplate.postByRequestBody(
                generateUrl(CUSTOMER_REST_API, "/customer/opt-in/update"),
                dataStr,
                String.class
        );
    }
}
