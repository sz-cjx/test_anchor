package com.arbfintech.microservice.customer.restapi.service;

import com.arbfintech.framework.component.core.type.ProcedureException;
import com.arbfintech.microservice.customer.object.entity.CustomerOptInData;
import com.arbfintech.microservice.customer.object.enumerate.CustomerErrorCode;
import com.arbfintech.microservice.customer.object.enumerate.CustomerOptInType;
import com.arbfintech.microservice.customer.object.enumerate.CustomerOptInValue;
import com.arbfintech.microservice.customer.restapi.repository.CustomerWriter;
import com.arbfintech.microservice.customer.restapi.util.ResultUtil;
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

    public Integer initCustomerOptIn(Long id, Long portfolioId) {
        Long time = System.currentTimeMillis();
        Integer defaultValue = CustomerOptInValue.IS_MARKETING.getValue() + CustomerOptInValue.IS_OPERATION.getValue();
        List<CustomerOptInData> customerOptInDataList = new ArrayList<>();
        customerOptInDataList.add(new CustomerOptInData(id, portfolioId, CustomerOptInType.EMAIL.getValue(), defaultValue, time, time));
        customerOptInDataList.add(new CustomerOptInData(id, portfolioId, CustomerOptInType.HOME_PHONE.getValue(), 0, time, time));
        customerOptInDataList.add(new CustomerOptInData(id, portfolioId, CustomerOptInType.CELL_PHONE.getValue(), 0, time, time));
        return customerWriter.batchSave(customerOptInDataList);
    }

    public List<CustomerOptInData> createCustomerOptIn(Long id, Long portfolioId) throws ProcedureException {
        Long time = System.currentTimeMillis();
        Integer defaultValue = CustomerOptInValue.IS_MARKETING.getValue() + CustomerOptInValue.IS_OPERATION.getValue();
        List<CustomerOptInData> customerOptInDataList = new ArrayList<>();
        customerOptInDataList.add(new CustomerOptInData(id, portfolioId, CustomerOptInType.EMAIL.getValue(), defaultValue, time, time));
        customerOptInDataList.add(new CustomerOptInData(id, portfolioId, CustomerOptInType.HOME_PHONE.getValue(), 0, time, time));
        customerOptInDataList.add(new CustomerOptInData(id, portfolioId, CustomerOptInType.CELL_PHONE.getValue(), 0, time, time));
        ResultUtil.checkResult(customerWriter.batchSave(customerOptInDataList), CustomerErrorCode.CREATE_FAILURE_OPT_IN_SAVE);
        return customerOptInDataList;
    }

    public <E> Integer batchSave(List<E> dataList) {
        return customerWriter.batchSave(dataList);
    }

    public Integer updateCustomerOptInData(List<CustomerOptInData> dataList) {
        return customerWriter.batchUpdateOptIn(dataList);
    }
}
