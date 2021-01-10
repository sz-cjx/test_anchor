package com.arbfintech.microservice.customer.restapi.service;

import com.arbfintech.microservice.customer.object.entity.CustomerOptInData;
import com.arbfintech.microservice.customer.object.enumerate.CustomerOptInType;
import com.arbfintech.microservice.customer.object.enumerate.CustomerOptInValue;
import com.arbfintech.microservice.customer.restapi.repository.CustomerWriter;
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
    private CustomerWriter customerWriter;

    public Integer initCustomerOptIn(Long id) {
        Long time = System.currentTimeMillis();
        Integer defaultValue = CustomerOptInValue.IS_MARKETING.getValue() + CustomerOptInValue.IS_OPERATION.getValue();
        List<CustomerOptInData> customerOptInDataList = new ArrayList<>();
        customerOptInDataList.add(new CustomerOptInData(id, CustomerOptInType.EMAIL.getValue(), defaultValue, time, time));
        customerOptInDataList.add(new CustomerOptInData(id, CustomerOptInType.HOME_PHONE.getValue(), defaultValue, time, time));
        customerOptInDataList.add(new CustomerOptInData(id, CustomerOptInType.CELL_PHONE.getValue(), defaultValue, time, time));
        return customerWriter.batchSave(customerOptInDataList);
    }

    public Integer updateCustomerOptInData(List<CustomerOptInData> dataList) {
        return customerWriter.batchUpdateOptIn(dataList);
    }
}
