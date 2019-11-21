package com.arbfintech.microservice.customer.domain.repositoryReader;

import com.alibaba.fastjson.JSONArray;
import com.arbfintech.framework.component.core.base.BaseJdbcRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author CAVALIERS
 * 2019/9/28 2:57
 */
@Repository
public class PandaReaderRepository extends BaseJdbcRepository {
    private Logger logger = LoggerFactory.getLogger(PandaReaderRepository.class);

    @Override
    protected JdbcTemplate jdbcTemplate() {
        return jdbcTemplate;
    }

    @Override
    protected NamedParameterJdbcTemplate namedJdbcTemplate() {
        return namedJdbcTemplate;
    }

    @Autowired
    @Qualifier("pandaReaderJdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    @Autowired
    @Qualifier("pandaReaderNamedJdbcTemplate")
    private NamedParameterJdbcTemplate namedJdbcTemplate;

    public JSONArray listCustomerBySSN(String ssn) {
        String sql = String.format("SELECT * FROM customer WHERE ssn = '%s' ORDER BY create_time DESC ; ", ssn);
        return namedJdbcTemplate.query(sql, resultSet -> {
            return extractArray(resultSet);
        });
    }

    public Long getTheLatestCustomerIdBySSN(String ssn) {
        String sql = String.format("SELECT id FROM customer WHERE ssn = '%s' ORDER BY create_time DESC LIMIT 1; ", ssn);
        return namedJdbcTemplate.query(sql, (ResultSet resultSet) -> getResultSet(resultSet));
    }

    public Long getLatestCustomerIdByEmailOrSsn(String email, String ssn) {
        String sql = String.format("SELECT id FROM customer WHERE email = '%s' OR ssn = '%s'  ORDER BY create_time DESC LIMIT 1; ", email, ssn);
        return namedJdbcTemplate.query(sql, (ResultSet resultSet) -> getResultSet(resultSet));
    }

    private Long getResultSet(ResultSet resultSet) throws SQLException {
        if (resultSet.next()) {
            return resultSet.getLong(1);
        } else {
            return null;
        }
    }

    public Long getLatestCustomerIdByUniqueKey(String uniqueKey) {
        String sql = String.format("SELECT id FROM customer WHERE unique_key = '%s'  ORDER BY create_time DESC LIMIT 1; ", uniqueKey);
        return namedJdbcTemplate.query(sql, (ResultSet resultSet) -> getResultSet(resultSet));
    }

//    public Long saveCustomerByJDBC(JSONObject customerJson) {
//        try {
//            final String sql = "insert into customer(cell_phone,home_phone,ssn,email,create_time,update_time) "
//                    + "values (?,?,?,?,?,?)";
//            KeyHolder keyHolder = new GeneratedKeyHolder();
//            jdbcTemplate.update(con -> {
//                PreparedStatement ps = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
//                ps.setString(1, customerJson.getString(JsonKeyConst.CELL_PHONE));
//                ps.setString(2,  customerJson.getString(JsonKeyConst.HOME_PHONE));
//                ps.setLong(3, customerJson.getLong(JsonKeyConst.SSN));
//                ps.setString(4,  customerJson.getString(JsonKeyConst.EMAIL));
//                ps.setLong(5,System.currentTimeMillis());
//                ps.setLong(6,System.currentTimeMillis());
//                return ps;
//            }, keyHolder);
//
//            return keyHolder.getKey().longValue();
//        } catch (Exception e) {
//            return 0L;
//        }
//
//
//    }
}
