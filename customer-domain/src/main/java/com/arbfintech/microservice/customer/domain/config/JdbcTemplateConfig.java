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
    //  panda
    @Bean(name = "panda-v2JdbcTemplate")
    public JdbcTemplate pandaJdbcTemplate(
            @Qualifier("panda-v2DataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean(name = "panda-v2NamedJdbcTemplate")
    public NamedParameterJdbcTemplate pandaNamedJdbcTemplate(
            @Qualifier("panda-v2DataSource") DataSource dataSource) {
        return new NamedParameterJdbcTemplate(dataSource);
    }
}
