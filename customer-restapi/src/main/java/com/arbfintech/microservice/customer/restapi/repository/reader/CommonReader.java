package com.arbfintech.microservice.customer.restapi.repository.reader;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.arbfintech.framework.component.core.constant.ConditionTypeConst;
import com.arbfintech.framework.component.core.type.SqlOption;
import com.arbfintech.framework.component.database.core.BaseJdbcReader;
import com.arbfintech.microservice.customer.object.entity.CustomerOptIn;
import com.arbfintech.microservice.customer.object.util.AESCryptoUtil;
import com.arbfintech.microservice.customer.object.util.CustomerFieldKey;
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

        T entity = findByOptions(tClass, sqlOption.toString());
        if (CustomerFieldKey.getEncodeClassList().contains(tClass)) {
            JSONObject entityJson = JSON.parseObject(JSON.toJSONString(entity));
            JSONObject decryptJson = AESCryptoUtil.decryptData(entityJson);
            return JSONObject.parseObject(JSON.toJSONString(decryptJson), tClass);
        } else {
            return entity;
        }
    }
}
