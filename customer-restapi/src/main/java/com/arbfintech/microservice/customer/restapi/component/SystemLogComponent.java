package com.arbfintech.microservice.customer.restapi.component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.arbfintech.microservice.customer.object.entity.CustomerOperationLog;
import com.arbfintech.microservice.customer.restapi.repository.writer.CommonWriter;
import com.arbfintech.microservice.loan.object.enumerate.EventTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
public class SystemLogComponent {

    @Autowired
    private CommonWriter commonWriter;

    public void addSystemLog(Long customerId, JSONObject logData, Integer logType, JSONObject original, JSONObject current, Long timestamp) {
        CustomerOperationLog customerOperationLog = new CustomerOperationLog();

        customerOperationLog.setCustomerId(customerId);
        customerOperationLog.setLogType(logType);
        customerOperationLog.setOperatedAt(timestamp);

        if (CollectionUtils.isEmpty(logData)) {
            customerOperationLog.setLogData(JSONObject.toJSONString(logData));
        }

        if (!CollectionUtils.isEmpty(current)) {
            customerOperationLog.setCurrent(JSON.toJSONString(current));
        }
        if (!CollectionUtils.isEmpty(original)) {
            customerOperationLog.setOriginal(JSON.toJSONString(original));
        }

        if (!(EventTypeEnum.LOAN_REGISTRY_CHANGE.getValue().equals(logType)
                && CollectionUtils.isEmpty(current) && CollectionUtils.isEmpty(original))) {
            commonWriter.save(customerOperationLog);
        }
    }


}
