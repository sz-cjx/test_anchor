package com.sztus.azeroth.microservice.customer.server.respository.reader;

import com.sztus.azeroth.microservice.customer.server.object.domain.Customer;
import com.sztus.framework.component.database.constant.ConditionTypeConst;
import com.sztus.framework.component.database.core.BaseJdbcReader;
import com.sztus.framework.component.database.type.SqlOption;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.Objects;

@Repository
public class CustomerReader extends BaseJdbcReader {

    public Customer getCustomerByCondition(Long id, String openId) {
        SqlOption sqlOption = SqlOption.getInstance();
        if (Objects.isNull(id) && Objects.isNull(openId)){
            return null;
        }
        if (Objects.nonNull(id)) {
            sqlOption.whereFormat(ConditionTypeConst.AND, "id = '%s'", id);
        }

        if (StringUtils.isNotBlank(openId)) {
            sqlOption.whereFormat(ConditionTypeConst.OR, "open_id = '%s'", openId);
        }

        return findByOptions(Customer.class, sqlOption.toString());
    }

}
