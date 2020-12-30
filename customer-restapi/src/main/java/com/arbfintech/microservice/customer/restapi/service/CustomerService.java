package com.arbfintech.microservice.customer.restapi.service;

import com.arbfintech.framework.component.core.constant.ConditionTypeConst;
import com.arbfintech.framework.component.core.type.SqlOption;
import com.arbfintech.framework.component.database.core.SimpleJdbcReader;
import com.arbfintech.framework.component.database.core.SimpleJdbcWriter;
import com.arbfintech.microservice.customer.object.entity.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Fly_Roushan
 * @date 2020/12/17
 */
@Service
public class CustomerService {

    @Autowired
    private SimpleJdbcReader simpleJdbcReader;

    @Autowired
    private SimpleJdbcWriter simpleJdbcWriter;

    public Long addCustomer(Customer customer) {
        return simpleJdbcWriter.save(customer);
    }

    public Customer searchCustomer(String email) {
        return simpleJdbcReader.findByOptions(Customer.class,
                SqlOption.getInstance().whereEqual("email", email, null).toString()
        );
    }

    public Customer checkCustomer(String email, String ssn) {
        SqlOption sqlOption = SqlOption.getInstance();

        sqlOption.whereEqual("email", email, ConditionTypeConst.OR);
        sqlOption.whereEqual("ssn", ssn, ConditionTypeConst.OR);
        return simpleJdbcReader.findByOptions(Customer.class,
                sqlOption.toString()
        );
    }
}
