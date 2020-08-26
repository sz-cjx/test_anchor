package com.arbfintech.microservice.customer.clientapi.future;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.arbfintech.framework.component.core.constant.CodeConst;
import com.arbfintech.framework.component.core.type.AjaxResult;
import com.arbfintech.framework.component.core.util.DateUtil;
import com.arbfintech.framework.component.database.core.SimpleProcedure;
import com.arbfintech.microservice.customer.domain.entity.CustomerOptInData;
import com.arbfintech.microservice.customer.object.constant.CustomerJsonConst;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class CustomerOptInFuture {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerOptInFuture.class);

    @Autowired
    private SimpleProcedure simpleProcedure;

    public String replaceCustomerOptInData(String dataStr) {
        try {
            JSONArray dataArray = JSON.parseArray(dataStr);
            dataArray.forEach(dataObject -> {
                JSONObject dataJson = (JSONObject) dataObject;
                Long id = dataJson.getLong(CustomerJsonConst.ID);
                Long currentTimestamp = DateUtil.getCurrentTimestamp();
                if (Objects.isNull(id)) {
                    dataJson.put(CustomerJsonConst.CREATED_AT, currentTimestamp);
                }
                dataJson.put(CustomerJsonConst.UPDATED_AT, currentTimestamp);
            });

            Long resultCode = simpleProcedure.save(CustomerOptInData.class, dataArray.toJSONString());
            if (resultCode < CodeConst.SUCCESS) {
                LOGGER.warn("[Replace Customer Opt-In Data]Failed to replace customer opt-in data. Request Parameters:{}", dataStr);
                return AjaxResult.failure();
            }
            return AjaxResult.success();
        } catch (Exception e) {
            return AjaxResult.failure();
        }
    }
}
