package com.arbfintech.microservice.customer.domain.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

/**
 * @author CAVALIERS
 * 2019/9/28 2:56
 */
@Configuration
public class DataSourceConfig {

    @Primary
    @Bean(name = "readerDataSource")
    @Qualifier("readerDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.reader")
    public DataSource readerDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "writerDataSource")
    @Qualifier("writerDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.writer")
    public DataSource writerDataSource() {
        return DataSourceBuilder.create().build();
    }

}
