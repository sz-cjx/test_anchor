package com.arbfintech.microservice.customer.restapi.repository.reader;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.arbfintech.framework.component.core.constant.ConditionTypeConst;
import com.arbfintech.framework.component.core.type.SqlOption;
import com.arbfintech.framework.component.database.core.BaseJdbcReader;
import com.arbfintech.microservice.customer.object.constant.CustomerJsonKey;
import com.arbfintech.microservice.customer.object.entity.CustomerContactData;
import com.arbfintech.microservice.customer.object.entity.CustomerCreditData;
import com.arbfintech.microservice.customer.object.entity.CustomerIbvData;
import com.arbfintech.microservice.customer.object.entity.CustomerOptIn;
import com.arbfintech.microservice.customer.object.util.AESCryptoUtil;
import com.arbfintech.microservice.customer.object.util.CustomerFieldKey;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

@Repository
public class CommonReader extends BaseJdbcReader {

    public <T> List<T> listEntityByCustomerId(Class<T> tClass, Long customerId) {
        SqlOption sqlOption = SqlOption.getInstance();
        sqlOption.whereFormat(ConditionTypeConst.AND, "customer_id = '%d'", customerId);

        List<T> entityList = findAllByOptions(tClass, sqlOption.toString());
        if (CollectionUtils.isEmpty(entityList)) {
            return null;
        }

        if (CustomerContactData.class.equals(tClass)) {
            JSONArray resultArray = new JSONArray();
            for (T entity : entityList) {
                JSONObject entityJson = JSON.parseObject(JSON.toJSONString(entity));
                entityJson.put(CustomerJsonKey.VALUE, AESCryptoUtil.AESDecrypt(entityJson.getString(CustomerJsonKey.VALUE)));
                resultArray.add(entityJson);
            }
            return resultArray.toJavaList(tClass);
        } else if (CustomerFieldKey.getEncodeClassList().contains(tClass)) {
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
        if (tClass.equals(CustomerOptIn.class) || tClass.equals(CustomerIbvData.class) || tClass.equals(CustomerCreditData.class)) {
            sqlOption.whereFormat(ConditionTypeConst.AND, "customer_id = '%d'", customerId);
        } else {
            sqlOption.whereFormat(ConditionTypeConst.AND, "id = '%d'", customerId);
        }

        T entity = findByOptions(tClass, sqlOption.toString());
        return decodeEntity(tClass, entity);
    }

    public <T> T getEntityByCondition(Class<T> tClass, Map<String, Object> condition) {
        SqlOption sqlOption = SqlOption.getInstance();

        if (CollectionUtils.isEmpty(condition)) {
            return null;
        }

        for (String key : condition.keySet()) {
            sqlOption.whereEqual(toUnderlineName(key), condition.get(key));
        }

        T entity = findByOptions(tClass, sqlOption.toString());
        return decodeEntity(tClass, entity);
    }

    private <T> T decodeEntity(Class<T> tClass, T entity) {
        if (CustomerContactData.class.equals(tClass)) {
            JSONObject entityJson = JSON.parseObject(JSON.toJSONString(entity));
            entityJson.put(CustomerJsonKey.VALUE, AESCryptoUtil.AESDecrypt(entityJson.getString(CustomerJsonKey.VALUE)));
            return JSONObject.parseObject(JSON.toJSONString(entityJson), tClass);
        } else if (CustomerFieldKey.getEncodeClassList().contains(tClass)) {
            JSONObject entityJson = JSON.parseObject(JSON.toJSONString(entity));
            JSONObject decryptJson = AESCryptoUtil.decryptData(entityJson);
            return JSONObject.parseObject(JSON.toJSONString(decryptJson), tClass);
        } else {
            return entity;
        }
    }

    private static String toUnderlineName(String s) {
        if (s == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        boolean upperCase = false;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            boolean nextUpperCase = true;
            if (i < (s.length() - 1)) {
                nextUpperCase = Character.isUpperCase(s.charAt(i + 1));
            }
            if (Character.isUpperCase(c)) {
                if (!upperCase || !nextUpperCase) {
                    if (i > 0) sb.append("_");
                }
                upperCase = true;
            } else {
                upperCase = false;
            }
            sb.append(Character.toLowerCase(c));
        }
        return sb.toString();
    }
}
