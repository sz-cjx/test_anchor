package com.sztus.dalaran.microservice.customer.server.service;

import com.sztus.dalaran.microservice.customer.client.object.type.CustomerErrorCode;
import com.sztus.dalaran.microservice.customer.server.business.DbKey;
import com.sztus.dalaran.microservice.customer.server.domain.Customer;
import com.sztus.dalaran.microservice.customer.server.domain.CustomerOptInData;
import com.sztus.framework.component.core.type.ProcedureException;
import com.sztus.framework.component.database.core.SimpleJdbcReader;
import com.sztus.framework.component.database.core.SimpleJdbcWriter;
import com.sztus.framework.component.database.type.SqlOption;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class CustomerOptInDataService {

    public List<CustomerOptInData> getOptInDataAsList(Long customerId) throws ProcedureException {
        if (Objects.isNull(customerId)) {
            throw new ProcedureException(CustomerErrorCode.PARAMETER_IS_INCOMPLETE);
        }

        return jdbcReader.findAllByOptions(CustomerOptInData.class, SqlOption.getInstance().whereEqual(DbKey.ID, customerId).toString());
    }

    public Customer saveCustomerOptInData(Customer customer) throws ProcedureException {
        return null;
    }

    @Autowired
    private SimpleJdbcReader jdbcReader;

    @Autowired
    private SimpleJdbcWriter jdbcWriter;
}
