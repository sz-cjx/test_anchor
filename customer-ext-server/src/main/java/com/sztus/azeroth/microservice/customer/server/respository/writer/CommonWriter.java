package com.sztus.azeroth.microservice.customer.server.respository.writer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.sztus.azeroth.microservice.customer.client.object.util.EncryptUtil;
import com.sztus.azeroth.microservice.customer.server.object.domain.Customer;
import com.sztus.azeroth.microservice.customer.server.util.CustomerEncryptedFieldUtil;
import com.sztus.framework.component.database.constant.ConditionTypeConst;
import com.sztus.framework.component.database.core.BaseJdbcWriter;
import com.sztus.framework.component.database.kit.SqlBuilder;
import com.sztus.framework.component.database.type.SqlOption;
import com.sztus.framework.component.database.type.SqlTable;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class CommonWriter extends BaseJdbcWriter {

    public <T> Long saveEncodeEntity(Class<T> tClass, String entityStr) {
        JSONObject entityJson = JSON.parseObject(entityStr);
        for (String field : CustomerEncryptedFieldUtil.getEncodeFieldList()) {
            if (entityJson.containsKey(field) && !StringUtils.isEmpty(entityJson.getString(field))) {
                entityJson.put(field, EncryptUtil.AESEncode(entityJson.getString(field)));
            }
        }

        return save(tClass, entityJson.toJSONString());
    }

    public <T> Long deleteByCustomerId(Class<T> tClass, Long customerId) {
        SqlOption sqlOption = SqlOption.getInstance();
        if (tClass.equals(Customer.class)) {
            sqlOption.whereFormat(ConditionTypeConst.AND, "id = %d", customerId);
        } else {
            sqlOption.whereFormat(ConditionTypeConst.AND, "customer_id = %d", customerId);
        }

        return deleteByOptions(tClass, sqlOption.toString());
    }

    public <T> Integer deleteByIdList(Class<T> tClass, List<Long> deleteList) {
        SqlTable sqlTable = SqlTable.getInstance(tClass);
        StringBuffer sql = new StringBuffer();
        sql.append("DELETE FROM " + sqlTable.getTableName());
        if (tClass.equals(Customer.class)) {
            sql.append(" WHERE id IN (:deleteList) ");
        } else {
            sql.append(" WHERE customer_id IN (:deleteList) ");
        }

        Map<String, Object> paramMap = new HashMap<>(16);
        paramMap.put("deleteList", deleteList);

        return namedJdbcTemplate().update(
                sql.toString(),
                paramMap
        );

    }

    public <E> Long batchSaveList(Class<E> entityClass, Collection<E> collection) {
        return batchSave(entityClass, JSON.parseArray(JSON.toJSONString(collection, SerializerFeature.WriteMapNullValue)));
    }

    protected <E> Long batchSave(Class<E> entityClass, JSONArray entityArray) {
        long result = 0;

        if (entityArray.size() <= 0) {
            return result;
        }

        JSONArray entityForInserting = new JSONArray();

        entityArray.forEach(entityObject -> {
            JSONObject entityJson = (JSONObject) entityObject;
            entityForInserting.add(entityJson);
        });

        if (entityForInserting.size() > 0) {
            try (SqlBuilder sqlBuilder = new SqlBuilder()) {
                sqlBuilder.insertOrUpdate(entityClass);
                entityForInserting.forEach(entityObject -> {
                    JSONObject entityJson = (JSONObject) entityObject;
                    sqlBuilder.values(entityJson);
                });

                int[] affectedArray = namedJdbcTemplate().batchUpdate(
                        sqlBuilder.getSqlStatement(),
                        sqlBuilder.getSqlParameterSourceArray()
                );
                result += affectedArray.length;
            } catch (Exception e) {
                result = 0;
            }
        }
        return result;
    }
}
