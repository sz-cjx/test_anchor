package com.arbfintech.microservice.customer.clientapi.api;

import com.arbfintech.framework.component.core.base.BaseClientApi;
import com.arbfintech.framework.component.core.type.AjaxResult;
import com.arbfintech.framework.component.core.type.HttpParamVariable;
import com.arbfintech.microservice.customer.object.constant.CustomerJsonKey;
import com.arbfintech.microservice.customer.object.dto.ActivateAccountDTO;
import com.arbfintech.microservice.customer.object.dto.CustomerAccountDTO;
import com.arbfintech.microservice.customer.object.dto.CustomerAccountPasswordDTO;
import org.springframework.stereotype.Component;

@Component
public class CustomerAccountClientApi extends BaseClientApi {

    private static final String CUSTOMER_REST_API = "/customer-restapi";

    public AjaxResult getAccountInfo(Long id) {
        return simpleRestCaller.get(
                generateUrl(CUSTOMER_REST_API, "/account"),
                HttpParamVariable.getInstance().addParam(CustomerJsonKey.ID, id).getParamMap()
        );
    }

    public AjaxResult saveAccountInfo(CustomerAccountDTO customerAccountDTO) {
        return simpleRestCaller.post(
                generateUrl(CUSTOMER_REST_API, "/account"),
                customerAccountDTO
        );
    }

    public AjaxResult changePassword(CustomerAccountPasswordDTO customerAccountPasswordDTO) {
        return simpleRestCaller.post(
                generateUrl(CUSTOMER_REST_API, "/change-password"),
                customerAccountPasswordDTO
        );
    }

    public AjaxResult activateAccount(ActivateAccountDTO activateAccountDTO) {
        return simpleRestCaller.post(
                generateUrl(CUSTOMER_REST_API, "/activate-account"),
                activateAccountDTO
        );
    }

    public AjaxResult signIn(ActivateAccountDTO activateAccountDTO) {
        return simpleRestCaller.post(
                generateUrl(CUSTOMER_REST_API, "/sign-in"),
                activateAccountDTO
        );
    }
}
