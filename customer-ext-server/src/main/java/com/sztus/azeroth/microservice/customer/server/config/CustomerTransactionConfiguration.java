package com.sztus.azeroth.microservice.customer.server.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
public class CustomerTransactionConfiguration {
    @Bean
    @ConditionalOnMissingBean
    public PlatformTransactionManager transactionManager(@Qualifier("masterWriterDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
}
