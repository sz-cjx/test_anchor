package com.sztus.microservice.customer.server.service;

import com.sztus.framework.component.core.type.ProcedureException;
import com.sztus.framework.component.core.type.SqlOption;
import com.sztus.framework.component.database.core.SimpleJdbcReader;
import com.sztus.microservice.customer.client.object.enumeration.CustomerErrorCode;
import com.sztus.microservice.customer.client.object.parameter.request.GetCustomerByConditionsRequest;
import com.sztus.microservice.customer.server.constant.DbKey;
import com.sztus.microservice.customer.server.domain.Customer;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomerService {

    public Customer getCustomerByEmail(String email) throws ProcedureException {

        if (StringUtils.isBlank(email)) {
            throw new ProcedureException(CustomerErrorCode.FAILURE_PARAMETER_IS_INCOMPLETE);
        }

        SqlOption sqlOption = SqlOption.getInstance();
        sqlOption.whereEqual(DbKey.EMAIL, email);

        return Optional
                .ofNullable(simpleJdbcReader.findByOptions(Customer.class, sqlOption.toString()))
                .orElseThrow(() -> new ProcedureException(CustomerErrorCode.CUSTOMER_IS_NOT_EXISTED));
    }

    @Autowired
    private SimpleJdbcReader simpleJdbcReader;

}
