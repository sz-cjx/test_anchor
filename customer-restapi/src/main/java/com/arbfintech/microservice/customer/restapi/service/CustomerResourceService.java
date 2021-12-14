package com.arbfintech.microservice.customer.restapi.service;

import com.alibaba.fastjson.JSONObject;
import com.arbfintech.framework.component.core.type.AjaxResult;
import com.arbfintech.microservice.customer.object.entity.CustomerEmploymentData;
import com.arbfintech.microservice.customer.object.entity.CustomerProfile;
import com.arbfintech.microservice.customer.restapi.repository.reader.CommonReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Jerry
 * @date 2021/12/14 16:07
 */
@Service
public class CustomerResourceService {

    @Autowired
    private CommonReader commonReader;

    public CustomerProfile getCustomerProfile(Long customerId) {
        return commonReader.getEntityByCustomerId(CustomerProfile.class, customerId);
    }

    public CustomerEmploymentData getCustomerEmploymentData(Long customerId) {
        return commonReader.getEntityByCustomerId(CustomerEmploymentData.class, customerId);
    }

    public String updateCustomerProfile(Long customerId, CustomerProfile customerProfile, JSONObject accountJson) {
        return AjaxResult.success();
    }

    public String updateCustomerEmploymentData(Long customerId, CustomerEmploymentData customerEmploymentData, JSONObject accountJson) {
        return AjaxResult.success();
    }
}
