package com.arbfintech.microservice.customer.clientapi.future;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.arbfintech.framework.component.core.constant.ConditionTypeConst;
import com.arbfintech.framework.component.core.type.SqlOption;
import com.arbfintech.framework.component.database.core.SimpleProcedure;
import com.arbfintech.microservice.customer.domain.entity.Customer;
import com.arbfintech.microservice.customer.object.constant.CustomerJsonConst;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * @author Wade He
 * @version 1.0
 * @date 10/14/2020 9:33 AM
 */
@Component
public class CustomerFuture {

    private static final Logger logger = LoggerFactory.getLogger(CustomerFuture.class);

    @Autowired
    private SimpleProcedure simpleProcedure;

    public String getLatestCustomer(String dataStr) {
        logger.info("Start to find the latest customer :{}", dataStr);
        JSONObject dataJson = JSON.parseObject(dataStr);
        String ssn = dataJson.getString(CustomerJsonConst.SSN);
        String email = dataJson.getString(CustomerJsonConst.EMAIL);
        if(StringUtils.isEmpty(ssn) && StringUtils.isEmpty(email)){
            return new JSONObject().toJSONString();
        }

        if(StringUtils.isBlank(ssn)){
            ssn="";
        }

        if(StringUtils.isBlank(email)){
            email="";
        }

        String whereFormatStr = "(email like '%s' OR ssn like '%s')";
        SqlOption sqlOption = SqlOption.getInstance();
        sqlOption.addOrder("id DESC");
        sqlOption.addPage("LIMIT 1");
        sqlOption.addWhereFormat(ConditionTypeConst.AND, whereFormatStr, ssn, email);
        Customer customer = simpleProcedure.findByOptions(Customer.class, sqlOption.toString());
        if(Objects.isNull(customer)){
            return new JSONObject().toJSONString();
        }
        logger.info("Found the customer info:{}", customer.getId());
        return JSON.toJSONString(customer);
    }
}
