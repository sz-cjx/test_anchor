package com.arbfintech.microservice.customer.restapi.repository;

import com.arbfintech.framework.component.core.annotation.Id;
import com.arbfintech.framework.component.core.util.StringUtil;
import com.arbfintech.framework.component.database.core.BaseJdbcWriter;
import com.arbfintech.microservice.customer.restapi.util.CustomerUtil;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.lang.reflect.Field;
import java.util.List;

/**
 * @author Fly_Roushan
 * @date 2021/1/7
 */
@Repository
public class CustomerWriter extends BaseJdbcWriter {
    public <E> Integer batchSave(List<E> dataList) {
        StringBuilder filedBuilder = new StringBuilder();
        StringBuilder paramBuilder = new StringBuilder();
        Class<?> clazz = dataList.get(0).getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            filedBuilder.append(StringUtil.formatCamelToUnderscore(field.getName())).append(",");
            paramBuilder.append(":").append(field.getName()).append(",");
        }

        String sql = String.format("INSERT INTO `%s` (%s) VALUES (%s)",
                StringUtil.formatCamelToUnderscore(dataList.get(0).getClass().getSimpleName()),
                CustomerUtil.removeLast(filedBuilder),
                CustomerUtil.removeLast(paramBuilder)
        );
        SqlParameterSource[] paramSourceArray = new SqlParameterSource[dataList.size()];
        for (int i = 0; i < dataList.size(); i++) {
            paramSourceArray[i] = new BeanPropertySqlParameterSource(dataList.get(i));
        }
        return namedJdbcTemplate().batchUpdate(sql, paramSourceArray).length;
    }

    public <E> Integer batchUpdateOptIn(List<E> dataList) {
        StringBuilder filedBuilder = new StringBuilder();
        StringBuilder whereBuilder = new StringBuilder();
        Class<?> clazz = dataList.get(0).getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            String fieldName = field.getName();
            Id idAnnotation = field.getDeclaredAnnotation(Id.class);
            if (idAnnotation != null) {
                whereBuilder.append(StringUtil.formatCamelToUnderscore(fieldName)).append("=:").append(fieldName).append(" AND ");
                continue;
            }
            if (!"createdAt".equals(fieldName)) {
                filedBuilder.append(StringUtil.formatCamelToUnderscore(fieldName)).append("=:").append(fieldName).append(",");
            }
        }

        String sql = String.format("UPDATE `%s` SET %s WHERE %s",
                StringUtil.formatCamelToUnderscore(dataList.get(0).getClass().getSimpleName()),
                CustomerUtil.removeLast(filedBuilder),
                CustomerUtil.removeLast(whereBuilder, " AND ")
        );

        SqlParameterSource[] paramSourceArray = new SqlParameterSource[dataList.size()];
        for (int i = 0; i < dataList.size(); i++) {
            paramSourceArray[i] = new BeanPropertySqlParameterSource(dataList.get(i));
        }
        int[] rowsArray = namedJdbcTemplate().batchUpdate(sql, paramSourceArray);
        return getRows(rowsArray);
    }

    public Integer getRows(int[] ints) {
        int i = 0;
        for (Integer row : ints) {
            i = row + i;
        }
        return i;
    }
}