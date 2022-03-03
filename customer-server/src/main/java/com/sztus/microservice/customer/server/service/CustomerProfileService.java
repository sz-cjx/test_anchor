package com.sztus.microservice.customer.server.service;

import com.sztus.framework.component.core.type.SqlOption;
import com.sztus.framework.component.database.core.SimpleJdbcReader;
import com.sztus.microservice.customer.server.constant.DbKey;
import com.sztus.microservice.customer.server.domain.CustomerProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class CustomerProfileService {

    public CustomerProfile getCustomerProfileByConditions(String email) {
        LOGGER.info(">>> CustomerAccountService.getCustomerProfileByConditions");

        SqlOption sqlOption = SqlOption.getInstance();
        if (Objects.nonNull(email)) {
            sqlOption.whereEqual(DbKey.EMAIL, email);
        }

        return simpleJdbcReader.findByOptions(CustomerProfile.class, sqlOption.toString());
    }

    @Autowired
    private SimpleJdbcReader simpleJdbcReader;

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerProfileService.class);
}
