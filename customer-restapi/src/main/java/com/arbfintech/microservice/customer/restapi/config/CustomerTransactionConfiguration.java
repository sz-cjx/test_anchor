package com.arbfintech.microservice.customer.restapi.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

/**
 * @author Baron
 * @date 2021/5/14
 */
@Configuration
public class CustomerTransactionConfiguration {
    @Bean
    @ConditionalOnMissingBean
    public PlatformTransactionManager transactionManager(@Qualifier("masterWriterDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
}
