package com.arbfintech.microservice.customer.clientapi.api;

import com.arbfintech.framework.component.core.base.BaseClientApi;
import com.arbfintech.framework.component.core.type.AjaxResult;
import com.arbfintech.microservice.customer.object.constant.RequestConst;
import com.arbfintech.microservice.customer.object.dto.CustomerProfileDTO;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;

import javax.servlet.http.HttpServletRequest;


@Component
public class CustomerResourceClientApi extends BaseClientApi {

    private static final String CUSTOMER_REST_API = "/customer-restapi";

    public AjaxResult getProfileByFeature(CustomerProfileDTO profileDTO) {
        return simpleRestCaller.post(
                generateUrl(CUSTOMER_REST_API, "/profile"),
                profileDTO
        );
    }

    public AjaxResult saveProfileByFeature(CustomerProfileDTO profileDTO, HttpServletRequest request) {
        MultiValueMap<String, String> header = simpleRestCaller.getDefaultHttpHeader();
        header.set(RequestConst.ACCESS_TOKEN, request.getHeader(RequestConst.ACCESS_TOKEN));
        return simpleRestCaller.put(
                generateUrl(CUSTOMER_REST_API, "/profile"),
                header,
                profileDTO
        );
    }

    public AjaxResult getOperationLog(String dataStr) {
        return simpleRestCaller.post(
                generateUrl(CUSTOMER_REST_API, "/operation-log"),
                dataStr
        );
    }
}
