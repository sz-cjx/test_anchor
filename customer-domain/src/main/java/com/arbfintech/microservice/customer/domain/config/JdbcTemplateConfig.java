package com.arbfintech.microservice.customer.domain.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;

/**
 * @author CAVALIERS
 * 2019/9/28 2:56
 */
@Configuration
public class JdbcTemplateConfig {
    @Bean(name = "readerJdbcTemplate")
    public JdbcTemplate readerJdbcTemplate(
            @Qualifier("readerDataSource") DataSource dataSource
    ) {
        return new JdbcTemplate(dataSource);
    }

    @Bean(name = "readerNamedJdbcTemplate")
    public NamedParameterJdbcTemplate readerNamedJdbcTemplate(
            @Qualifier("readerDataSource") DataSource dataSource
    ) {
        return new NamedParameterJdbcTemplate(dataSource);
    }

    @Bean(name = "writerJdbcTemplate")
    public JdbcTemplate writerJdbcTemplate(
            @Qualifier("writerDataSource") DataSource dataSource
    ) {
        return new JdbcTemplate(dataSource);
    }

    @Bean(name = "writerNamedJdbcTemplate")
    public NamedParameterJdbcTemplate writerNamedJdbcTemplate(
            @Qualifier("writerDataSource") DataSource dataSource
    ) {
        return new NamedParameterJdbcTemplate(dataSource);
    }
}
