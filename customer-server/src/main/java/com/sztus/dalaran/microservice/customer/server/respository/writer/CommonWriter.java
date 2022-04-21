package com.sztus.dalaran.microservice.customer.server.respository.writer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.sztus.dalaran.microservice.customer.server.object.domain.Customer;
import com.sztus.framework.component.database.constant.ConditionTypeConst;
import com.sztus.framework.component.database.core.BaseJdbcWriter;
import com.sztus.framework.component.database.kit.SqlBuilder;
import com.sztus.framework.component.database.type.SqlOption;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public class CommonWriter extends BaseJdbcWriter {

    public <T> Long deleteByLoanId(Class<T> tClass, Long loanId) {
        SqlOption sqlOption = SqlOption.getInstance();
        if (tClass.equals(Customer.class)) {
            sqlOption.whereFormat(ConditionTypeConst.AND, "id = %d", loanId);
        } else {
            sqlOption.whereFormat(ConditionTypeConst.AND, "customer_id = %d", loanId);
        }

        return deleteByOptions(tClass, sqlOption.toString());
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
