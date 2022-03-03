package com.sztus.microservice.customer.server.service;

import com.sztus.framework.component.core.type.SqlOption;
import com.sztus.framework.component.database.core.SimpleJdbcReader;
import com.sztus.microservice.customer.server.constant.DbKey;
import com.sztus.microservice.customer.server.domain.CustomerAccountData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class CustomerAccountService {

    public CustomerAccountData getCustomerAccountByConditions(String openId) {
        LOGGER.info(">>> CustomerAccountService.getCustomerAccountByConditions");

        SqlOption sqlOption = SqlOption.getInstance();
        if (Objects.nonNull(openId)) {
            sqlOption.whereEqual(DbKey.OPEN_ID, openId);
        }

        return simpleJdbcReader.findByOptions(CustomerAccountData.class, sqlOption.toString());
    }

    @Autowired
    private SimpleJdbcReader simpleJdbcReader;

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerAccountService.class);
}
