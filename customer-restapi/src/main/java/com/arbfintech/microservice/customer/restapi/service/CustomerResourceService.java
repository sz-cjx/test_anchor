package com.arbfintech.microservice.customer.restapi.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.arbfintech.framework.component.core.constant.CodeConst;
import com.arbfintech.framework.component.core.enumerate.StateEnum;
import com.arbfintech.framework.component.core.type.AjaxResult;
import com.arbfintech.framework.component.core.type.ProcedureException;
import com.arbfintech.framework.component.core.util.DateUtil;
import com.arbfintech.framework.component.core.util.EnumUtil;
import com.arbfintech.framework.component.core.util.JsonUtil;
import com.arbfintech.microservice.customer.object.constant.CustomerJsonKey;
import com.arbfintech.microservice.customer.object.dto.CustomerContactDTO;
import com.arbfintech.microservice.customer.object.dto.CustomerEmploymentDTO;
import com.arbfintech.microservice.customer.object.dto.CustomerOptInDTO;
import com.arbfintech.microservice.customer.object.dto.IbvDTO;
import com.arbfintech.microservice.customer.object.entity.*;
import com.arbfintech.microservice.customer.object.enumerate.*;
import com.arbfintech.microservice.customer.object.util.CustomerFieldKey;
import com.arbfintech.microservice.customer.restapi.component.SystemLogComponent;
import com.arbfintech.microservice.customer.restapi.repository.CustomerReader;
import com.arbfintech.microservice.customer.restapi.repository.CustomerWriter;
import com.arbfintech.microservice.customer.restapi.repository.reader.CommonReader;
import com.arbfintech.microservice.customer.restapi.repository.writer.CommonWriter;
import com.arbfintech.microservice.loan.object.enumerate.DecisionLogicRequestCodeStatusEnum;
import com.arbfintech.microservice.origination.object.util.DataProcessingUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.util.*;

