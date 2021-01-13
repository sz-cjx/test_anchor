package com.arbfintech.microservice.customer.clientapi.api;

import com.alibaba.fastjson.JSONObject;
import com.arbfintech.framework.component.core.base.BaseClientApi;
import com.arbfintech.framework.component.core.type.AjaxResult;
import com.arbfintech.framework.component.core.type.MultiValueManager;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Fly_Roushan
 * @date 2020/12/21
 */
@Component
public class CustomerClientApi extends BaseClientApi {

    private static final String CUSTOMER_REST_API = "/customer-restapi";

    public AjaxResult createCustomer(JSONObject dataJson) {
        return simpleRestCaller.post(
                generateUrl(CUSTOMER_REST_API, "/customer/create"),
                dataJson
        );
    }

    public AjaxResult getCustomerByUnique(String ssn, String email, String routingNo, String accountNo) {
        return simpleRestCaller.get(
                generateUrl(CUSTOMER_REST_API, "/customer/unique"),
                new MultiValueManager()
                        .add("ssn", ssn)
                        .add("email", email)
                        .add("routingNo", routingNo)
                        .add("accountNo", accountNo)
                        .getMap()
        );
    }

    public AjaxResult searchCustomer(Long customerId, String openId, String email) {
        return simpleRestCaller.get(
                generateUrl(CUSTOMER_REST_API, "/customer/search"),
                new MultiValueManager()
                        .add("customerId", customerId)
                        .add("email", email)
                        .add("openId", openId)
                        .getMap()
        );
    }

    public AjaxResult loadFeatures(Long customerId, List<String> features) {
        return simpleRestCaller.get(
                generateUrl(CUSTOMER_REST_API, "/customer/load-features"),
                new MultiValueManager()
                        .add("customerId", customerId)
                        .add("features", String.join(",", features))
                        .getMap()
        );
    }

    public AjaxResult updateFeatures(JSONObject dataJson) {
        return simpleRestCaller.put(
                generateUrl(CUSTOMER_REST_API, "/customer/update-features"),
                dataJson
        );
    }

    public AjaxResult updateCustomerProfile(JSONObject dataJson) {
        return simpleRestCaller.put(
                generateUrl(CUSTOMER_REST_API, "/customer/update"),
                dataJson
        );
    }
}
