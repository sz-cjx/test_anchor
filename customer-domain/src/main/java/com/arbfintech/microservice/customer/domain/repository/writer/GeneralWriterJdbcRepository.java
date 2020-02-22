package com.arbfintech.microservice.customer.domain.repository.writer;

import com.arbfintech.framework.component.core.base.BaseJdbcRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * general JDBC reader
 */
@Repository
public class GeneralWriterJdbcRepository extends BaseJdbcRepository {

    @Override
    protected JdbcTemplate jdbcTemplate() {
        return jdbcTemplate;
    }

    @Override
    protected NamedParameterJdbcTemplate namedJdbcTemplate() {
        return namedJdbcTemplate;
    }

    @Autowired
    @Qualifier("writerJdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    @Autowired
    @Qualifier("writerNamedJdbcTemplate")
    private NamedParameterJdbcTemplate namedJdbcTemplate;

}
