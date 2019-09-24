package com.arbfintech.microservice.customer.restapi.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.arbfintech.framework.component.core.constant.CodeConst;
import com.arbfintech.framework.component.core.type.JpaService;
import com.arbfintech.microservice.customer.domain.entity.Customer;
import com.arbfintech.microservice.customer.domain.repository.CustomerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author CAVALIERS
 * 2019/9/23 12:36
 */

@Service
public class CustomerRestService extends JpaService<Customer> {
    private Logger logger = LoggerFactory.getLogger(CustomerRestService.class);

    @Autowired
    private CustomerRepository customerRepository;

    public Long addCustomer(String dataStr) {
        Long restCode;
        logger.info("Start to add the customer:{}", dataStr);
        try {
            Customer customer = JSONObject.parseObject(dataStr, Customer.class);
            Customer customerInDb = customerRepository.save(customer);
            restCode = customerInDb.getId();
            logger.info("Add customer success, Id:{}", restCode);
        } catch (Exception exception) {
            logger.error(exception.toString());
            restCode = Long.parseLong(String.valueOf(CodeConst.FAILURE));
        }
        return restCode;
    }

    public String getCustomerById(Long id) {
        String result = "";
        logger.info("Start to query the customer by Id:{}", id);
        if (id != null) {
            Customer customerInDb  = customerRepository.findOne(id);
            if (customerInDb != null) {
                result = JSONObject.toJSONString(customerInDb);
                logger.info("Found the customer:" + result);
            } else {
                logger.error("No find the customer ");
            }
        }
        return result;
    }


    public Integer setCustomerById(Long id, String dataStr) {
        Integer code = CodeConst.DEFAULT;
        logger.info("Start to update customer data By id : " + id + "  dataStr : " + dataStr);
        try {
            if (customerRepository.exists(id)) {
                Customer customerInDb  = customerRepository.findOne(id);
                JSONObject oldCustomerJson = JSONObject.parseObject(customerInDb.toString());
                Map<String, Object> customerJsonMap = JSONObject.parseObject(dataStr);
                for (Map.Entry<String, Object> entry : customerJsonMap.entrySet()) {
                    oldCustomerJson.put(entry.getKey(), entry.getValue());
                }
                Customer customer = JSON.parseObject(oldCustomerJson.toJSONString(), Customer.class);
                customerRepository.save(customer);
                code = CodeConst.SUCCESS;
            } else {
                logger.error(" No find the customer");
            }
        } catch (Exception exception) {
            logger.error(exception.toString());
            code = CodeConst.FAILURE;
        }
        return code;
    }

    public String listCustomerByConditions(String conditionStr,String conditionType){
        List<Customer> customerList = new ArrayList<>();
        Specification<Customer> specification = createConditions(conditionStr, conditionType);
        if (specification != null) {
            customerList = customerRepository.findAll(specification);
        }

        return JSON.toJSONString(customerList);
    }
}
