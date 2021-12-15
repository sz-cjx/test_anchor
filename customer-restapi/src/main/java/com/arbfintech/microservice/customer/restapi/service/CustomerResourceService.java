package com.arbfintech.microservice.customer.restapi.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.arbfintech.framework.component.core.constant.CodeConst;
import com.arbfintech.framework.component.core.type.AjaxResult;
import com.arbfintech.framework.component.core.type.ProcedureException;
import com.arbfintech.framework.component.core.util.DateUtil;
import com.arbfintech.microservice.customer.object.entity.CustomerEmploymentData;
import com.arbfintech.microservice.customer.object.entity.CustomerProfile;
import com.arbfintech.microservice.customer.object.enumerate.CustomerErrorCode;
import com.arbfintech.microservice.customer.object.util.CustomerFeildKey;
import com.arbfintech.microservice.customer.restapi.component.SystemLogComponent;
import com.arbfintech.microservice.customer.restapi.repository.reader.CommonReader;
import com.arbfintech.microservice.customer.restapi.repository.writer.CommonWriter;
import com.arbfintech.microservice.loan.object.enumerate.EventTypeEnum;
import com.arbfintech.microservice.origination.object.util.DataProcessingUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;

@Service
public class CustomerResourceService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerResourceService.class);

    @Autowired
    private CommonReader commonReader;

    @Autowired
    private CommonWriter commonWriter;

    @Autowired
    private SystemLogComponent systemLogComponent;

    public CustomerProfile getCustomerProfile(Long customerId) {
        return commonReader.getEntityByCustomerId(CustomerProfile.class, customerId);
    }

    public JSONObject getCustomerEmploymentData(Long customerId) {
        CustomerEmploymentData employmentData = commonReader.getEntityByCustomerId(CustomerEmploymentData.class, customerId);
        JSONObject employmentJson = JSON.parseObject(JSON.toJSONString(employmentData));
        getPretreatment(employmentJson);
        return employmentJson;
    }

    public String updateCustomerProfile(Long customerId, JSONObject currentCustomerProfile, JSONObject accountJson) {
        return AjaxResult.success();
    }

    public String updateCustomerEmploymentData(Long customerId, JSONObject currentCustomerEmployment) throws ParseException, ProcedureException {
        CustomerEmploymentData originCustomerEmployment = commonReader.getEntityByCustomerId(CustomerEmploymentData.class, customerId);

        savePretreatment(currentCustomerEmployment);

        Long resultCode = commonWriter.save(CustomerEmploymentData.class, currentCustomerEmployment.toJSONString());
        if (resultCode < CodeConst.SUCCESS) {
            LOGGER.warn("[Update Employment Data]Failed to replace customer employment data. customerId:{}, Request parameters:{}",
                    customerId, currentCustomerEmployment.toJSONString());
            throw new ProcedureException(CustomerErrorCode.FAILURE_FAILED_TO_UPDATE_DATA);
        }

        systemLogComponent.addSystemLog(
                customerId, null, EventTypeEnum.LOAN_REGISTRY_CHANGE.getValue(),
                JSONObject.parseObject(JSON.toJSONString(originCustomerEmployment)), currentCustomerEmployment, DateUtil.getCurrentTimestamp()
        );

        return AjaxResult.success();
    }

    /**
     * 预处理： 转换时间，去掉mask，名字和邮箱转小写
     * @param dataJson
     */
    private void savePretreatment(JSONObject dataJson) throws ParseException {
        // 去掉电话号码的mask
        for (String key : CustomerFeildKey.getContainPhoneNumberList()) {
            dataJson.put(key, dataJson.getString(key).replaceAll("[^0-9]", ""));
        }

        // 时间字符串转时间戳
        DataProcessingUtil.batchConvertDateToTimestamp(dataJson, CustomerFeildKey.getTimeConversionList());

        // 名字和邮箱转小写
        for (String key : CustomerFeildKey.getConversionLowercaseList()) {
            dataJson.put(key, dataJson.getString(key).toLowerCase());
        }
    }

    private void getPretreatment(JSONObject dataJson) {
        // 时间字符串转时间戳
        DataProcessingUtil.batchConvertTimestampToDate(dataJson, CustomerFeildKey.getTimeConversionList());
    }
}
