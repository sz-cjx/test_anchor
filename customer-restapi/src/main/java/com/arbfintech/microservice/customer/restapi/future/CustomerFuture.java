package com.arbfintech.microservice.customer.restapi.future;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.arbfintech.framework.component.core.type.AjaxResult;
import com.arbfintech.framework.component.core.type.ProcedureException;
import com.arbfintech.framework.component.core.type.SqlOption;
import com.arbfintech.framework.component.database.core.SimpleService;
import com.arbfintech.microservice.customer.object.constant.CustomerFeatureConst;
import com.arbfintech.microservice.customer.object.constant.JsonKeyConst;
import com.arbfintech.microservice.customer.object.entity.Customer;
import com.arbfintech.microservice.customer.object.entity.CustomerOptIn;
import com.arbfintech.microservice.customer.object.entity.CustomerProfile;
import com.arbfintech.microservice.customer.object.enumerate.CustomerErrorCode;
import com.arbfintech.microservice.customer.restapi.service.CustomerOptInService;
import com.arbfintech.microservice.customer.restapi.service.CustomerProfileService;
import com.arbfintech.microservice.customer.restapi.util.CustomerUtil;
import com.arbfintech.microservice.customer.restapi.util.ResultUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
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
    private CustomerOptInService customerOptInService;

    @Autowired
    private CustomerProfileService customerProfileService;

    @Autowired
    private SimpleService simpleService;

    public CompletableFuture<String> createCustomer(JSONObject dataJson) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                if (!CustomerUtil.isValid(dataJson)) {
                    throw new ProcedureException(CustomerErrorCode.QUERY_FAILURE_MISS_REQUIRED_PARAM);
                }

                String uniqueCode = CustomerUtil.generateUniqueCode(
                        dataJson.getString(JsonKeyConst.SSN),
                        dataJson.getString(JsonKeyConst.EMAIL),
                        dataJson.getString(JsonKeyConst.BANK_ROUTING_NO),
                        dataJson.getString(JsonKeyConst.BANK_ACCOUNT_NO)
                );

                Customer customerDb = simpleService.findByOptions(Customer.class,
                        SqlOption.getInstance().whereEqual("unique_code", uniqueCode, null).toString()
                );

                if (Objects.nonNull(customerDb)) {
                    throw new ProcedureException(CustomerErrorCode.QUERY_FAILURE_CUSTOMER_IS_EXISTED);
                }
                Customer customer = dataJson.toJavaObject(Customer.class);
                CustomerProfile customerProfile = dataJson.toJavaObject(CustomerProfile.class);

                customer.setUniqueCode(uniqueCode);
                customer.setStatus(-1);

                Long customerId = simpleService.save(customer);
                ResultUtil.checkResult(customerId, CustomerErrorCode.CREATE_FAILURE_CUSTOMER_SAVE);

                ResultUtil.checkResult(simpleService.save(customerProfile), CustomerErrorCode.CREATE_FAILURE_CUSTOMER_PROFILE_SAVE);
                ResultUtil.checkResult(customerOptInService.initCustomerOptIn(customerId), CustomerErrorCode.CREATE_FAILURE_OPT_IN_SAVE);

                return AjaxResult.success(customerId);
            } catch (ProcedureException e) {
                LOGGER.warn(e.getMessage());
                return AjaxResult.failure(e);
            }
        });
    }

    public CompletableFuture<String> searchCustomer(Long id, String email, List<String> features) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                if (StringUtils.isAllBlank(String.valueOf(id), email)) {
                    throw new ProcedureException(CustomerErrorCode.QUERY_FAILURE_SEARCH_FAILED);
                }
                JSONObject resultJson = new JSONObject();
                CustomerProfile customerProfile = customerProfileService.searchCustomerProfile(id, email);
                if (Objects.isNull(customerProfile)) {
                    throw new ProcedureException(CustomerErrorCode.QUERY_FAILURE_CUSTOMER_NOT_EXISTED);
                }
                cascadeFeatureByCustomerId(customerProfile.getId(), features, resultJson);
                return AjaxResult.success(resultJson);
            } catch (ProcedureException e) {
                LOGGER.warn(e.getMessage());
                return AjaxResult.failure(e);
            }
        });
    }

    private void cascadeFeatureByCustomerId(Long customerId, Collection<String> featureArray, JSONObject dataJson) {
        SqlOption sqlOption = SqlOption.getInstance();
        sqlOption.whereEqual("id", customerId, null);
        for (String feature : featureArray) {
            switch (feature) {
                case CustomerFeatureConst.CUSTOMER:
                    Customer customer = simpleService.findByOptions(Customer.class, sqlOption.toString());
                    dataJson.putAll(JSON.parseObject(JSON.toJSONString(customer)));
                    break;
                case CustomerFeatureConst.OPT_IN:
                    CustomerOptIn customerOptIn = simpleService.findByOptions(CustomerOptIn.class, sqlOption.toString());
                    dataJson.putAll(JSON.parseObject(JSON.toJSONString(customerOptIn)));
                    break;
                case CustomerFeatureConst.PROFILE:
                    CustomerProfile customerProfile = simpleService.findByOptions(CustomerProfile.class, sqlOption.toString());
                    dataJson.putAll(JSON.parseObject(JSON.toJSONString(customerProfile)));
                    break;
                default:
                    LOGGER.warn("No such feature: {}", feature);
                    break;
            }
        }
    }
}
