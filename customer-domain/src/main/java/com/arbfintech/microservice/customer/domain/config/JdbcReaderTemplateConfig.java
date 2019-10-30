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
public class JdbcReaderTemplateConfig {
    //  panda
    @Bean(name = "pandaReaderJdbcTemplate")
    public JdbcTemplate pandaReaderJdbcTemplate(
            @Qualifier("pandaReaderDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean(name = "pandaReaderNamedJdbcTemplate")
    public NamedParameterJdbcTemplate pandaReaderNamedJdbcTemplate(
            @Qualifier("pandaReaderDataSource") DataSource dataSource) {
        return new NamedParameterJdbcTemplate(dataSource);
    }
}
