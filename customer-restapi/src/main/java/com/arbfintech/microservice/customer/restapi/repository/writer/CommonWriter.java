package com.arbfintech.microservice.customer.restapi.repository.writer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.arbfintech.framework.component.database.core.BaseJdbcWriter;
import com.arbfintech.microservice.customer.object.util.AESCryptoUtil;
import org.springframework.stereotype.Repository;

@Repository
public class CommonWriter extends BaseJdbcWriter {

    public <E> Long save(Class<E> entityClass, String entityStr) {
        JSONObject entityJson = JSON.parseObject(entityStr);
        JSONObject encryptJson = AESCryptoUtil.encryptData(entityJson);
        return save(entityClass, JSON.toJSONString(encryptJson), null);
    }
}
