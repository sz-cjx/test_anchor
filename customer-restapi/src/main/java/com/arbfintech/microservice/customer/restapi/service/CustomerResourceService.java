package com.arbfintech.microservice.customer.restapi.service;

import com.alibaba.fastjson.JSONObject;
import com.arbfintech.framework.component.core.constant.CodeConst;
import com.arbfintech.framework.component.core.type.AjaxResult;
import com.arbfintech.framework.component.core.type.ProcedureException;
import com.arbfintech.microservice.customer.object.constant.CustomerJsonKey;
import com.arbfintech.microservice.customer.object.entity.CustomerEmploymentData;
import com.arbfintech.microservice.customer.object.entity.CustomerProfile;
import com.arbfintech.microservice.customer.object.enumerate.CustomerErrorCode;
import com.arbfintech.microservice.customer.object.util.CustomerFeildKey;
import com.arbfintech.microservice.customer.restapi.repository.reader.CommonReader;
import com.arbfintech.microservice.customer.restapi.repository.writer.CommonWriter;
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

    public CustomerProfile getCustomerProfile(Long customerId) {
        return commonReader.getEntityByCustomerId(CustomerProfile.class, customerId);
    }

    public CustomerEmploymentData getCustomerEmploymentData(Long customerId) {
        return commonReader.getEntityByCustomerId(CustomerEmploymentData.class, customerId);
    }

    public String updateCustomerProfile(Long customerId, JSONObject currentCustomerProfile, JSONObject accountJson) {
        return AjaxResult.success();
    }

    public String updateCustomerEmploymentData(Long customerId, JSONObject currentCustomerEmployment, JSONObject accountJson) throws ParseException, ProcedureException {
        CustomerEmploymentData originCustomerEmployment = commonReader.getEntityByCustomerId(CustomerEmploymentData.class, customerId);

        DataProcessingUtil.batchConvertDateToTimestamp(currentCustomerEmployment, CustomerJsonKey.LAST_PAYDAY);

        removeMask(currentCustomerEmployment);

        Long resultCode = commonWriter.save(CustomerEmploymentData.class, currentCustomerEmployment.toJSONString());
        if (resultCode < CodeConst.SUCCESS) {
            LOGGER.warn("[Update Employment Data]Failed to replace customer employment data. customerId:{}, Request parameters:{}",
                    customerId, currentCustomerEmployment.toJSONString());
            throw new ProcedureException(CustomerErrorCode.FAILURE_FAILED_TO_UPDATE_DATA);
        }

        // TODO add timeline

        return AjaxResult.success();
    }

    private void removeMask(JSONObject dataJson) {
        // 去掉电话号码的mask
        for (String key : CustomerFeildKey.getContainPhoneNumberList()) {
            dataJson.put(key, dataJson.getString(key).replaceAll("[^0-9]", ""));
        }


    }
}
