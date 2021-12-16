package com.arbfintech.microservice.customer.restapi.repository.reader;

import com.arbfintech.framework.component.core.constant.ConditionTypeConst;
import com.arbfintech.framework.component.core.type.SqlOption;
import com.arbfintech.framework.component.database.core.BaseJdbcReader;
import com.arbfintech.microservice.customer.object.entity.CustomerOptIn;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CommonReader extends BaseJdbcReader {

    public <T> List<T> listEntityByCustomerId(Class<T> tClass, Long customerId) {
        SqlOption sqlOption = SqlOption.getInstance();
        sqlOption.whereFormat(ConditionTypeConst.AND, "customer_id = '%d'", customerId);
        return findAllByOptions(tClass, sqlOption.toString());
    }

    public <T> T getEntityByCustomerId(Class<T> tClass, Long customerId) {
        SqlOption sqlOption = SqlOption.getInstance();
        if (tClass.equals(CustomerOptIn.class)) {
            sqlOption.whereFormat(ConditionTypeConst.AND, "customer_id = '%d'", customerId);
        } else {
            sqlOption.whereFormat(ConditionTypeConst.AND, "id = '%d'", customerId);
        }
        return findByOptions(tClass, sqlOption.toString());
    }
}