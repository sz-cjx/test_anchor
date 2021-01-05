package com.arbfintech.microservice.customer.restapi.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.arbfintech.framework.component.core.type.ProcedureException;
import com.arbfintech.framework.component.database.core.SimpleJdbcReader;
import com.arbfintech.framework.component.database.core.SimpleJdbcWriter;
import com.arbfintech.microservice.customer.object.constant.CustomerJsonConst;
import com.arbfintech.microservice.customer.object.entity.CustomerOptIn;
import com.arbfintech.microservice.customer.object.enumerate.CustomerOptInType;
import com.arbfintech.microservice.customer.object.enumerate.CustomerOptInValue;
import com.arbfintech.microservice.customer.restapi.repository.CustomerReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * @author Fly_Roushan
 * @date 2020/12/17
 */
@Service
public class CustomerOptInService {

    @Autowired
    private SimpleJdbcReader simpleJdbcReader;

    @Autowired
    private SimpleJdbcWriter simpleJdbcWriter;

    @Autowired
    private CustomerReader customerReader;

    public Long initCustomerOptIn(Long id) {
        Long time = System.currentTimeMillis();
        Integer defaultValue = CustomerOptInValue.IS_MARKETING.getValue() + CustomerOptInValue.IS_OPERATION.getValue();
        List<CustomerOptIn> customerOptInList = new ArrayList<>();
        customerOptInList.add(new CustomerOptIn(id, CustomerOptInType.EMAIL.getValue(), defaultValue, time, time));
        customerOptInList.add(new CustomerOptIn(id, CustomerOptInType.TELEPHONE.getValue(), defaultValue, time, time));
        return simpleJdbcWriter.save(CustomerOptIn.class, JSON.toJSONString(customerOptInList));
    }

    public CustomerOptIn getCustomerOptInByCondition(String condition) throws ProcedureException {
        JSONObject requestDataJson = JSON.parseObject(condition);
        CustomerOptIn customerOptIn;
        // customerId == id
        Long customerId = Optional.ofNullable(requestDataJson)
                .map((source) -> requestDataJson.getLong(CustomerJsonConst.CUSTOMER_ID))
                .orElseThrow(() -> new ProcedureException("Lack of Customer Id param"));
        Long optInType = Optional.of(requestDataJson)
                .map((source) -> requestDataJson.getLong(CustomerJsonConst.OPT_IN_TYPE))
                .orElseThrow(() -> new ProcedureException("Lack of customer Opt-In type param"));

        customerOptIn = customerReader.getCustomerOptInByCondition(customerId, optInType);
        if (Objects.isNull(customerOptIn)) {
            throw new ProcedureException("Customer Opt-In Data does not exist");
        }
        return customerOptIn;
    }

}