@Service
public class CustomerResourceService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerResourceService.class);

    @Autowired
    private CommonReader commonReader;

    @Autowired
    private CommonWriter commonWriter;

    @Autowired
    private CustomerReader customerReader;

    @Autowired
    private CustomerWriter customerWriter;

    @Autowired
    private SystemLogComponent systemLogComponent;

    public JSONObject getCustomerProfile(Long customerId) throws ProcedureException {
        CustomerProfile customerProfile = Optional.ofNullable(commonReader.getEntityByCustomerId(CustomerProfile.class, customerId))
                .orElseThrow(() -> new ProcedureException(CustomerErrorCode.QUERY_FAILURE_CUSTOMER_NOT_EXISTED));

        JSONObject jsonObject = JSON.parseObject(JSON.toJSONString(customerProfile));
        getPretreatment(jsonObject);

        return jsonObject;
    }

    public List<CustomerContactData> getCustomerContact(Long customerId) throws ProcedureException {
        List<CustomerContactData> contactData = commonReader.listEntityByCustomerId(CustomerContactData.class, customerId);
        if (CollectionUtils.isEmpty(contactData)) {
            throw new ProcedureException(CustomerErrorCode.QUERY_FAILURE_CUSTOMER_NOT_EXISTED);
        }

        return contactData;
    }

    public CustomerEmploymentDTO getCustomerEmploymentData(Long customerId) throws ProcedureException {
        CustomerEmploymentData employmentData = Optional.ofNullable(commonReader.getEntityByCustomerId(CustomerEmploymentData.class, customerId))
                .orElseThrow(() -> new ProcedureException(CustomerErrorCode.QUERY_FAILURE_CUSTOMER_NOT_EXISTED));

        JSONObject employmentJson = JSON.parseObject(JSON.toJSONString(employmentData));
        getPretreatment(employmentJson);
        return employmentJson.toJavaObject(CustomerEmploymentDTO.class);
    }

    public List<CustomerBankData> listCustomerBankData(Long customerId) {
        return commonReader.listEntityByCustomerId(CustomerBankData.class, customerId);
    }

    public List<CustomerBankCardData> listCustomerBankCardData(Long customerId) {
        return commonReader.listEntityByCustomerId(CustomerBankCardData.class, customerId);
    }

    public String updateCustomerProfile(Long customerId, JSONObject currentCustomerProfile) throws ParseException, ProcedureException {
        CustomerProfile originCustomerProfile = Optional.ofNullable(commonReader.getEntityByCustomerId(CustomerProfile.class, customerId))
                .orElseThrow(() -> new ProcedureException(CustomerErrorCode.QUERY_FAILURE_CUSTOMER_NOT_EXISTED));

        savePretreatment(currentCustomerProfile);

        Long resultCode = commonWriter.save(CustomerProfile.class, currentCustomerProfile.toJSONString());
        if (resultCode < CodeConst.SUCCESS) {
            LOGGER.warn("[Update Customer Profile Data]Failed to replace customer profile data. CustomerId:{}, Request parameters:{}",
                    customerId, currentCustomerProfile.toJSONString());
            throw new ProcedureException(CustomerErrorCode.FAILURE_FAILED_TO_UPDATE_DATA);
        }

        systemLogComponent.sysLogHandleFactory(
                customerId, null, JSONObject.parseObject(JSON.toJSONString(originCustomerProfile)),
                currentCustomerProfile, DateUtil.getCurrentTimestamp()
        );

        return AjaxResult.success();
    }

    public String updateCustomerContact(Long customerId, JSONObject currentCustomerContact) throws ProcedureException, ParseException {
        List<CustomerContactData> contactDataList = commonReader.listEntityByCustomerId(CustomerContactData.class, customerId);
        savePretreatment(currentCustomerContact);
        CustomerContactDTO currentData = JSON.toJavaObject(currentCustomerContact, CustomerContactDTO.class);
        currentCustomerContact = JSONObject.parseObject(JSON.toJSONString(currentData));
        JSONArray currentArray = new JSONArray();
        for (CustomerContactTypeEnum contactTypeEnum : CustomerContactTypeEnum.values()) {
            String contactValue = currentCustomerContact.getString(contactTypeEnum.getKey());
            if (StringUtils.isNotBlank(contactValue)) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put(CustomerJsonKey.CUSTOMER_ID, customerId);
                jsonObject.put(CustomerJsonKey.CONTACT_TYPE, contactTypeEnum.getValue());
                jsonObject.put(CustomerJsonKey.VALUE, contactValue);
                currentArray.add(jsonObject);
            }
        }
        if (CollectionUtils.isEmpty(currentArray)) {
            LOGGER.info("[Update Contact]Missing customer contact data. CustomerId: {}", customerId);
            throw new ProcedureException(CustomerErrorCode.FAILURE_MISS_REQUIRED_PARAM);
        }

        commonWriter.bathSave(CustomerContactData.class, currentArray.toJSONString());

        CustomerContactDTO originData = null;
        if (!CollectionUtils.isEmpty(contactDataList)) {
            JSONObject originJson = new JSONObject();
            for (CustomerContactData contactData : contactDataList) {
                originJson.put(
                        CustomerContactTypeEnum.getEnumByvalue(contactData.getContactType()).getKey(),
                        contactData.getValue()
                );
            }
            originData = originJson.toJavaObject(CustomerContactDTO.class);
        }

        systemLogComponent.sysLogHandleFactory(
                customerId, null, JSONObject.parseObject(JSON.toJSONString(originData)),
                currentCustomerContact, DateUtil.getCurrentTimestamp()
        );
        return AjaxResult.success();
    }

    public String updateCustomerEmploymentData(Long customerId, JSONObject currentCustomerEmployment) throws ParseException, ProcedureException {
        CustomerEmploymentData originCustomerEmployment = commonReader.getEntityByCustomerId(CustomerEmploymentData.class, customerId);

        CustomerEmploymentDTO employmentDTO = currentCustomerEmployment.toJavaObject(CustomerEmploymentDTO.class);
        employmentDTO.setId(customerId);
        currentCustomerEmployment = JSON.parseObject(JSON.toJSONString(employmentDTO));

        savePretreatment(currentCustomerEmployment);

        Long resultCode = commonWriter.save(CustomerEmploymentData.class, currentCustomerEmployment.toJSONString());
        if (resultCode < CodeConst.SUCCESS) {
            LOGGER.warn("[Update Employment Data]Failed to replace customer employment data. CustomerId:{}, Request parameters:{}",
                    customerId, currentCustomerEmployment.toJSONString());
            throw new ProcedureException(CustomerErrorCode.FAILURE_FAILED_TO_UPDATE_DATA);
        }

        systemLogComponent.sysLogHandleFactory(
                customerId, null, JSONObject.parseObject(JSON.toJSONString(originCustomerEmployment)),
                currentCustomerEmployment, DateUtil.getCurrentTimestamp()
        );

        return AjaxResult.success();
    }

    public String updateCustomerBankData(Long customerId, JSONObject customerBankJson) throws ProcedureException, ParseException {
        savePretreatment(customerBankJson);
        CustomerBankData currentCustomerBank = customerBankJson.toJavaObject(CustomerBankData.class);
        Long id = currentCustomerBank.getId();
        currentCustomerBank.setCustomerId(customerId);

        JSONObject originCustomerBankJson = new JSONObject();
        Long result = null;
        if (Objects.nonNull(id)) {
            // Update
            CustomerBankData originCustomerBank = commonReader.getEntityByCustomerId(CustomerBankData.class, id);
            originCustomerBankJson = JSONObject.parseObject(JSON.toJSONString(originCustomerBank));
        }

        result = commonWriter.save(currentCustomerBank);
        if (result < CodeConst.SUCCESS) {
            LOGGER.warn("[Update Bank Data]Failed to replace customer bank data. CustomerId:{}", customerId);
            throw new ProcedureException(CustomerErrorCode.FAILURE_FAILED_TO_UPDATE_DATA);
        }

        systemLogComponent.sysLogHandleFactory(
                customerId, null, originCustomerBankJson,
                JSONObject.parseObject(JSON.toJSONString(currentCustomerBank)), DateUtil.getCurrentTimestamp()
        );

        return AjaxResult.success(result);
    }

    public String updateCustomerBankCardData(Long customerId, JSONObject currentCustomerBankCard) throws ProcedureException, ParseException {
        savePretreatment(currentCustomerBankCard);
        Long currentTimestamp = DateUtil.getCurrentTimestamp();

        if (JsonUtil.containsKey(currentCustomerBankCard, CustomerJsonKey.EXPIRATION_MONTH, CustomerJsonKey.EXPIRATION_YEAR)) {
            Integer expirationMonth = currentCustomerBankCard.getInteger(CustomerJsonKey.EXPIRATION_MONTH);
            Integer expirationYear = currentCustomerBankCard.getInteger(CustomerJsonKey.EXPIRATION_YEAR);
            long expirationAt = DateUtil.strToTimeStamp(expirationYear + "-" + expirationMonth + "-1");

            if (expirationAt < currentTimestamp) {
                LOGGER.warn("[Update Card Data]Expiration time is less than current time. CustomerId:{}, ExpirationMonth:{}, ExpirationYear: {}",
                        customerId, expirationMonth, expirationYear);
                throw new ProcedureException(CustomerErrorCode.FAILURE_UPDATE_BANK_CARD_EXPIRATION_DATE);
            }

            currentCustomerBankCard.put(CustomerJsonKey.EXPIRED_AT, expirationAt);
        }

        CustomerBankCardData customerBankCardData = currentCustomerBankCard.toJavaObject(CustomerBankCardData.class);
        Long id = customerBankCardData.getId();
        customerBankCardData.setCustomerId(customerId);
        customerBankCardData.setAddedAt(currentTimestamp);

        JSONObject originCustomerBankCardJson = new JSONObject();
        Long result = null;
        if (Objects.nonNull(id)) {
            // Update
            CustomerBankCardData originCustomerBankCard = commonReader.getEntityByCustomerId(CustomerBankCardData.class, id);
            originCustomerBankCardJson = JSONObject.parseObject(JSON.toJSONString(originCustomerBankCard));
        }

        result = commonWriter.save(customerBankCardData);
        if (result < CodeConst.SUCCESS) {
            LOGGER.warn("[Update Card Data]Failed to replace customer bank card data. CustomerId:{}", customerId);
            throw new ProcedureException(CustomerErrorCode.FAILURE_FAILED_TO_UPDATE_DATA);
        }

        systemLogComponent.sysLogHandleFactory(
                customerId, null, originCustomerBankCardJson,
                JSONObject.parseObject(JSON.toJSONString(customerBankCardData)), currentTimestamp
        );

        return AjaxResult.success(result);
    }

    public String getOperationLog(String dataStr) {
        JSONObject dataJson = JSON.parseObject(dataStr);
        Long customerId = dataJson.getLong(CustomerJsonKey.CUSTOMER_ID);
        Integer logType = dataJson.getInteger(CustomerJsonKey.LOG_TYPE);
        Integer withinDays = dataJson.getInteger(CustomerJsonKey.WITHIN_DAYS);
        List<CustomerOperationLog> list = customerReader.getOperationLogByCondition(customerId, logType, withinDays);

        return AjaxResult.success(list);
    }

    /**
     * 查询opt-in的数据，如果没有查到，初始化并返回
     * @param customerId
     * @return
     */
    public String getCustomerOptIn(Long customerId) {
        List<CustomerOptInData> optInList = commonReader.listEntityByCustomerId(CustomerOptInData.class, customerId);
        if (CollectionUtils.isEmpty(optInList)) {
            // 初始化opt-in
            LOGGER.warn("[Init Customer Opt-In]Start init customer opt-in. CustomerId:{}", customerId);
            optInList = new ArrayList<>();
            Long currentTimestamp = DateUtil.getCurrentTimestamp();
            Integer defaultValue = CustomerOptInValue.IS_MARKETING.getValue() + CustomerOptInValue.IS_OPERATION.getValue();
            optInList.add(new CustomerOptInData(customerId, 0L, CustomerOptInType.EMAIL.getValue(),defaultValue, currentTimestamp, currentTimestamp));
            optInList.add(new CustomerOptInData(customerId, 0L, CustomerOptInType.ALTERNATIVE_EMAIL.getValue(),defaultValue, currentTimestamp, currentTimestamp));
            optInList.add(new CustomerOptInData(customerId, 0L, CustomerOptInType.HOME_PHONE.getValue(),0, currentTimestamp, currentTimestamp));
            optInList.add(new CustomerOptInData(customerId, 0L, CustomerOptInType.CELL_PHONE.getValue(),0, currentTimestamp, currentTimestamp));
            customerWriter.batchSave(optInList);

            JSONObject logData = new JSONObject();
            logData.put(CustomerJsonKey.NOTE, "Initialize Customer Opt-In");
            systemLogComponent.addSystemLog(
                    customerId, logData, CustomerEventTypeEnum.CUSTOMER_NOTE.getValue(),
                    null, null, currentTimestamp
            );
        }

        return AjaxResult.success(optInList);
    }

    public String saveCustomerOptIn(CustomerOptInDTO customerOptInDTO) throws ProcedureException {
        Long customerId = customerOptInDTO.getCustomerId();
        Integer optInType = customerOptInDTO.getOptInType();
        Integer currentValue = customerOptInDTO.getOptInStatus();
        CustomerOptInType customerOptInType = EnumUtil.getByValue(CustomerOptInType.class, optInType);

        CustomerOptInData originCustomerOptIn = customerReader.getCustomerOptInByCondition(customerId, optInType);
        if (Objects.isNull(originCustomerOptIn) || Objects.isNull(originCustomerOptIn.getOptInValue())) {
            throw new ProcedureException(CustomerErrorCode.FAILURE_QUERY_DATA_IS_EXISTED);
        }

        Integer originValue = originCustomerOptIn.getOptInValue();
        Integer isMarketingValue = CustomerOptInValue.IS_MARKETING.getValue();
        Integer isOperationValue = CustomerOptInValue.IS_OPERATION.getValue();

        JSONObject originJson = new JSONObject();
        JSONObject currentJson = new JSONObject();
        boolean originEnable = false;

        if (!Objects.equals(originValue & isMarketingValue, currentValue & isMarketingValue)) {
            if (Objects.equals(originValue & isMarketingValue, isMarketingValue)) {
                originEnable = true;
            }
            originJson.put(customerOptInType.getKey() + "IsMarketing", originEnable ? "Enable" : "Disable");
            currentJson.put(customerOptInType.getKey() + "IsMarketing", originEnable ? "Disable" : "Enable");
        } else if (!Objects.equals(originValue & isOperationValue, currentValue & isOperationValue)) {
            if (Objects.equals(originValue & isOperationValue, isOperationValue)) {
                originEnable = true;
            }
            originJson.put(customerOptInType.getKey() + "IsOperation", originEnable ? "Enable" : "Disable");
            currentJson.put(customerOptInType.getKey() + "IsOperation", originEnable ? "Disable" : "Enable");
        }

        if (!CollectionUtils.isEmpty(currentJson)) {
            Long currentTimestamp = DateUtil.getCurrentTimestamp();
            originCustomerOptIn.setOptInValue(currentValue);
            originCustomerOptIn.setUpdatedAt(currentTimestamp);
            commonWriter.save(originCustomerOptIn);

            systemLogComponent.sysLogHandleFactory(
                    customerId, null, originJson,
                    currentJson, currentTimestamp
            );
        }

        return AjaxResult.success();
    }

    public String getDecisionLogic(Long customerId) {
        HashMap<String, Object> condition = new HashMap<>();
        condition.put("customerId", customerId);
        condition.put("portfolioId", 0);
        CustomerIbvData customerIbvData = commonReader.getEntityByCondition(CustomerIbvData.class, condition);
        IbvDTO ibvDTO = JSON.parseObject(JSON.toJSONString(customerIbvData), IbvDTO.class);
        return AjaxResult.success(ibvDTO);
    }

    public String authorizationDecisionLogic(IbvDTO ibvDTO) throws ProcedureException {
        Long customerId = ibvDTO.getCustomerId();
        String authorizationStr = ibvDTO.getAuthorizationData();
        JSONObject authorizationJson = JSON.parseObject(authorizationStr);
        if (!JsonUtil.containsKey(authorizationJson, CustomerJsonKey.EMAIL,
                CustomerJsonKey.BANK, CustomerJsonKey.ACCOUNT_NO, CustomerJsonKey.ROUTING_NO)) {
            LOGGER.info("[Authorize Decision Logic]Lack of some information. CustomerId: {}, AuthorizeData: {}",
                    customerId, authorizationStr);
            throw new ProcedureException(CustomerErrorCode.FAILURE_INPUT_DATA_IS_INCOMPLETE);
        }

        HashMap<String, Object> condition = new HashMap<>();
        condition.put("customerId", customerId);
        condition.put("portfolioId", 0);
        CustomerIbvData customerIbvData = commonReader.getEntityByCondition(CustomerIbvData.class, condition);
        if (Objects.isNull(customerIbvData)) {
            customerIbvData = new CustomerIbvData();
            customerIbvData.setCustomerId(customerId);
            customerIbvData.setPortfolioId(0L);
        }

        customerIbvData.setAuthorizationData(ibvDTO.getAuthorizationData());
        customerIbvData.setAuthorizationStatus(AuthorizationStatusEnum.AUTHORIZE.getValue());
        customerIbvData.setAuthorizatedAt(DateUtil.getCurrentTimestamp());
        customerIbvData.setRequestCodeStatus(DecisionLogicRequestCodeStatusEnum.VERIFIED.getValue());

        commonWriter.save(customerIbvData);

        return AjaxResult.success();
    }

    /**
     * 预处理： 转换时间，去掉mask，名字和邮箱转小写
     *
     * @param dataJson
     */
    private void savePretreatment(JSONObject dataJson) throws ParseException {
        // 去掉电话号码和SSN的mask
        for (String key : CustomerFieldKey.getRemoveMaskList()) {
            if (dataJson.containsKey(key)) {
                dataJson.put(key, dataJson.getString(key).replaceAll("[^0-9]", ""));
            }
        }

        // 时间字符串转时间戳
        DataProcessingUtil.batchConvertDateToTimestamp(dataJson, CustomerFieldKey.getTimeConversionList());

        // 名字和邮箱转小写
        for (String key : CustomerFieldKey.getConversionLowercaseList()) {
            if (dataJson.containsKey(key)) {
                dataJson.put(key, dataJson.getString(key).toLowerCase());
            }
        }

        // profile中的state转换: String -> int
        if (dataJson.containsKey(CustomerJsonKey.STATE)) {
            Integer state = StateEnum.getStateEnumBytext(dataJson.getString(CustomerJsonKey.STATE)).getValue();
            dataJson.put(CustomerJsonKey.STATE, state);
        }
    }

    private void getPretreatment(JSONObject dataJson) {
        // 时间字符串转时间戳
        DataProcessingUtil.batchConvertTimestampToDate(dataJson, CustomerFieldKey.getTimeConversionList());

        // profile中的state转换: int -> String

        if (dataJson.containsKey(CustomerJsonKey.STATE)) {
            String state = StateEnum.getStateEnumByValue(dataJson.getInteger(CustomerJsonKey.STATE)).getText();
            dataJson.put(CustomerJsonKey.STATE, state);
        }
    }
}
