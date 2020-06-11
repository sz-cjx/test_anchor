package com.arbfintech.microservice.customer.domain.repository;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.arbfintech.framework.component.core.constant.ConditionTypeConst;
import com.arbfintech.framework.component.core.type.SqlOption;
import com.arbfintech.framework.component.core.util.StringUtil;
import com.arbfintech.framework.component.database.core.GeneralJdbcReader;
import com.arbfintech.framework.component.database.core.GeneralJdbcWriter;
import com.arbfintech.microservice.customer.domain.entity.Customer;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CustomerRepository {

    @Autowired
    private GeneralJdbcReader generalJdbcReader;

    @Autowired
    private GeneralJdbcWriter generalJdbcWriter;

    public Long replaceCustomer(String dataStr) {
        return generalJdbcWriter.save(Customer.class, dataStr, null);
    }

    public String getCustomerById(Long id) {
        return generalJdbcReader.findById(Customer.class, id, null);
    }

    public String listCustomerByOptions(String options) {
        SqlOption sqlOption = SqlOption.getInstance();
        JSONObject paramJson = JSON.parseObject(options);
        paramJson.keySet().forEach(key -> {
            String value = paramJson.getString(key);
            sqlOption.addWhereFormat(ConditionTypeConst.AND, StringUtil.formatCamelToUnderscore(key) + " = '%s'", value);
        });
        return generalJdbcReader.findAllByOptions(Customer.class, sqlOption.toString());
    }

    public String findCustomerByOptions(String options) {
        SqlOption sqlOption = SqlOption.getInstance();
        JSONObject paramJson = JSON.parseObject(options);
        paramJson.keySet().forEach(key -> {
            String value = paramJson.getString(key);
            sqlOption.addWhereFormat(ConditionTypeConst.AND, StringUtil.formatCamelToUnderscore(key) + " = '%s'", value);
        });
        return generalJdbcReader.findByOptions(Customer.class, sqlOption.toString());
    }
}
