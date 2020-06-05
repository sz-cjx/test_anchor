package com.arbfintech.microservice.customer.clientapi.client;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.arbfintech.framework.component.core.constant.ConditionTypeConst;
import com.arbfintech.framework.component.core.constant.JsonKeyConst;
import com.arbfintech.framework.component.core.type.SqlOption;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Objects;

/**
 * @author Baron
 *
 */
@Component
public class CustomerHttpClient {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${arb.gateway.url}")
    private String gatewayUrl;

    private static final String customerUrl = "/customer-restapi/customer";

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerHttpClient.class);

    public Long replaceCustomer(String dataStr) {
        LOGGER.info("Start to replace customer");
        Long result = restTemplate.postForObject(gatewayUrl + customerUrl + "/customers", dataStr, Long.class);
        if (Objects.requireNonNull(result) != -1) {
            LOGGER.info("Success ->>> replace customer, result: {}", result);
        } else {
            LOGGER.info("Failed ->>> replace customer");
        }
        return result;
    }

    public JSONObject getCustomerById(Long id) {
        LOGGER.info("Start to replace customer");
        JSONObject resultJson = restTemplate.getForObject(String.format(gatewayUrl + customerUrl + "/customers/%s", id), JSONObject.class);
        if (!CollectionUtils.isEmpty(resultJson)) {
            LOGGER.info("Success ->>> get customer, customer id: {}", resultJson.getLong(JsonKeyConst.ID));
        } else {
            resultJson = new JSONObject();
            LOGGER.info("Failed ->>> get customer, param id: {}", id);
        }
        return resultJson;
    }

    public JSONArray listCustomerBySsnOrEmailOrNo(String conditionStr) {
        JSONObject paramJson = JSON.parseObject(conditionStr);
        String ssn = paramJson.getString(JsonKeyConst.SSN);
        String email = paramJson.getString(JsonKeyConst.EMAIL);
        String accountNo = paramJson.getString(JsonKeyConst.ACCOUNT_NO);
        String routingNo = paramJson.getString(JsonKeyConst.ROUTING_NO);

        SqlOption sqlOption = SqlOption.getInstance();
        sqlOption.addWhereFormat(ConditionTypeConst.OR, "ssn = '%s'", ssn);
        sqlOption.addWhereFormat(ConditionTypeConst.OR, "email = '%s'", email);
        sqlOption.addWhereFormat(ConditionTypeConst.OR, "(bank_account_no = '%s' AND bank_routing_no = '%s')", accountNo, routingNo);
        sqlOption.addField(JsonKeyConst.ID);
        sqlOption.addOrder("create_time DESC");
        sqlOption.addPage("LIMIT 1");

        return this.listCustomerByOptions(sqlOption.toString());
    }

    public JSONArray listCustomerByOptions(String optionStr) {
        LOGGER.info("Start to replace customer");
        JSONArray resultArray = restTemplate.getForObject(gatewayUrl + customerUrl + "/customers", JSONArray.class, JSONObject.class, new HashMap<String, String>() {{
            put(JsonKeyConst.OPTIONS, optionStr);
        }});
        if (!CollectionUtils.isEmpty(resultArray)) {
            LOGGER.info("Success ->>> find customers, size: {}", resultArray.size());
        } else {
            resultArray = new JSONArray();
            LOGGER.info("Failed ->>> find customers");
        }
        return resultArray;
    }

    public JSONObject findCustomerByOptions(String optionStr) {
        LOGGER.info("Start to replace customer");
        JSONObject resultJson = restTemplate.getForObject(gatewayUrl + customerUrl + "/customer/single?{options}", JSONObject.class, new HashMap<String, String>() {{
            put(JsonKeyConst.OPTIONS, optionStr);
        }});

        if (CollectionUtils.isEmpty(resultJson)) {
            LOGGER.info("Success ->>> find customer, customer id: {}", resultJson.getLong(JsonKeyConst.ID));
        } else {
            resultJson = new JSONObject();
            LOGGER.info("Failed ->>> find customer");
        }
        return resultJson;
    }
}
