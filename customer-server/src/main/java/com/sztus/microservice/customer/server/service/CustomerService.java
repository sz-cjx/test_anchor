package com.sztus.microservice.customer.server.service;

import com.sztus.framework.component.core.constant.CodeConst;
import com.sztus.framework.component.core.type.ProcedureException;
import com.sztus.framework.component.core.type.SqlOption;
import com.sztus.framework.component.core.util.DateUtil;
import com.sztus.framework.component.core.util.UuidUtil;
import com.sztus.framework.component.database.core.SimpleJdbcReader;
import com.sztus.framework.component.database.core.SimpleJdbcWriter;
import com.sztus.microservice.customer.client.object.business.CustomerErrorCode;
import com.sztus.microservice.customer.server.business.DbKey;
import com.sztus.microservice.customer.server.domain.Customer;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class CustomerService {

    public Customer getCustomer(String email) throws ProcedureException {
        if (StringUtils.isBlank(email)) {
            throw new ProcedureException(CustomerErrorCode.PARAMETER_IS_INCOMPLETE);
        }

        SqlOption sqlOption = SqlOption.getInstance();
        sqlOption.whereEqual(DbKey.EMAIL, email);

        return Optional
                .ofNullable(jdbcReader.findByOptions(Customer.class, sqlOption.toString()))
                .orElseThrow(() -> new ProcedureException(CustomerErrorCode.CUSTOMER_IS_NOT_EXISTED));
    }

    public Customer saveCustomer(Customer customer) throws ProcedureException {
        Long currentTimestamp = DateUtil.getCurrentTimestamp();
        customer.setUpdatedAt(currentTimestamp);

        if (Objects.isNull(customer.getId())) {
            customer.setCreatedAt(currentTimestamp);
            customer.setOpenId(UuidUtil.getUuid());
            // TODO uniqueCode is null
        } else {
            Customer originalCustomer = jdbcReader.findById(Customer.class, customer.getId(), null);
            if (Objects.isNull(originalCustomer)) {
                throw new ProcedureException(CustomerErrorCode.CUSTOMER_IS_NOT_EXISTED);
            }
        }

        Long result = jdbcWriter.save(customer);
        if (result < CodeConst.DEFAULT) {
            throw new ProcedureException(CustomerErrorCode.FAILED_TO_SAVE_CUSTOMER);
        }
        if (result > CodeConst.SUCCESS) {
            customer.setId(result);
        }

        return customer;
    }

    @Autowired
    private SimpleJdbcReader jdbcReader;

    @Autowired
    private SimpleJdbcWriter jdbcWriter;

}
