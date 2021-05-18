package com.arbfintech.microservice.customer.restapi.future;

import com.alibaba.fastjson.JSONArray;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
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

    @Transactional(rollbackFor = Exception.class)
    public String createCustomer(JSONObject dataJson) {
        Long now = DateUtil.getCurrentTimestamp();
        try {
            if (!CustomerUtil.isValid(dataJson)) {
                throw new ProcedureException(CustomerErrorCode.QUERY_FAILURE_MISS_REQUIRED_PARAM);
            }

            String uniqueCode = CustomerUtil.generateUniqueCode(
                    dataJson.getString(CustomerJsonKey.SSN),
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
            ResultUtil.checkResult(customerOptInService.initCustomerOptIn(customerId, dataJson.getLong(CustomerJsonKey.PORTFOLIO_ID)), CustomerErrorCode.CREATE_FAILURE_OPT_IN_SAVE);

            LOGGER.info("[Create Customer] Create customer success, id: {}", customerId);
            return AjaxResult.success(customerId);
        } catch (ProcedureException e) {
            LOGGER.warn(e.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return AjaxResult.failure(e);
        }
    }

    public CompletableFuture<String> searchCustomer(Long id, String email, String openId) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                if (StringUtils.isAllBlank(String.valueOf(id), email, openId)) {
                    throw new ProcedureException(CustomerErrorCode.QUERY_FAILURE_SEARCH_FAILED);
                }

                JSONArray resultJsonArray = customerProfileService.searchCustomer(id, email, openId);
                if (CollectionUtils.isEmpty(resultJsonArray)) {
                    throw new ProcedureException(CustomerErrorCode.QUERY_FAILURE_CUSTOMER_NOT_EXISTED);
                }
                return AjaxResult.success(resultJsonArray);
            } catch (ProcedureException e) {
                LOGGER.warn(e.getMessage());
                return AjaxResult.failure(e);
            }
        });
    }

    public CompletableFuture<String> loadFeatures(Long customerId, Long portfolioId, List<String> features) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                if (CollectionUtils.isEmpty(features)) {
                    throw new ProcedureException(CustomerErrorCode.QUERY_FAILURE_MISS_REQUIRED_PARAM);
                }

                JSONObject featureJson = new JSONObject();
                cascadeFeatureByCustomerId(customerId, portfolioId, features, featureJson);
                return AjaxResult.success(featureJson);
            } catch (ProcedureException e) {
                LOGGER.warn(e.getMessage());
                return AjaxResult.failure(e);
            }
        });
    }

    public CompletableFuture<String> updateFeatures(JSONObject dataJson) {
        return CompletableFuture.supplyAsync(() -> {
            Integer totalRow;
            try {
                List<String> features = dataJson.getJSONArray(CustomerJsonKey.FEATURES).toJavaList(String.class);
                JSONObject data = dataJson.getJSONObject(CustomerJsonKey.DATA);
                Long customerId = dataJson.getLong(CustomerJsonKey.CUSTOMER_ID);
                Long portfolioId = dataJson.getLong(CustomerJsonKey.PORTFOLIO_ID);

                if (customerId == null || CollectionUtils.isEmpty(features) || CollectionUtils.isEmpty(data)) {
                    throw new ProcedureException(CustomerErrorCode.QUERY_FAILURE_MISS_REQUIRED_PARAM);
                }
                totalRow = updateFeatureByCustomerId(customerId, portfolioId, features, data);
                ResultUtil.checkResult(totalRow, CustomerErrorCode.CREATE_FAILURE_OPT_IN_SAVE);
                LOGGER.info("[Update feature] update success");
            } catch (ProcedureException e) {
                LOGGER.warn(e.getMessage());
                return AjaxResult.failure(e);
            }
            return AjaxResult.success(totalRow);
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

    public CompletableFuture<String> getCustomerByUnique(String ssn, String routingNo, String accountNo) {
        return CompletableFuture.supplyAsync(() -> {
            String uniqueCode = CustomerUtil.generateUniqueCode(ssn, routingNo, accountNo);
            return AjaxResult.success(simpleService.findByOptions(
                    Customer.class,
                    SqlOption.getInstance().whereEqual("unique_code", uniqueCode, null).toString())
            );
        });
    }

    private void cascadeFeatureByCustomerId(Long customerId, Long portfolioId, Collection<String> featureArray, JSONObject dataJson) throws ProcedureException {
        if (featureArray == null) {
            return;
        }
        SqlOption sqlOption = SqlOption.getInstance();
        sqlOption.whereEqual("id", customerId, null);
        for (String feature : featureArray) {
            switch (feature) {
                case CustomerFeatureKey.OPT_IN:
                    if (Objects.isNull(portfolioId)) {
                        throw new ProcedureException(CustomerErrorCode.FAILURE_MISS_REQUIRED_PARAM);
                    }

                    JSONObject optInDataJson = new JSONObject();
                    sqlOption.whereIN("opt_in_type", EnumUtil.getAllValues(CustomerOptInType.class), null);
                    sqlOption.whereEqual("portfolio_id", portfolioId);
                    List<CustomerOptInData> optInDataList = simpleService.findAllByOptions(CustomerOptInData.class, sqlOption.toString());

                    if (CollectionUtils.isEmpty(optInDataList)) {
                        optInDataList = customerOptInService.createCustomerOptIn(customerId, portfolioId);
                    }
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

    private Integer updateFeatureByCustomerId(Long customerId, Long portfolioId, Collection<String> featureArray, JSONObject dataJson) throws ProcedureException {
        Integer result = -1;
        Long now = DateUtil.getCurrentTimestamp();
        for (String feature : featureArray) {
            switch (feature) {
                case CustomerFeatureKey.OPT_IN:
                    if (Objects.isNull(portfolioId)) {
                        throw new ProcedureException(CustomerErrorCode.FAILURE_MISS_REQUIRED_PARAM);
                    }

                    List<CustomerOptInData> dataList = new ArrayList<>();
                    JSONObject optInJson = dataJson.getJSONObject(CustomerJsonKey.OPT_IN);
                    for (String key : optInJson.keySet()) {
                        Integer value = optInJson.getInteger(key);
                        Integer type = EnumUtil.getValueByKey(CustomerOptInType.class, key);
                        CustomerOptInData customerOptInData = new CustomerOptInData();
                        customerOptInData.setId(customerId);
                        customerOptInData.setPortfolioId(portfolioId);
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

    public CompletableFuture<String> unsubscribeCustomer(String openId, Integer type, Integer value) {
        return CompletableFuture.supplyAsync(() -> {
            Long result = -1L;
            try {
                if (StringUtils.isAnyBlank(openId, String.valueOf(type), String.valueOf(value))) {
                    throw new ProcedureException(CustomerErrorCode.QUERY_FAILURE_MISS_REQUIRED_PARAM);
                }
                LOGGER.info("[Unsubscribe Marketing] Start to unsubscribe marketing open id: {}, type: {}, value:{}", openId, type, value);
                SqlOption customerSql = SqlOption.getInstance();
                customerSql.whereEqual("open_id", openId, null);
                Customer customerDb = simpleService.findByOptions(Customer.class, customerSql.toString());
                if (Objects.isNull(customerDb)) {
                    throw new ProcedureException(CustomerErrorCode.QUERY_FAILURE_CUSTOMER_NOT_EXISTED);
                }
                SqlOption customerOptInSql = SqlOption.getInstance();
                customerOptInSql.whereEqual("id", customerDb.getId(), null);
                customerOptInSql.whereEqual("opt_in_type", type, null);
                CustomerOptInData customerOptInData = simpleService.findByOptions(CustomerOptInData.class, customerOptInSql.toString());
                customerOptInData.setUpdatedAt(System.currentTimeMillis());
                Integer optInValue = customerOptInData.getOptInValue();
                if ((optInValue & value) != value) {
                    throw new ProcedureException(CustomerErrorCode.UPDATE_FAILURE_HAD_UNSUBSCRIBE);
                }
                customerOptInData.setOptInValue(optInValue - value);
                result = simpleService.save(customerOptInData);
                ResultUtil.checkResult(result, CustomerErrorCode.UPDATE_FAILURE_OPT_IN_SAVE);
            } catch (ProcedureException e) {
                LOGGER.warn(e.getMessage());
                return AjaxResult.failure(e);
            }
            LOGGER.info("[Unsubscribe Marketing] Update success, marketing open id: {}, type: {}, value:{}", openId, type, value);
            return AjaxResult.success(result);
        });
    }

    public String createCustomerOptIn(List<CustomerOptInData> optInDataList) {
        try {
            if (CollectionUtils.isEmpty(optInDataList) || optInDataList.get(0).getId() == null) {
                throw new ProcedureException(CustomerErrorCode.FAILURE_MISS_REQUIRED_PARAM);
            }
            Long time = System.currentTimeMillis();
            for (CustomerOptInData customerOptInData : optInDataList) {
                customerOptInData.setCreatedAt(time);
                customerOptInData.setUpdatedAt(time);
            }
            Integer row = customerOptInService.batchSave(optInDataList);
            ResultUtil.checkResult(row, CustomerErrorCode.CREATE_FAILURE_OPT_IN_SAVE);
            return AjaxResult.success(row);
        } catch (ProcedureException e) {
            LOGGER.warn(e.getMessage());
            return AjaxResult.failure(e);
        }
    }
}