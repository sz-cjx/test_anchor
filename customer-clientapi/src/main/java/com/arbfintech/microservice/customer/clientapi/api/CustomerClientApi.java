package com.arbfintech.microservice.customer.clientapi.api;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.arbfintech.framework.component.core.base.BaseClientApi;
import com.arbfintech.framework.component.core.type.AjaxResult;
import com.arbfintech.framework.component.core.type.MultiValueManager;
import com.arbfintech.framework.component.core.type.ProcedureException;
import com.arbfintech.microservice.customer.object.constant.CustomerJsonKey;
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

    public JSONObject getCustomerByUnique(String ssn, String email, String routingNo, String accountNo) throws ProcedureException {
        AjaxResult ajaxResult = simpleRestCaller.get(
                generateUrl(CUSTOMER_REST_API, "/customer/unique"),
                new MultiValueManager()
                        .add("ssn", ssn)
                        .add("email", email)
                        .add("routingNo", routingNo)
                        .add("accountNo", accountNo)
                        .getMap()
        );
        return fetchResultDataObject(ajaxResult, JSONObject.class);
    }

    public JSONArray searchCustomer(String openId, String email) throws ProcedureException {
        AjaxResult ajaxResult = simpleRestCaller.get(
                generateUrl(CUSTOMER_REST_API, "/customer/search"),
                new MultiValueManager()
                        .add("email", email)
                        .add("openId", openId)
                        .getMap()
        );
        return fetchResultDataObject(ajaxResult, JSONArray.class);
    }

    public String loadFeatures(Long customerId, List<String> features) throws ProcedureException {
        AjaxResult ajaxResult = simpleRestCaller.get(
                generateUrl(CUSTOMER_REST_API, "/customer/load-features"),
                new MultiValueManager()
                        .add("customerId", customerId)
                        .add("features", String.join(",", features))
                        .getMap()
        );
        checkAjaxResult(ajaxResult);
        return ajaxResult.getDataString();
    }

    public Integer updateFeatures(JSONObject dataJson) throws ProcedureException {
        AjaxResult ajaxResult = simpleRestCaller.put(
                generateUrl(CUSTOMER_REST_API, "/customer/update-features"),
                dataJson
        );
        return checkAjaxResult(ajaxResult);
    }

    public Integer updateCustomerProfile(JSONObject dataJson) throws ProcedureException {
        AjaxResult ajaxResult = simpleRestCaller.put(
                generateUrl(CUSTOMER_REST_API, "/customer/update"),
                dataJson
        );
        return checkAjaxResult(ajaxResult);
    }
}
