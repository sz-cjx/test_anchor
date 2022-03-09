package com.sztus.microservice.customer.server.service;

import com.alibaba.fastjson.JSON;
import com.sztus.framework.component.core.constant.CodeConst;
import com.sztus.framework.component.core.type.ProcedureException;
import com.sztus.framework.component.core.type.SqlOption;
import com.sztus.framework.component.core.util.DateUtil;
import com.sztus.framework.component.core.util.UuidUtil;
import com.sztus.framework.component.database.core.SimpleJdbcReader;
import com.sztus.framework.component.database.core.SimpleJdbcWriter;
import com.sztus.microservice.customer.client.object.type.CustomerErrorCode;
import com.sztus.microservice.customer.server.business.DbKey;
import com.sztus.microservice.customer.server.domain.Customer;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class CustomerService {

    public Customer getCustomer(String email, String openId) throws ProcedureException {
        if (StringUtils.isBlank(email) && StringUtils.isBlank(openId)) {
            throw new ProcedureException(CustomerErrorCode.PARAMETER_IS_INCOMPLETE);
        }

        SqlOption sqlOption = SqlOption.getInstance();
        if (StringUtils.isNotBlank(email)) {
            sqlOption.whereEqual(DbKey.EMAIL, email);
        }

        if (StringUtils.isNotBlank(openId)) {
            sqlOption.whereEqual(DbKey.OPEN_ID, openId);
        }

        return Optional
                .ofNullable(jdbcReader.findByOptions(Customer.class, sqlOption.toString()))
                .orElseThrow(() -> new ProcedureException(CustomerErrorCode.CUSTOMER_IS_NOT_EXISTED));
    }

    /**
     * 条件： id, openId
     * 条件无值保存，条件有值，通过条件更新
     *
     * @param customer
     * @return
     */
    public Customer saveCustomer(Customer customer) throws ProcedureException {
        Long currentTimestamp = DateUtil.getCurrentTimestamp();
        customer.setUpdatedAt(currentTimestamp);
        Long id = customer.getId();
        String openId = customer.getOpenId();

        Long result = null;
        if (Objects.isNull(id) && StringUtils.isBlank(openId)) {
            // Save
            customer.setCreatedAt(currentTimestamp);
            customer.setOpenId(UuidUtil.getUuid());
            // TODO uniqueCode is null
            result = jdbcWriter.save(customer);
        } else {
            // Update
            SqlOption sqlOption = SqlOption.getInstance();
            if (Objects.nonNull(id)) {
                sqlOption.whereEqual(DbKey.ID, id);
            }

            if (StringUtils.isNotBlank(openId)) {
                sqlOption.whereEqual(DbKey.OPEN_ID, openId);
            }
            result = jdbcWriter.save(Customer.class, JSON.toJSONString(customer), sqlOption.toString());

        }
        if (Objects.isNull(result) || result < CodeConst.DEFAULT) {
            throw new ProcedureException(CustomerErrorCode.FAILED_TO_SAVE_CUSTOMER);
        }
        if (result >= CodeConst.SUCCESS && Objects.isNull(id)) {
            customer.setId(result);
        }

        return customer;
    }

    @Autowired
    private SimpleJdbcReader jdbcReader;

    @Autowired
    private SimpleJdbcWriter jdbcWriter;

}
