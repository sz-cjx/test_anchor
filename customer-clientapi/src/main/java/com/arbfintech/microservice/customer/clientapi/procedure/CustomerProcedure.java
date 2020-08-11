package com.arbfintech.microservice.customer.clientapi.procedure;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.arbfintech.framework.component.core.constant.ConditionTypeConst;
import com.arbfintech.framework.component.core.constant.JsonKeyConst;
import com.arbfintech.framework.component.core.type.SqlOption;
import com.arbfintech.framework.component.core.util.StringUtil;
import com.arbfintech.framework.component.database.core.GeneralJdbcReader;
import com.arbfintech.framework.component.database.core.GeneralJdbcWriter;
import com.arbfintech.framework.component.database.core.SimpleJdbcReader;
import com.arbfintech.framework.component.database.core.SimpleJdbcWriter;
import com.arbfintech.microservice.customer.domain.entity.Customer;
import com.arbfintech.microservice.customer.domain.entity.CustomerOptInData;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.List;

@Repository
public class CustomerProcedure {

    @Autowired
    private SimpleJdbcReader simpleJdbcReader;

    @Autowired
    private SimpleJdbcWriter simpleJdbcWriter;


    public Long replaceCustomer(String dataStr) {
        return simpleJdbcWriter.save(Customer.class, dataStr, null);
    }

    public Customer getCustomerById(Long id) {
        return simpleJdbcReader.findById(Customer.class, id, null);
    }

    public List<Customer> listCustomerByOptions(String options) {
        SqlOption sqlOption = SqlOption.getInstance();
        JSONObject paramJson = JSON.parseObject(options);
        paramJson.keySet().forEach(key -> {
            String value = paramJson.getString(key);
            sqlOption.addWhereFormat(ConditionTypeConst.AND, StringUtil.formatCamelToUnderscore(key) + " = '%s'", value);
        });
        return simpleJdbcReader.findAllByOptions(Customer.class, sqlOption.toString());
    }

    public Customer getCustomerByOptions(String options) {
        SqlOption sqlOption = SqlOption.getInstance();
        JSONObject paramJson = JSON.parseObject(options);
        paramJson.keySet().forEach(key -> {
            String value = paramJson.getString(key);
            sqlOption.addWhereFormat(ConditionTypeConst.AND, StringUtil.formatCamelToUnderscore(key) + " = '%s'", value);
        });
        return simpleJdbcReader.findByOptions(Customer.class, sqlOption.toString());
    }

    public Long addCustomerOptInData(CustomerOptInData optInData) {
        return simpleJdbcWriter.save(CustomerOptInData.class, JSON.toJSONString(optInData));
    }

    public CustomerOptInData getCustomerOptInDataByCustomerId(Long customerId) {
        SqlOption optInOption = SqlOption.getInstance();
        optInOption.addWhereFormat(ConditionTypeConst.AND, "customer_id = '%d'", customerId);

        return simpleJdbcReader.findByOptions(CustomerOptInData.class, optInOption.toString());
    }

    public Long updateCustomerOptInData(CustomerOptInData optInData) {
        SqlOption optInOption = SqlOption.getInstance();
        optInOption.addWhereFormat(ConditionTypeConst.AND, "customer_id = '%d'", optInData);
        return simpleJdbcWriter.save(CustomerOptInData.class, JSON.toJSONString(optInData));
    }
}
