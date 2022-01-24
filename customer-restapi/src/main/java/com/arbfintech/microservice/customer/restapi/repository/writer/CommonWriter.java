package com.arbfintech.microservice.customer.restapi.repository.writer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.arbfintech.framework.component.database.core.BaseJdbcWriter;
import com.arbfintech.microservice.customer.object.constant.CustomerJsonKey;
import com.arbfintech.microservice.customer.object.entity.CustomerContactData;
import com.arbfintech.microservice.customer.object.util.AESCryptoUtil;
import com.arbfintech.microservice.customer.object.util.CustomerFieldKey;
import org.springframework.stereotype.Repository;

@Repository
public class CommonWriter extends BaseJdbcWriter {

    public <E> Long save(Class<E> entityClass, String entityStr) {
        if (CustomerContactData.class.equals(entityClass)) {
            JSONObject entityJson = JSON.parseObject(entityStr);
            entityJson.put(CustomerJsonKey.VALUE, AESCryptoUtil.AESDecrypt(entityJson.getString(CustomerJsonKey.VALUE)));
            return save(entityClass, JSON.toJSONString(entityJson), null);
        } else if (CustomerFieldKey.getEncodeClassList().contains(entityClass)) {
            JSONObject entityJson = JSON.parseObject(entityStr);
            JSONObject encryptJson = AESCryptoUtil.encryptData(entityJson);
            return save(entityClass, JSON.toJSONString(encryptJson), null);
        } else {
            return save(entityClass, entityStr, null);
        }
    }

    public <E> Long bathSave(Class<E> entityClass, String entityStr) {
        JSONArray entityArray = JSON.parseArray(entityStr);
        if (CustomerFieldKey.getEncodeClassList().contains(entityClass)) {
            JSONArray saveList = new JSONArray();
            for (int i = 0; i < entityArray.size(); i++) {
                JSONObject entityJson = entityArray.getJSONObject(i);
                if (CustomerContactData.class.equals(entityClass)) {
                    entityJson.put(CustomerJsonKey.VALUE, AESCryptoUtil.AESDecrypt(entityJson.getString(CustomerJsonKey.VALUE)));
                } else {
                    entityJson = AESCryptoUtil.encryptData(entityJson);
                }
                saveList.add(entityJson);
            }
            return save(entityClass, saveList.toJSONString());
        } else {
            return save(entityClass, entityStr);
        }
    }
}
