package com.arbfintech.microservice.customer.restapi.future;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.arbfintech.framework.component.core.type.AjaxResult;
import com.arbfintech.framework.component.core.type.ProcedureException;
import com.arbfintech.framework.component.core.type.SqlOption;
import com.arbfintech.framework.component.database.core.SimpleService;
import com.arbfintech.microservice.customer.object.constant.CustomerFeatureKey;
import com.arbfintech.microservice.customer.object.constant.CustomerJsonKey;
import com.arbfintech.microservice.customer.object.entity.Customer;
import com.arbfintech.microservice.customer.object.entity.CustomerOptIn;
import com.arbfintech.microservice.customer.object.entity.CustomerProfile;
import com.arbfintech.microservice.customer.object.enumerate.CustomerErrorCode;
import com.arbfintech.microservice.customer.object.enumerate.CustomerOptInType;
import com.arbfintech.microservice.customer.restapi.repository.CustomerReader;
import com.arbfintech.microservice.customer.restapi.service.CustomerOptInService;
import com.arbfintech.microservice.customer.restapi.service.CustomerProfileService;
import com.arbfintech.microservice.customer.restapi.util.CustomerUtil;
import com.arbfintech.microservice.customer.restapi.util.ResultUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

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

    @Autowired
    private CustomerReader customerReader;

    public CompletableFuture<String> createCustomer(JSONObject dataJson) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                if (!CustomerUtil.isValid(dataJson)) {
                    throw new ProcedureException(CustomerErrorCode.QUERY_FAILURE_MISS_REQUIRED_PARAM);
                }

                String uniqueCode = CustomerUtil.generateUniqueCode(
                        dataJson.getString(CustomerJsonKey.SSN),
                        dataJson.getString(CustomerJsonKey.EMAIL),
                        dataJson.getString(CustomerJsonKey.BANK_ROUTING_NO),
                        dataJson.getString(CustomerJsonKey.BANK_ACCOUNT_NO)
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

    private void cascadeFeatureByCustomerId(Long customerId, Collection<String> featureArray, JSONObject dataJson) {
        if (featureArray == null) {
            return;
        }
        SqlOption sqlOption = SqlOption.getInstance();
        sqlOption.whereEqual("id", customerId, null);
        for (String feature : featureArray) {
            switch (feature) {
                case CustomerFeatureKey.CUSTOMER:
                    Customer customer = simpleService.findByOptions(Customer.class, sqlOption.toString());
                    dataJson.putAll(JSON.parseObject(JSON.toJSONString(customer)));
                    break;
                case CustomerFeatureKey.OPT_IN:
                    CustomerOptIn customerOptIn = simpleService.findByOptions(CustomerOptIn.class, sqlOption.toString());
                    dataJson.putAll(JSON.parseObject(JSON.toJSONString(customerOptIn)));
                    break;
                case CustomerFeatureKey.PROFILE:
                    CustomerProfile customerProfile = simpleService.findByOptions(CustomerProfile.class, sqlOption.toString());
                    dataJson.putAll(JSON.parseObject(JSON.toJSONString(customerProfile)));
                    break;
                default:
                    LOGGER.warn("No such feature: {}", feature);
                    break;
            }
        }
    }

    public CompletableFuture<String> searchCustomer(CustomerProfile customerProfile) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                JSONObject requestParam = JSON.parseObject(JSON.toJSONString(customerProfile));

                if (CollectionUtils.isEmpty(requestParam)) {
                    throw new ProcedureException(CustomerErrorCode.QUERY_FAILURE_SEARCH_FAILED);
                }

                JSONObject resultJson = customerProfileService.searchCustomerProfile(requestParam);
                if (CollectionUtils.isEmpty(resultJson)) {
                    throw new ProcedureException(CustomerErrorCode.QUERY_FAILURE_CUSTOMER_NOT_EXISTED);
                }
                return AjaxResult.success(resultJson);
            } catch (ProcedureException e) {
                LOGGER.warn(e.getMessage());
                return AjaxResult.failure(e);
            }
        });
    }

    public CompletableFuture<String> loadFeatures(Long id, List<String> features) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                if (StringUtils.isBlank(String.valueOf(id))) {
                    throw new ProcedureException(CustomerErrorCode.QUERY_FAILURE_CUSTOMER_IS_EXISTED);
                }
                JSONObject featureJson = new JSONObject();
                cascadeFeatureByCustomerId(id, features, featureJson);
                return AjaxResult.success(featureJson);
            } catch (ProcedureException e) {
                LOGGER.warn(e.getMessage());
                return AjaxResult.failure(e);
            }
        });
    }

    public CompletableFuture<String> updateFeatures(JSONObject dataJson) {
        return CompletableFuture.supplyAsync(() -> {
            if (Objects.isNull(dataJson)) {
                LOGGER.warn("DataStr is invalid");
                return AjaxResult.failure();
            }

            String openId = dataJson.getString(CustomerJsonKey.OPEN_ID);
            if (StringUtils.isBlank(openId)) {
                LOGGER.warn("Open id can't be null");
                return AjaxResult.failure();
            }

            Customer customerDb = simpleService.findByOptions(Customer.class, SqlOption.getInstance().whereEqual("open_id", openId, null).toString());
            if (Objects.isNull(customerDb)) {
                LOGGER.warn("Customer is not existed Open id:{}", openId);
                return AjaxResult.failure();
            }

            JSONArray featureArray = dataJson.getJSONArray("features");
            List<String> features = featureArray.toJavaList(String.class);
            if (features == null) {
                return AjaxResult.failure();
            }
            for (String feature : features) {
                switch (feature) {
                    case CustomerFeatureKey.OPT_IN:
                        JSONObject optIn = dataJson.getJSONObject(CustomerJsonKey.OPT_IN);
                        if (Objects.nonNull(optIn)) {
                            Long id = customerDb.getId();
                            Integer optInValue;
                            Integer optInType;
                            Integer emailOptInValue = optIn.getInteger(CustomerJsonKey.EMAIL);
                            Integer cellPhoneOptInValue = optIn.getInteger(CustomerJsonKey.CELL_PHONE);
                            Integer homePhoneOptInValue = optIn.getInteger(CustomerJsonKey.HOME_PHONE);

                            if (Objects.nonNull(emailOptInValue)) {
                                optInValue = emailOptInValue;
                                optInType = CustomerOptInType.EMAIL.getValue();
                            } else if (Objects.nonNull(cellPhoneOptInValue)) {
                                optInValue = cellPhoneOptInValue;
                                optInType = CustomerOptInType.CELL_PHONE.getValue();
                            } else {
                                optInValue = homePhoneOptInValue;
                                optInType = CustomerOptInType.HOME_PHONE.getValue();
                            }

                            customerOptInService.updateCustomerOptInData(id, optInType, optInValue);
                            return AjaxResult.success();
                        } else {
                            LOGGER.warn("DataStr is invalid");
                            return AjaxResult.failure();
                        }
                    default:
                        LOGGER.warn("No such feature: {}", feature);
                        break;
                }
            }
            return AjaxResult.success();
        });
    }
}
