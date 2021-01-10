package com.arbfintech.microservice.customer.restapi.future;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.arbfintech.framework.component.core.constant.JsonKeyConst;
import com.arbfintech.framework.component.core.type.AjaxResult;
import com.arbfintech.framework.component.core.type.ProcedureException;
import com.arbfintech.framework.component.core.type.SqlOption;
import com.arbfintech.framework.component.core.util.DateUtil;
import com.arbfintech.framework.component.core.util.EnumUtil;
import com.arbfintech.framework.component.core.util.UuidUtil;
import com.arbfintech.framework.component.database.core.SimpleService;
import com.arbfintech.microservice.customer.object.constant.CustomerFeatureKey;
import com.arbfintech.microservice.customer.object.constant.CustomerJsonKey;
import com.arbfintech.microservice.customer.object.entity.Customer;
import com.arbfintech.microservice.customer.object.entity.CustomerOptInData;
import com.arbfintech.microservice.customer.object.entity.CustomerProfile;
import com.arbfintech.microservice.customer.object.enumerate.CustomerErrorCode;
import com.arbfintech.microservice.customer.object.enumerate.CustomerOptInType;
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

import java.util.ArrayList;
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
            Long now = DateUtil.getCurrentTimestamp();
            try {
                if (!CustomerUtil.isValid(dataJson)) {
                    throw new ProcedureException(CustomerErrorCode.QUERY_FAILURE_MISS_REQUIRED_PARAM);
                }

                String uniqueCode = CustomerUtil.generateUniqueCode(
                        dataJson.getString(CustomerJsonKey.SSN),
                        dataJson.getString(CustomerJsonKey.EMAIL)
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
                customer.setOpenId(UuidUtil.getUuid());
                customer.setCreatedAt(now);
                customer.setUpdatedAt(now);
                customer.setStatus(-1);

                Long customerId = simpleService.save(customer);
                ResultUtil.checkResult(customerId, CustomerErrorCode.CREATE_FAILURE_CUSTOMER_SAVE);
                customerProfile.setId(customerId);
                customerProfile.setCreatedAt(now);
                customerProfile.setUpdatedAt(now);
                ResultUtil.checkResult(simpleService.save(customerProfile), CustomerErrorCode.CREATE_FAILURE_CUSTOMER_PROFILE_SAVE);
                ResultUtil.checkResult(customerOptInService.initCustomerOptIn(customerId), CustomerErrorCode.CREATE_FAILURE_OPT_IN_SAVE);

                LOGGER.info("[Create Customer] Create customer success, id: {}", customerId);
                return AjaxResult.success(customerId);
            } catch (ProcedureException e) {
                LOGGER.warn(e.getMessage());
                return AjaxResult.failure(e);
            }
        });
    }

    public CompletableFuture<String> searchCustomer(String email, String openId) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                if (StringUtils.isAllBlank(email, openId)) {
                    throw new ProcedureException(CustomerErrorCode.QUERY_FAILURE_SEARCH_FAILED);
                }

                JSONObject resultJson = customerProfileService.searchCustomer(email, openId);
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

    public CompletableFuture<String> loadFeatures(Long customerId, List<String> features) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                if (CollectionUtils.isEmpty(features)) {
                    throw new ProcedureException(CustomerErrorCode.QUERY_FAILURE_MISS_REQUIRED_PARAM);
                }

                JSONObject featureJson = new JSONObject();
                cascadeFeatureByCustomerId(customerId, features, featureJson);
                return AjaxResult.success(featureJson);
            } catch (ProcedureException e) {
                LOGGER.warn(e.getMessage());
                return AjaxResult.failure(e);
            }
        });
    }

    public CompletableFuture<String> updateFeatures(Long customerId, List<String> features, String data) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                if (CollectionUtils.isEmpty(features) || StringUtils.isBlank(data)) {
                    throw new ProcedureException(CustomerErrorCode.QUERY_FAILURE_MISS_REQUIRED_PARAM);
                }
                ResultUtil.checkResult(updateFeatureByCustomerId(customerId, features, data), CustomerErrorCode.CREATE_FAILURE_OPT_IN_SAVE);
                LOGGER.info("[Update feature] update success");
            } catch (ProcedureException e) {
                LOGGER.warn(e.getMessage());
                return AjaxResult.failure(e);
            }
            return AjaxResult.success();
        });
    }

    public CompletableFuture<String> updateCustomerProfile(JSONObject dataJson) {
        return CompletableFuture.supplyAsync(() -> {
            Long now = DateUtil.getCurrentTimestamp();
            try {
                Long customerId = dataJson.getLong(JsonKeyConst.ID);

                if (customerId == null) {
                    throw new ProcedureException(CustomerErrorCode.UPDATE_FAILURE_MISS_ID);
                }

                Customer customer = dataJson.toJavaObject(Customer.class);
                CustomerProfile customerProfile = dataJson.toJavaObject(CustomerProfile.class);

                customer.setUpdatedAt(now);
                customerProfile.setUpdatedAt(now);

                ResultUtil.checkResult(simpleService.save(customer), CustomerErrorCode.UPDATE_FAILURE_CUSTOMER_SAVE);
                ResultUtil.checkResult(simpleService.save(customerProfile), CustomerErrorCode.UPDATE_FAILURE_CUSTOMER_PROFILE_SAVE);
            } catch (ProcedureException e) {
                LOGGER.warn(e.getMessage());
                return AjaxResult.failure(e);
            }

            return AjaxResult.success();
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
                case CustomerFeatureKey.OPT_IN:
                    JSONObject optInDataJson = new JSONObject();
                    sqlOption.whereIN("opt_in_type", EnumUtil.getAllValues(CustomerOptInType.class), null);
                    List<CustomerOptInData> optInDataList = simpleService.findAllByOptions(CustomerOptInData.class, sqlOption.toString());
                    for (CustomerOptInData optInData : optInDataList) {
                        optInDataJson.put(
                                EnumUtil.getKeyByValue(CustomerOptInType.class, optInData.getOptInType()),
                                optInData.getOptInValue()
                        );
                    }
                    dataJson.put(CustomerJsonKey.OPT_IN, optInDataJson);
                    break;
                default:
                    LOGGER.warn("No such feature: {}", feature);
                    break;
            }
        }
    }

    private Integer updateFeatureByCustomerId(Long customerId, Collection<String> featureArray, String data) {
        Integer result = -1;
        Long now = DateUtil.getCurrentTimestamp();
        JSONObject dataJson = JSON.parseObject(data).getJSONObject(CustomerJsonKey.DATA);
        for (String feature : featureArray) {
            switch (feature) {
                case CustomerFeatureKey.OPT_IN:
                    List<CustomerOptInData> dataList = new ArrayList<>();
                    JSONObject optInJson = dataJson.getJSONObject(CustomerJsonKey.OPT_IN);
                    for (String key : optInJson.keySet()) {
                        Integer value = optInJson.getInteger(key);
                        Integer type = EnumUtil.getValueByKey(CustomerOptInType.class, key);
                        CustomerOptInData customerOptInData = new CustomerOptInData();
                        customerOptInData.setId(customerId);
                        customerOptInData.setOptInType(type);
                        customerOptInData.setOptInValue(value);
                        customerOptInData.setUpdatedAt(now);
                        dataList.add(customerOptInData);
                    }
                    result = customerOptInService.updateCustomerOptInData(dataList);
                    break;
                default:
                    LOGGER.warn("No such feature: {}", feature);
                    break;
            }
        }
        return result;
    }
}
