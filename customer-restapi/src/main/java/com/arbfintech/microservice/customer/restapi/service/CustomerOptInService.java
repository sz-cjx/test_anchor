package com.arbfintech.microservice.customer.restapi.service;

import com.alibaba.fastjson.JSON;
import com.arbfintech.framework.component.database.core.SimpleJdbcReader;
import com.arbfintech.framework.component.database.core.SimpleJdbcWriter;
import com.arbfintech.microservice.customer.object.entity.CustomerOptIn;
import com.arbfintech.microservice.customer.object.enumerate.CustomerOptInTypeEnum;
import com.arbfintech.microservice.customer.object.enumerate.CustomerOptInValueEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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

    public Long initCustomerOptIn(Long id) {
        Long time = System.currentTimeMillis();
        Integer defaultValue = CustomerOptInValueEnum.IS_MARKETING.getValue() + CustomerOptInValueEnum.IS_OPERATION.getValue();
        List<CustomerOptIn> customerOptInList = new ArrayList<>();
        customerOptInList.add(new CustomerOptIn(id, CustomerOptInTypeEnum.EMAIL.getValue(), defaultValue, time, time));
        customerOptInList.add(new CustomerOptIn(id, CustomerOptInTypeEnum.TELEPHONE.getValue(), defaultValue, time, time));
        return simpleJdbcWriter.save(CustomerOptIn.class, JSON.toJSONString(customerOptInList));
    }
}
