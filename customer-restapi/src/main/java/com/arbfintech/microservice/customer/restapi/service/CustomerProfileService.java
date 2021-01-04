package com.arbfintech.microservice.customer.restapi.service;

import com.arbfintech.framework.component.core.constant.ConditionTypeConst;
import com.arbfintech.framework.component.core.type.SqlOption;
import com.arbfintech.framework.component.database.core.SimpleJdbcReader;
import com.arbfintech.microservice.customer.object.entity.CustomerProfile;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Fly_Roushan
 * @date 2021/1/4
 */
@Service
public class CustomerProfileService {

    @Autowired
    private SimpleJdbcReader simpleJdbcReader;

    public CustomerProfile searchCustomerProfile(Long id, String email) {
        SqlOption sqlOption = SqlOption.getInstance();
        if (StringUtils.isNotBlank(email)) {
            sqlOption.whereEqual("email", email, ConditionTypeConst.OR);
        }

        if (id != null) {
            sqlOption.whereEqual("id", id, ConditionTypeConst.OR);
        }

        sqlOption.page(0, 1);
        return simpleJdbcReader.findByOptions(CustomerProfile.class, sqlOption.toString());
    }
}
