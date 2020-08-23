package com.arbfintech.microservice.customer.clientapi.future;

import com.alibaba.fastjson.JSON;
import com.arbfintech.framework.component.core.constant.ConditionTypeConst;
import com.arbfintech.framework.component.core.type.AjaxResult;
import com.arbfintech.framework.component.core.type.ProcedureException;
import com.arbfintech.framework.component.core.type.SqlOption;
import com.arbfintech.microservice.customer.domain.entity.Customer;
import com.arbfintech.microservice.customer.domain.entity.CustomerOptInData;
import com.arbfintech.microservice.customer.clientapi.procedure.CustomerProcedure;
import com.arbfintech.microservice.customer.object.enumerate.CustomerErrorCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;

/**
 * @author Baron
 */
@Component
public class CustomerClientFuture {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerClientFuture.class);

    @Autowired
    private CustomerProcedure customerProcedure;

    public String getCustomerByEmail(String email) {
        SqlOption sqlOption = SqlOption.getInstance();
        sqlOption.addWhere("email = :email", ConditionTypeConst.AND, "email", email);

        Customer customer = customerProcedure.getCustomerByOptions(sqlOption.toString());

        if (customer != null) {
            return AjaxResult.success(customer);
        } else {
            return AjaxResult.failure(CustomerErrorCode.CUSTOMER_IS_NOT_EXISTED);
        }
    }

    public String replaceCustomer(String dataStr) {
        Long resultCode = customerProcedure.replaceCustomer(dataStr);
        if (resultCode < CustomerErrorCode.SUCCESS.getCode()) {
            return AjaxResult.failure();
        }
        return AjaxResult.success();
    }

    public String getCustomerById(Long id) {
        Customer customer = customerProcedure.getCustomerById(id);
        if(Objects.isNull(customer)) {
            LOGGER.warn("[Get Customer]Customer is not found. CustomerId:{}", id);
            return AjaxResult.result(CustomerErrorCode.UNKNOWN);
        }
        return AjaxResult.success(customer);
    }

    public String listCustomerBySsnOrEmailOrNo(String conditionStr) {
        List<Customer> customerList = customerProcedure.listCustomerByOptions(conditionStr);
        if(CollectionUtils.isEmpty(customerList)) {
            return AjaxResult.result(CustomerErrorCode.UNKNOWN);
        }
        return AjaxResult.success(customerList);
    }

    public String getCustomerByCondition(String conditionStr) {
        Customer customer = customerProcedure.getCustomerByOptions(conditionStr);
        if (Objects.isNull(customer)) {
            return AjaxResult.result(CustomerErrorCode.UNKNOWN);
        }
        return AjaxResult.success(customer);
    }

    public String getCustomerOptInDataByCustomerId(Long customerId) {
        CustomerOptInData customerOptInData = customerProcedure.getCustomerOptInDataByCustomerId(customerId);
        if (Objects.isNull(customerOptInData)) {
            LOGGER.warn("[Get Opt In Data]Customer Opt-In Data is not found. CustomerId:{}", customerId);
            return AjaxResult.result(CustomerErrorCode.UNKNOWN);
        }
        return AjaxResult.success(customerOptInData);
    }

    public String addCustomerOptInData(Long customerId, String dataStr) {
        try {
            CustomerOptInData optInData = JSON.parseObject(dataStr, CustomerOptInData.class);
            if(Objects.isNull(optInData)) {
                LOGGER.warn("[Add Opt In Data]Failure: Lack of input data. CustomerId:{}", customerId);
                throw new ProcedureException(CustomerErrorCode.FAILURE_LACK_OF_INPUT_DATA);
            }
            optInData.setCustomerId(customerId);

            Long primaryKey = customerProcedure.addCustomerOptInData(optInData);
            if(primaryKey < CustomerErrorCode.SUCCESS.getCode()) {
                LOGGER.warn("[Add Opt In Data]Failed to update opt in data. CustomerId:{}", customerId);
                throw new ProcedureException(CustomerErrorCode.OPT_IN_DATA_FAILED_TO_SAVE_DATA);
            }

            return AjaxResult.success(primaryKey);
        } catch (ProcedureException e) {
            return AjaxResult.failure(e);
        }
    }

    public String updateCustomerOptInData(Long customerId, String dataStr) {
        try {
            CustomerOptInData optInData = JSON.parseObject(dataStr, CustomerOptInData.class);
            if(Objects.isNull(optInData)) {
                LOGGER.warn("[Update Opt In Data]Failure: Lack of input data. CustomerId:{}", customerId);
                throw new ProcedureException(CustomerErrorCode.FAILURE_LACK_OF_INPUT_DATA);
            }
            optInData.setCustomerId(customerId);

            Long affectedCount = customerProcedure.updateCustomerOptInData(optInData);
            if(affectedCount < CustomerErrorCode.SUCCESS.getCode()) {
                LOGGER.warn("[Update Opt In Data]Failed to update opt in data. CustomerId:{}", customerId);
                throw new ProcedureException(CustomerErrorCode.OPT_IN_DATA_FAILED_TO_UPDATE_DATA);
            }

            return AjaxResult.success();
        } catch (ProcedureException e) {
            return AjaxResult.failure(e);
        }
    }
}
