package com.arbfintech.microservice.customer.restapi.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.arbfintech.framework.component.core.constant.CodeConst;
import com.arbfintech.framework.component.core.constant.CustomerStatusConst;
import com.arbfintech.framework.component.core.constant.JsonKeyConst;
import com.arbfintech.framework.component.core.enumerate.CodeEnum;
import com.arbfintech.framework.component.core.type.AjaxResult;
import com.arbfintech.framework.component.core.type.JpaService;
import com.arbfintech.framework.component.core.util.CryptUtil;
import com.arbfintech.framework.component.core.util.RandomUtil;
import com.arbfintech.microservice.customer.domain.entity.Customer;
import com.arbfintech.microservice.customer.domain.repository.CustomerRepository;
import com.arbfintech.microservice.customer.domain.repository.PandaV2Repository;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import sun.security.util.Password;

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
    private PandaV2Repository pandaV2Repository;

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

    public String listCustomerBySSN(Long ssn) {
        JSONArray customerJsonStr = pandaV2Repository.listCustomerBySSN(ssn);
        return customerJsonStr.toJSONString();
    }

    public Long getTheLatestCustomerIdBySSN(Long ssn) {
        return pandaV2Repository.getTheLatestCustomerIdBySSN(ssn);
    }

    public String verifyCustomerLoginData(String loginData) {
        JSONObject loginDataObj = JSON.parseObject(loginData);
        String email = loginDataObj.getString(JsonKeyConst.EMAIL);

        Customer customerInDB = customerRepository.findByEmail(email);
        if(customerInDB == null) {
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
        if(!passwordInDB.equals(encryptLoginPassword(password, salt))) {
            logger.info("Failure: The login password is not correct. Email:{}, Password:{}", email, password);
            return AjaxResult.failure(CodeEnum.ERROR_EMAIL_OR_PASSWORD);
        }

        JSONObject resultDataObj = new JSONObject();
        resultDataObj.put(JsonKeyConst.ID, customerInDB.getId());
        return AjaxResult.success(resultDataObj);
    }

    private String encryptLoginPassword (String password, String salt) {
        return CryptUtil.md5(CryptUtil.md5(password) + salt);
    }

    public String doCustomerSignUp(String signUpData) {

        JSONObject customerSignUpDataMap = JSON.parseObject(signUpData);
        String ssn = customerSignUpDataMap.getString(JsonKeyConst.SSN);
        String email = customerSignUpDataMap.getString(JsonKeyConst.EMAIL);
        Customer customerInDB = customerRepository.findBySsnAndEmail(ssn, email);

        //Make the judgment temporarily.
        if(customerInDB == null) {
            logger.warn("Failure: Don't query customer. SSN:{}, Email:{}", ssn, email);
            return AjaxResult.failure(CodeConst.FAILURE, "Don't query customer and need to create.");
        }
        if(customerInDB.getIsSignUp() != null && customerInDB.getIsSignUp() == CustomerStatusConst.ALREADY_SIGN_UP) {
            logger.warn("Success: The customer was already sign-up. SSN:{}, Email:{}", ssn, email);
            customerInDB.setSalt(null);
            customerInDB.setPassword(null);
            return AjaxResult.result(CodeEnum.CUSTOMER_ALREADY_SIGN_UP.getValue(), CodeEnum.CUSTOMER_ALREADY_SIGN_UP.getText(), customerInDB);
        }
        //This key is not field in Customer, so it must be removed if you do a data update operation like the following code
        customerSignUpDataMap.remove(JsonKeyConst.CONFIRM_PASSWORD);

        JSONObject customerInDbObj = (JSONObject)JSONObject.toJSON(customerInDB);
        for (Map.Entry<String, Object> customerDataEntry : customerSignUpDataMap.entrySet()) {
            customerInDbObj.put(customerDataEntry.getKey(), customerDataEntry.getValue());
        }
        String salt = RandomUtil.getAlphabet();
        String encryptPassword = encryptLoginPassword(customerSignUpDataMap.getString(JsonKeyConst.PASSWORD), salt);
        customerInDbObj.put(JsonKeyConst.PASSWORD, encryptPassword);
        customerInDbObj.put(JsonKeyConst.SALT, salt);

        customerInDbObj.put(JsonKeyConst.IS_SIGN_UP, CustomerStatusConst.ALREADY_SIGN_UP);
        Customer completeCustomerData = JSON.parseObject(customerInDbObj.toJSONString(), Customer.class);
        customerInDB = customerRepository.save(completeCustomerData);
        customerInDB.setSalt(null);
        customerInDB.setPassword(null);
        logger.info("Success to create customer(complete customer info). Email:{},SSN:{}", email, ssn);
        return AjaxResult.success(customerInDB);
    }


//    public Long  saveCustomerByJDBC(String customerStr) {
//        JSONObject customerInfo = JSONObject.parseObject(customerStr);
//        return  pandaV2Repository.saveCustomerByJDBC(customerInfo);
//    }
}
