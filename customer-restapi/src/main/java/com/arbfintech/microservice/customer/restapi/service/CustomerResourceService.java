package com.arbfintech.microservice.customer.restapi.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.arbfintech.framework.component.core.constant.CodeConst;
import com.arbfintech.framework.component.core.enumerate.StateEnum;
import com.arbfintech.framework.component.core.type.AjaxResult;
import com.arbfintech.framework.component.core.type.ProcedureException;
import com.arbfintech.framework.component.core.util.DateUtil;
import com.arbfintech.framework.component.core.util.EnumUtil;
import com.arbfintech.microservice.customer.object.constant.CustomerJsonKey;
import com.arbfintech.microservice.customer.object.dto.CustomerEmploymentDTO;
import com.arbfintech.microservice.customer.object.entity.CustomerBankCardData;
import com.arbfintech.microservice.customer.object.entity.CustomerBankData;
import com.arbfintech.microservice.customer.object.entity.CustomerEmploymentData;
import com.arbfintech.microservice.customer.object.entity.CustomerOperationLog;
import com.arbfintech.microservice.customer.object.entity.CustomerProfile;
import com.arbfintech.microservice.customer.object.enumerate.CustomerErrorCode;
import com.arbfintech.microservice.customer.object.util.CustomerFeildKey;
import com.arbfintech.microservice.customer.restapi.component.SystemLogComponent;
import com.arbfintech.microservice.customer.restapi.repository.CustomerReader;
import com.arbfintech.microservice.customer.restapi.repository.reader.CommonReader;
import com.arbfintech.microservice.customer.restapi.repository.writer.CommonWriter;
import com.arbfintech.microservice.origination.object.util.DataProcessingUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

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
    private SystemLogComponent systemLogComponent;

    public JSONObject getCustomerProfile(Long customerId) throws ProcedureException {
        CustomerProfile customerProfile = Optional.ofNullable(commonReader.getEntityByCustomerId(CustomerProfile.class, customerId))
                .orElseThrow(() -> new ProcedureException(CustomerErrorCode.QUERY_FAILURE_CUSTOMER_NOT_EXISTED));

        JSONObject jsonObject = JSON.parseObject(JSON.toJSONString(customerProfile));
        getPretreatment(jsonObject);

        return jsonObject;
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
            LOGGER.warn("[Update Customer Profile Data]Failed to replace customer profile data. customerId:{}, Request parameters:{}",
                    customerId, currentCustomerProfile.toJSONString());
            throw new ProcedureException(CustomerErrorCode.FAILURE_FAILED_TO_UPDATE_DATA);
        }

        systemLogComponent.sysLogHandleFactory(
                customerId, null, JSONObject.parseObject(JSON.toJSONString(originCustomerProfile)),
                currentCustomerProfile, DateUtil.getCurrentTimestamp()
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
            LOGGER.warn("[Update Employment Data]Failed to replace customer employment data. customerId:{}, Request parameters:{}",
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
            LOGGER.warn("[Update Employment Data]Failed to replace customer employment data. customerId:{}", customerId);
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
        CustomerBankCardData customerBankCardData = currentCustomerBankCard.toJavaObject(CustomerBankCardData.class);
        Long id = customerBankCardData.getId();
        customerBankCardData.setCustomerId(customerId);

        JSONObject originCustomerBankCardJson = new JSONObject();
        Long result = null;
        if (Objects.nonNull(id)) {
            // Update
            CustomerBankCardData originCustomerBankCard = commonReader.getEntityByCustomerId(CustomerBankCardData.class, id);
            originCustomerBankCardJson = JSONObject.parseObject(JSON.toJSONString(originCustomerBankCard));
        }

        result = commonWriter.save(customerBankCardData);
        if (result < CodeConst.SUCCESS) {
            LOGGER.warn("[Update Employment Data]Failed to replace customer employment data. customerId:{}", customerId);
            throw new ProcedureException(CustomerErrorCode.FAILURE_FAILED_TO_UPDATE_DATA);
        }

        systemLogComponent.sysLogHandleFactory(
                customerId, null, originCustomerBankCardJson,
                JSONObject.parseObject(JSON.toJSONString(customerBankCardData)), DateUtil.getCurrentTimestamp()
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
     * 预处理： 转换时间，去掉mask，名字和邮箱转小写
     *
     * @param dataJson
     */
    private void savePretreatment(JSONObject dataJson) throws ParseException {
        // 去掉电话号码和SSN的mask
        for (String key : CustomerFeildKey.getRemoveMaskList()) {
            if (dataJson.containsKey(key)) {
                dataJson.put(key, dataJson.getString(key).replaceAll("[^0-9]", ""));
            }
        }

        // 时间字符串转时间戳
        DataProcessingUtil.batchConvertDateToTimestamp(dataJson, CustomerFeildKey.getTimeConversionList());

        // 名字和邮箱转小写
        for (String key : CustomerFeildKey.getConversionLowercaseList()) {
            if (dataJson.containsKey(key)) {
                dataJson.put(key, dataJson.getString(key).toLowerCase());
            }
        }

        // profile中的state转换: String -> int
        Integer state = (Integer) EnumUtil.getValueByText(StateEnum.class, dataJson.getString(CustomerJsonKey.STATE));
        dataJson.put(CustomerJsonKey.STATE, state);
    }

    private void getPretreatment(JSONObject dataJson) {
        // 时间字符串转时间戳
        DataProcessingUtil.batchConvertTimestampToDate(dataJson, CustomerFeildKey.getTimeConversionList());

        // profile中的state转换: int -> String
        String state = EnumUtil.getTextByValue(StateEnum.class, dataJson.getInteger(CustomerJsonKey.STATE));
        dataJson.put(CustomerJsonKey.STATE, state);
    }
}
