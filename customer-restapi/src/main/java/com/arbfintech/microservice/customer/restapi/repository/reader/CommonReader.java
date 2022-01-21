package com.arbfintech.microservice.customer.restapi.repository.reader;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.arbfintech.framework.component.core.constant.ConditionTypeConst;
import com.arbfintech.framework.component.core.type.SqlOption;
import com.arbfintech.framework.component.database.core.BaseJdbcReader;
import com.arbfintech.microservice.customer.object.entity.CustomerDecisionLogicAuthorizationRecord;
import com.arbfintech.microservice.customer.object.entity.CustomerOptIn;
import com.arbfintech.microservice.customer.object.util.AESCryptoUtil;
import com.arbfintech.microservice.customer.object.util.CustomerFieldKey;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Repository
public class CommonReader extends BaseJdbcReader {

    public <T> List<T> listEntityByCustomerId(Class<T> tClass, Long customerId) {
        SqlOption sqlOption = SqlOption.getInstance();
        sqlOption.whereFormat(ConditionTypeConst.AND, "customer_id = '%d'", customerId);

        List<T> entityList = findAllByOptions(tClass, sqlOption.toString());
        if (CollectionUtils.isEmpty(entityList)) {
            return null;
        }

        if (CustomerFieldKey.getEncodeClassList().contains(tClass)) {
            JSONArray resultArray = new JSONArray();
            for (T entity : entityList) {
                JSONObject entityJson = JSON.parseObject(JSON.toJSONString(entity));
                JSONObject decryptJson = AESCryptoUtil.decryptData(entityJson);
                resultArray.add(decryptJson);
            }
            return resultArray.toJavaList(tClass);
        } else {
            return entityList;
        }
    }

    public <T> T getEntityByCustomerId(Class<T> tClass, Long customerId) {
        SqlOption sqlOption = SqlOption.getInstance();
        if (tClass.equals(CustomerOptIn.class) || tClass.equals(CustomerDecisionLogicAuthorizationRecord.class)) {
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
