package com.arbfintech.microservice.customer.restapi.repository.reader;

import com.alibaba.fastjson.JSONArray;
import com.arbfintech.framework.component.database.core.BaseJdbcReader;
import com.arbfintech.microservice.customer.object.entity.CustomerDocumentData;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class CustomerDocumentReader extends BaseJdbcReader {

    public List<CustomerDocumentData> listDocument(Long customerId, List<Integer> documentType) {
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("SELECT * FROM customer_document_data");
        sqlBuilder.append(" WHERE customer_id = :customerId");
        sqlBuilder.append(" AND document_type IN (:documentType)");

        Map<String, Object> condition = new HashMap<>();
        condition.put("customerId", customerId);
        condition.put("documentType", documentType);

        JSONArray jsonArray = namedJdbcTemplate().query(sqlBuilder.toString(), condition, this::returnArray);
        List<CustomerDocumentData> list = new ArrayList<>();
        if (!CollectionUtils.isEmpty(jsonArray)) {
            list = jsonArray.toJavaList(CustomerDocumentData.class);
        }

        return list;
    }
}
