package com.arbfintech.microservice.customer.future.business;

import com.arbfintech.framework.component.core.type.AjaxResult;
import com.arbfintech.framework.component.core.type.ProcedureException;
import com.arbfintech.microservice.customer.object.entity.Customer;
import com.arbfintech.microservice.customer.object.enumerate.CustomerErrorCode;
import com.arbfintech.microservice.customer.service.CustomerOptInService;
import com.arbfintech.microservice.customer.service.CustomerService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

/**
 * @author Fly_Roushan
 * @date 2020/12/17
 */
@Component
public class CustomerFuture {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerFuture.class);

    @Autowired
    private CustomerService customerService;

    @Autowired
    private CustomerOptInService customerOptInService;

    public CompletableFuture<String> createCustomer(Customer customer) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Customer customerDb = customerService.checkCustomer(customer.getEmail(), customer.getSsn());
                if (Objects.nonNull(customerDb)) {
                    throw new ProcedureException(CustomerErrorCode.QUERY_FAILURE_CUSTOMER_IS_EXISTED);
                }
                customer.setStatus(-1);
                Long id = customerService.addCustomer(customer);

                if (id > 0) {
                    LOGGER.info("[Create Customer] Create customer success");
                } else {
                    LOGGER.warn("[Create Customer] Create customer failed");
                }
                Long row = customerOptInService.initCustomerOptIn(id);
                if (row > 0) {
                    LOGGER.info("[Create Customer] Init customer opt in success");
                } else {
                    LOGGER.warn("[Create Customer] Init customer opt in failed");
                }

                return AjaxResult.success(id);
            } catch (ProcedureException e) {
                LOGGER.warn(e.getMessage());
                return AjaxResult.failure(e);
            }
        });
    }

    public CompletableFuture<String> searchCustomer(String email) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                if (StringUtils.isAllBlank(email)) {
                    throw new ProcedureException(CustomerErrorCode.QUERY_FAILURE_SEARCH_FAILED);
                }

                Customer customer = customerService.searchCustomer(email);
                if (Objects.isNull(customer)) {
                    throw new ProcedureException(CustomerErrorCode.QUERY_FAILURE_CUSTOMER_NOT_EXISTED);
                }

                return AjaxResult.success(customer);
            } catch (ProcedureException e) {
                LOGGER.warn(e.getMessage());
                return AjaxResult.failure(e);
            }
        });
    }
}
