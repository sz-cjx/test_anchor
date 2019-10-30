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
    @Bean(name = "pandaWriterDataSource")
    @Qualifier("pandaWriterDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.writer")
    public DataSource getPandaWriterDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "pandaReaderDataSource")
    @Qualifier("pandaReaderDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.reader")
    public DataSource getPandaReaderDataSource() {
        return DataSourceBuilder.create().build();
    }
}
