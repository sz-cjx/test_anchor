package com.sztus.azeroth.microservice.customer.server.respository.reader;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sztus.azeroth.microservice.customer.client.object.util.EncryptUtil;
import com.sztus.azeroth.microservice.customer.server.object.domain.Customer;
import com.sztus.azeroth.microservice.customer.server.object.domain.CustomerBankAccount;
import com.sztus.azeroth.microservice.customer.server.object.domain.CustomerContactInfo;
import com.sztus.azeroth.microservice.customer.server.object.domain.CustomerIdentityInfo;
import com.sztus.azeroth.microservice.customer.server.object.util.CustomerEncryptedFieldUtil;
import com.sztus.framework.component.database.constant.ConditionTypeConst;
import com.sztus.framework.component.database.core.BaseJdbcReader;
import com.sztus.framework.component.database.type.SqlOption;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

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

    public <T> T getEntityWithDecrypt(Class<T> tClass, SqlOption sqlOption) {

        T entity = findByOptions(tClass, sqlOption.toString());

        if (CustomerIdentityInfo.class.equals(tClass) || CustomerContactInfo.class.equals(tClass)) {
            JSONObject entityJson = JSON.parseObject(JSON.toJSONString(entity));
            if (!CollectionUtils.isEmpty(entityJson)) {
                for (String field : CustomerEncryptedFieldUtil.getEncodeFieldList()) {
                    if (entityJson.containsKey(field) && StringUtils.isNotEmpty(entityJson.getString(field))) {
                        entityJson.put(field, EncryptUtil.AESDecode(entityJson.getString(field)));
                    }
                }
            }
            entity = JSONObject.parseObject(JSON.toJSONString(entityJson), tClass);
        }

        return entity;
    }

}
