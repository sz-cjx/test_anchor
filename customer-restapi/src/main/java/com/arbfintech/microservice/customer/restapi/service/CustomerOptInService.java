package com.arbfintech.microservice.customer.restapi.service;

import com.arbfintech.framework.component.core.type.ProcedureException;
import com.arbfintech.microservice.customer.object.entity.CustomerOptInData;
import com.arbfintech.microservice.customer.object.enumerate.CustomerErrorCode;
import com.arbfintech.microservice.customer.object.enumerate.CustomerOptInType;
import com.arbfintech.microservice.customer.object.enumerate.CustomerOptInValue;
import com.arbfintech.microservice.customer.object.util.ResultUtil;
import com.arbfintech.microservice.customer.restapi.repository.CustomerWriter;
import com.google.common.collect.Lists;
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

    //TODO 暂时使用这种方式, 如果新增Portfolio需要修改其中数值
    private static final List<Long> PORTFOLIO_ID_LIST = Lists.newArrayList(3L, 7L, 8L, 10L);

    public Integer initAllPortfolioOptIn(Long id) {
        List<CustomerOptInData> customerOptInDataList = new ArrayList<>();
        for (Long portfolioId : PORTFOLIO_ID_LIST) {
            customerOptInDataList.addAll(getDefaultOptInDataList(id, portfolioId));
        }
        return customerWriter.batchSave(customerOptInDataList);
    }

    public List<CustomerOptInData> createCustomerOptIn(Long id, Long portfolioId) throws ProcedureException {
        List<CustomerOptInData> customerOptInDataList = new ArrayList<>(getDefaultOptInDataList(id, portfolioId));
        ResultUtil.checkResult(customerWriter.batchSave(customerOptInDataList), CustomerErrorCode.CREATE_FAILURE_OPT_IN_SAVE);
        return customerOptInDataList;
    }

    public <E> Integer batchSave(List<E> dataList) {
        return customerWriter.batchSave(dataList);
    }

    public Integer updateCustomerOptInData(List<CustomerOptInData> dataList) {
        return customerWriter.batchUpdateOptIn(dataList);
    }

    private List<CustomerOptInData> getDefaultOptInDataList(Long id, Long portfolioId) {
        Long time = System.currentTimeMillis();
        Integer defaultValue = CustomerOptInValue.IS_MARKETING.getValue() + CustomerOptInValue.IS_OPERATION.getValue();
        List<CustomerOptInData> customerOptInDataList = new ArrayList<>();
        customerOptInDataList.add(new CustomerOptInData(id, portfolioId, CustomerOptInType.EMAIL.getValue(), defaultValue, time, time));
        customerOptInDataList.add(new CustomerOptInData(id, portfolioId, CustomerOptInType.HOME_PHONE.getValue(), 0, time, time));
        customerOptInDataList.add(new CustomerOptInData(id, portfolioId, CustomerOptInType.CELL_PHONE.getValue(), 0, time, time));

        return customerOptInDataList;
    }
}
