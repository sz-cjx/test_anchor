package com.sztus.dalaran.microservice.customer.server.service;

import com.sztus.dalaran.microservice.customer.server.domain.Customer;
import com.sztus.dalaran.microservice.customer.server.domain.CustomerContactData;
import com.sztus.framework.component.core.constant.StatusConst;
import com.sztus.framework.component.database.constant.ConditionTypeConst;
import com.sztus.framework.component.database.core.SimpleProcedure;
import com.sztus.framework.component.database.type.SqlOption;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
