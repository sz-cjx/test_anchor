package com.sztus.dalaran.microservice.customer.server.respository.reader;

import com.sztus.dalaran.microservice.customer.server.object.domain.Customer;
import com.sztus.dalaran.microservice.customer.server.object.domain.CustomerBankAccount;
import com.sztus.framework.component.database.constant.ConditionTypeConst;
import com.sztus.framework.component.database.core.BaseJdbcReader;
import com.sztus.framework.component.database.type.SqlOption;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CommonReader extends BaseJdbcReader {


    public <T> T getEntityByCustomerId(Class<T> tClass, Long customerId) {
        SqlOption sqlOption = SqlOption.getInstance();
        if (tClass.equals(Customer.class) || tClass.equals(CustomerBankAccount.class)) {
            sqlOption.whereFormat(ConditionTypeConst.AND, "id = %d", customerId);
        } else {
            sqlOption.whereFormat(ConditionTypeConst.AND, "customer_id = %d", customerId);
        }

        return findByOptions(tClass, sqlOption.toString());
    }

    public <T> List<T> listEntityByCustomerId(Class<T> tClass, Long customerId) {
        SqlOption sqlOption = SqlOption.getInstance();
        sqlOption.whereFormat(ConditionTypeConst.AND, "customer_id = %d", customerId);
        return findAllByOptions(tClass, sqlOption.toString());
    }

}
