package com.arbfintech.microservice.customer.restapi.component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.arbfintech.microservice.customer.object.entity.CustomerOperationLog;
import com.arbfintech.microservice.customer.object.enumerate.CustomerEventTypeEnum;
import com.arbfintech.microservice.customer.restapi.repository.writer.CommonWriter;
import com.arbfintech.microservice.origination.object.constant.OriginationJsonKey;
import com.arbfintech.microservice.origination.object.util.DataProcessingUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

@Component
public class SystemLogComponent {

    @Autowired
    private CommonWriter commonWriter;

    public void addSystemLog(Long customerId, JSONObject logData, Integer logType, JSONObject original, JSONObject current, Long timestamp) {
        CustomerOperationLog customerOperationLog = new CustomerOperationLog();

        customerOperationLog.setCustomerId(customerId);
        customerOperationLog.setLogType(logType);
        customerOperationLog.setOperatedAt(timestamp);

        if (!CollectionUtils.isEmpty(logData)) {
            customerOperationLog.setLogData(JSONObject.toJSONString(logData));
        }

        if (!CollectionUtils.isEmpty(current)) {
            customerOperationLog.setCurrent(JSON.toJSONString(current));
        }
        if (!CollectionUtils.isEmpty(original)) {
            customerOperationLog.setOriginal(JSON.toJSONString(original));
        }

        if (!(CustomerEventTypeEnum.CUSTOMER_REGISTRY_CHANGE.getValue().equals(logType)
                && CollectionUtils.isEmpty(current) && CollectionUtils.isEmpty(original))) {
            commonWriter.save(customerOperationLog);
        }
    }

    public void sysLogHandleFactory(Long customerId, JSONObject logData, JSONObject originalData, JSONObject currentData,  Long timestamp) {
        CompletableFuture.runAsync(() -> {
            JSONObject originalLogJson = null;
            JSONObject comparativeDataJson;
            JSONObject currentLogJson;

            if (CollectionUtils.isEmpty(originalData)) {
                comparativeDataJson = DataProcessingUtil.extractComparativeData(currentData, null);
            } else {
                comparativeDataJson = DataProcessingUtil.extractComparativeData(currentData, originalData);
                originalLogJson = comparativeDataJson.getJSONObject(OriginationJsonKey.ORIGINAL);
            }
            currentLogJson = comparativeDataJson.getJSONObject(OriginationJsonKey.CURRENT);

            if (currentLogJson.size() > 0) {
                addSystemLog(customerId, logData, CustomerEventTypeEnum.CUSTOMER_REGISTRY_CHANGE.getValue(),
                        Objects.isNull(originalLogJson) ? null : originalLogJson, currentLogJson, timestamp);
            }
        });
    }

}
