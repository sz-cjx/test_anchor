package com.arbfintech.microservice.customer.restapi.future;

import com.arbfintech.framework.component.core.type.AjaxResult;
import com.arbfintech.framework.component.core.type.ProcedureException;
import com.arbfintech.microservice.customer.object.entity.CustomerOptIn;
import com.arbfintech.microservice.customer.object.enumerate.CustomerErrorCode;
import com.arbfintech.microservice.customer.restapi.service.CustomerOptInService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
public class CustomerOptInFuture {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerOptInFuture.class);

    @Autowired
    private CustomerOptInService customerOptInService;

    public CompletableFuture<String> getCustomerOptInByCondition(String dataStr) {
        return CompletableFuture.supplyAsync(() -> {
            String ajaxResultStr;
            try {
                CustomerOptIn customerOptIn = customerOptInService.getCustomerOptInByCondition(dataStr);
                ajaxResultStr = AjaxResult.success(customerOptIn);
            } catch (ProcedureException e) {
                LOGGER.error(e.getMessage(), e);
                ajaxResultStr = AjaxResult.failure(CustomerErrorCode.QUERY_FAILURE_NO_DATA_WAS_QUERIED);
            }
            return ajaxResultStr;
        });
    }

}
