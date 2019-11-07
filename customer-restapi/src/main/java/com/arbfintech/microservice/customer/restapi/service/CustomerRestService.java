package com.arbfintech.microservice.customer.restapi.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.arbfintech.framework.component.core.constant.*;
import com.arbfintech.framework.component.core.enumerate.CodeEnum;
import com.arbfintech.framework.component.core.type.AjaxResult;
import com.arbfintech.framework.component.core.type.JpaService;
import com.arbfintech.framework.component.core.util.CryptUtil;
import com.arbfintech.framework.component.core.util.RandomUtil;
import com.arbfintech.microservice.customer.domain.entity.Customer;
import com.arbfintech.microservice.customer.domain.repository.CustomerRepository;
import com.arbfintech.microservice.customer.domain.repositoryReader.CustomerReaderRepository;
import com.arbfintech.microservice.customer.domain.repositoryReader.PandaReaderRepository;
import org.apache.commons.lang3.StringUtils;
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

    @Autowired
    private CustomerReaderRepository customerReaderRepository;

    @Autowired
    private PandaReaderRepository pandaReaderRepository;

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
            Customer customerInDb = customerReaderRepository.findOne(id);
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
                Customer customerInDb = customerReaderRepository.findOne(id);
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

    public String listCustomerByConditions(String conditionStr, String conditionType) {
        List<Customer> customerList = new ArrayList<>();
        Specification<Customer> specification = createConditions(conditionStr, conditionType);
        if (specification != null) {
            customerList = customerReaderRepository.findAll(specification);
        }

        return JSON.toJSONString(customerList);
    }

    public String listCustomerBySSN(String ssn) {
        JSONArray customerJsonStr = pandaReaderRepository.listCustomerBySSN(ssn);
        return customerJsonStr.toJSONString();
    }

    public Long getTheLatestCustomerIdBySSN(String ssn) {
        return pandaReaderRepository.getTheLatestCustomerIdBySSN(ssn);
    }

    public Long getLatestCustomerIdByEmailOrSsn(String email, String ssn) {
        return pandaReaderRepository.getLatestCustomerIdByEmailOrSsn(email, ssn);
    }

    public String verifyCustomerLoginData(String loginData) {
        JSONObject loginDataObj = JSON.parseObject(loginData);
        String email = loginDataObj.getString(JsonKeyConst.EMAIL);
        Long customerId = loginDataObj.getLong(JsonKeyConst.CUSTOMER_ID);

        Customer customerInDB;
        if(customerId != null) {
            customerInDB = customerReaderRepository.findByEmailAndId(email, customerId);
        } else {
            logger.info("Customer From LOS: Customer sign in. Email:{}", email);
            customerInDB = customerReaderRepository.findByEmail(email);
        }

        if (customerInDB != null && !Integer.valueOf(CustomerStatusConst.ALREADY_SIGN_UP).equals(customerInDB.getIsSignUp())) {
            logger.warn("Failure: Customer don't sign up");
            customerInDB.setSalt(null);
            customerInDB.setPassword(null);
            return AjaxResult.failure(CodeConst.FAILURE, GlobalConst.STR_EMPTY, customerInDB);
        }
        /**
         * Status:
         *      -1 : reject, 0 : not review, 1 : pass
         */
        if (customerInDB != null && !Integer.valueOf(CodeConst.SUCCESS).equals(customerInDB.getStatus())) {
            logger.warn("Failure: Customer is rejected or not reviewed. Status:{}", customerInDB.getStatus());
            customerInDB.setSalt(null);
            customerInDB.setPassword(null);
            return AjaxResult.failure(CodeConst.FAILURE, GlobalConst.STR_EMPTY, customerInDB);
        }
        if (customerInDB == null) {
            logger.warn("Failure: Don't query customer by email. Email:{}", email);
            return AjaxResult.failure(CodeEnum.ERROR_EMAIL_OR_PASSWORD);
        }

        String password = loginDataObj.getString(JsonKeyConst.PASSWORD);
        String salt = customerInDB.getSalt();
        String passwordInDB = customerInDB.getPassword();
        /**
         * Note: Password encrypted using this method will be all uppercase if it contains letters.
         * In Panda system, login password is encrypted with javaScript firstly, so the first encrypted
         * password is will be lowercase. When the second encryption is done with Java, it will be all
         * uppercase.
         */
        if (StringUtils.isNotEmpty(password) && !passwordInDB.equals(encryptLoginPassword(password, salt))) {
            logger.info("Failure: The login password is not correct. Email:{}, Password:{}", email, password);
            return AjaxResult.failure(CodeEnum.ERROR_EMAIL_OR_PASSWORD);
        }

        JSONObject resultDataObj = new JSONObject();
        resultDataObj.put(JsonKeyConst.ID, customerInDB.getId());

        return AjaxResult.success(resultDataObj);
    }

    private String encryptLoginPassword(String password, String salt) {
        return CryptUtil.md5(CryptUtil.md5(password) + salt);
    }

    public String doCustomerSignUp(String signUpData) {
        CodeEnum codeEnum = CodeEnum.DEFAULT;
        JSONObject customerSignUpDataJSON = JSON.parseObject(signUpData);
        String email = customerSignUpDataJSON.getString(JsonKeyConst.EMAIL);
        Long customerId = customerSignUpDataJSON.getLong(JsonKeyConst.CUSTOMER_ID);
        Customer customerInDB;
        if (customerId != null) {
            logger.info("Customer From LOS: Customer sign up. CustomerId:{}, email:{}", customerId, email);
            customerInDB = customerReaderRepository.findByEmailAndId(email, customerId);
        } else {
            customerInDB = customerReaderRepository.findByEmail(email);
        }

        if (customerInDB == null) {
            //This key is not field in Customer, so it must be removed if you do a data update operation like the following code.
            customerSignUpDataJSON.remove(JsonKeyConst.CONFIRM_PASSWORD);
            String salt = RandomUtil.getAlphabet();
            String encryptPassword = encryptLoginPassword(customerSignUpDataJSON.getString(JsonKeyConst.PASSWORD), salt);
            customerSignUpDataJSON.put(JsonKeyConst.PASSWORD, encryptPassword);
            customerSignUpDataJSON.put(JsonKeyConst.SALT, salt);
            Customer customer = JSON.parseObject(customerSignUpDataJSON.toJSONString(), Customer.class);
            customer.setIsSignUp(CustomerStatusConst.ALREADY_SIGN_UP);
            customerInDB = customerRepository.save(customer);
            logger.info("Success to create a new customer. Customer email:{}", email);
            codeEnum = CodeEnum.SUCCESS;
        } else {
            if (customerInDB.getIsSignUp() == null || customerInDB.getIsSignUp() == CustomerStatusConst.NOT_SIGN_UP) {
                logger.info("Customer already exist and will init password for customer. Email:{}", email);
                String salt = RandomUtil.getAlphabet();
                String encryptPassword = encryptLoginPassword(customerSignUpDataJSON.getString(JsonKeyConst.PASSWORD), salt);
                customerInDB.setPassword(encryptPassword);
                customerInDB.setSalt(salt);
                customerInDB.setIsSignUp(CustomerStatusConst.ALREADY_SIGN_UP);
                customerInDB.setStatus(StatusConst.ENABLED);
                customerInDB = customerRepository.save(customerInDB);
                logger.info("Success to init customer for exist customer. Email:{}", email);
                codeEnum = CodeEnum.SUCCESS;
            } else {
                if (customerInDB.getIsSignUp() != null && customerInDB.getIsSignUp() == CustomerStatusConst.ALREADY_SIGN_UP) {
                    codeEnum = CodeEnum.CUSTOMER_ALREADY_SIGN_UP;
                    logger.warn("Customer already sign up. Email:{}", email);
                }
            }
        }
        customerInDB.setSalt(null);
        customerInDB.setPassword(null);
        return AjaxResult.result(codeEnum.getValue(), codeEnum.getText(), customerInDB);
    }

//    public Long  saveCustomerByJDBC(String customerStr) {
//        JSONObject customerInfo = JSONObject.parseObject(customerStr);
//        return  pandaV2Repository.saveCustomerByJDBC(customerInfo);
//    }
}
