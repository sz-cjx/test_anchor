package com.arbfintech.microservice.customer.clientapi.api;

import com.arbfintech.framework.component.core.base.BaseClientApi;
import com.arbfintech.framework.component.core.type.AjaxResult;
import com.arbfintech.framework.component.core.type.HttpParamVariable;
import com.arbfintech.microservice.customer.object.constant.CustomerJsonKey;
import com.arbfintech.microservice.customer.object.dto.ActivateAccountDTO;
import com.arbfintech.microservice.customer.object.dto.CustomerAccountDTO;
import com.arbfintech.microservice.customer.object.dto.CustomerAccountPasswordDTO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class CustomerAccountClientApi extends BaseClientApi {

    private static final String CUSTOMER_REST_API = "/customer-restapi";

    public AjaxResult getAccountInfo(Long id, String accountId) {
        HttpParamVariable httpParamVariable = HttpParamVariable.getInstance();
        if (Objects.nonNull(id)) {
            httpParamVariable.addParam(CustomerJsonKey.ID, id);
        }
        if (StringUtils.isNotBlank(accountId)) {
            httpParamVariable.addParam(CustomerJsonKey.ACCOUNT_ID, accountId);
        }
        return simpleRestCaller.get(
                generateUrl(CUSTOMER_REST_API, "/account"),
                httpParamVariable.getParamMap()
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

    public AjaxResult forgotPassword(CustomerAccountPasswordDTO customerAccountPasswordDTO) {
        return simpleRestCaller.post(
                generateUrl(CUSTOMER_REST_API, "/forgot-password"),
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
