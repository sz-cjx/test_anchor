package com.sztus.dalaran.microservice.customer.server.service;

import com.alibaba.fastjson.JSON;
import com.sztus.dalaran.microservice.customer.client.object.parameter.enumerate.CustomerErrorCode;
import com.sztus.dalaran.microservice.customer.server.domain.Customer;
import com.sztus.dalaran.microservice.customer.server.domain.CustomerContactData;
import com.sztus.dalaran.microservice.customer.server.domain.CustomerPersonalData;
import com.sztus.framework.component.core.constant.StatusConst;
import com.sztus.framework.component.core.type.ProcedureException;
import com.sztus.framework.component.database.constant.ConditionTypeConst;
import com.sztus.framework.component.database.core.SimpleProcedure;
import com.sztus.framework.component.database.type.SqlOption;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class CustomerGeneralService {

    @Autowired
    private SimpleProcedure simpleProcedure;

    public Customer getCustomerByCustomerId(Long customerId) {
        return simpleProcedure.findById(Customer.class, customerId);
    }

    public CustomerContactData getCustomerContactByContact(String contactInformation) {
        SqlOption sqlOption = SqlOption.getInstance();
        sqlOption.whereFormat(ConditionTypeConst.AND, "contact_information = '%s'", contactInformation);
        return simpleProcedure.findByOptions(CustomerContactData.class, sqlOption.toString());
    }

    public CustomerPersonalData getCustomerPersonalDataByCustomerId(Long customerId) throws ProcedureException {
        if (Objects.isNull(customerId)){
            throw new ProcedureException(CustomerErrorCode.PARAMETER_IS_INCOMPLETE);
        }
        SqlOption instance = SqlOption.getInstance();
        instance.whereFormat(ConditionTypeConst.AND,"customer_id= %d",customerId);
        return simpleProcedure.findByOptions(CustomerPersonalData.class,instance.toString());
    }

    public Long saveCustomerPersonalData(CustomerPersonalData personalData) throws ProcedureException {
        if (Objects.isNull(personalData.getCustomerId())){
            throw  new ProcedureException(CustomerErrorCode.PARAMETER_IS_INCOMPLETE);
        }
       return simpleProcedure.save(CustomerPersonalData.class, JSON.toJSONString(personalData));
    }
}
