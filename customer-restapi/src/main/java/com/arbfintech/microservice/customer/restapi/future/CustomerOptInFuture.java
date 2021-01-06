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

import java.util.List;
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

    public CompletableFuture<String> listCustomerOptInData(Long customerId) {
        return CompletableFuture.supplyAsync(() -> {
            String ajaxResultStr;
            try {
                List<CustomerOptIn> customerOptInList = customerOptInService.listCustomerOptInData(customerId);
                ajaxResultStr = AjaxResult.success(customerOptInList);
            } catch (Exception e) {
                LOGGER.error(e.getMessage(), e);
                ajaxResultStr = AjaxResult.failure(CustomerErrorCode.QUERY_FAILURE_NO_DATA_WAS_QUERIED);
            }
            return ajaxResultStr;
        });
    }

    public CompletableFuture<String> updateCustomerOptInData(String dataStr) {
        return CompletableFuture.supplyAsync(() -> {
            String ajaxResultStr;
            try {
                customerOptInService.updateCustomerOptInData(dataStr);
                ajaxResultStr = AjaxResult.success();
            } catch (Exception e) {
                LOGGER.warn("[Replace Customer Opt-In Data]Failed to update customer opt-in data. Request Parameters:{}", dataStr, e);
                ajaxResultStr = AjaxResult.failure();
            }
            return ajaxResultStr;
        });
    }

}
