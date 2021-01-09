package com.arbfintech.microservice.customer.clientapi.api;

import com.alibaba.fastjson.JSONObject;
import com.arbfintech.framework.component.core.base.BaseClientApi;
import com.arbfintech.framework.component.core.type.AjaxResult;
import com.arbfintech.framework.component.core.type.HttpParamVariable;
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
        AjaxResult ajaxResult = simpleRestCaller.post(
                generateUrl(CUSTOMER_REST_API, "/customer/create"),
                dataJson
        );
        return fetchResultDataObject(ajaxResult, Long.class);
    }


    public JSONObject searchCustomer(String openId, String email) throws ProcedureException {
        AjaxResult ajaxResult = simpleRestCaller.get(
                generateUrl(CUSTOMER_REST_API, "/customer/search"),
                new MultiValueManager()
                        .add("email", email)
                        .add("openId", openId)
                        .getMap()
        );
        return fetchResultDataObject(ajaxResult, JSONObject.class);
    }

    public String loadFeatures(Long customerId, List<String> features) throws ProcedureException {
        AjaxResult ajaxResult = simpleRestCaller.get(
                generateUrl(CUSTOMER_REST_API, "/customer/load-features"),
                new MultiValueManager()
                        .add("customerId", customerId)
                        .add("features", features)
                        .getMap()
        );
        checkAjaxResult(ajaxResult);
        return ajaxResult.getDataString();
    }

    public Integer updateFeatures(Long customerId, List<String> features, String data) throws ProcedureException {
        AjaxResult ajaxResult = simpleRestCaller.put(
                generateUrl(CUSTOMER_REST_API, "/customer/update-features"),
                MultiValueManager.getBean()
                        .add("customerId", customerId)
                        .add("features", features)
                        .add("data", data)
        );
        return checkAjaxResult(ajaxResult);
    }

    /**
     * customerOptIn
     */

    public String getCustomerOptInByCondition(String condition) throws ProcedureException {
        AjaxResult ajaxResult = simpleRestCaller.post(
                generateUrl(CUSTOMER_REST_API, "/customer/opt-in/get"),
                condition
        );
        checkAjaxResult(ajaxResult);
        return ajaxResult.getDataString();
    }

    public Integer updateCustomerOptInData(String dataStr) throws ProcedureException {
        AjaxResult ajaxResult = simpleRestCaller.put(
                generateUrl(CUSTOMER_REST_API, "/customer/opt-in/update"),
                dataStr
        );
        return checkAjaxResult(ajaxResult);
    }

    public String listCustomerOptInData(Long customerId) throws ProcedureException {
        AjaxResult ajaxResult = simpleRestCaller.get(
                generateUrl(CUSTOMER_REST_API, "/customer/opt-in/list"),
                HttpParamVariable.getInstance()
                        .addParam("customerId", customerId)
                        .getParamMap()
        );
        checkAjaxResult(ajaxResult);
        return ajaxResult.getDataString();
    }

    public Integer update(JSONObject dataJson) throws ProcedureException {
        AjaxResult ajaxResult = simpleRestCaller.put(
                generateUrl(CUSTOMER_REST_API, "/customer/update"),
                dataJson
        );
        return checkAjaxResult(ajaxResult);
    }

}
