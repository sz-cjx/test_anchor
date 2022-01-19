package com.arbfintech.microservice.customer.restapi.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.arbfintech.framework.component.core.constant.CodeConst;
import com.arbfintech.framework.component.core.type.AjaxResult;
import com.arbfintech.framework.component.core.type.ProcedureException;
import com.arbfintech.framework.component.core.util.CryptUtil;
import com.arbfintech.framework.component.core.util.DateUtil;
import com.arbfintech.framework.component.core.util.EnumUtil;
import com.arbfintech.framework.component.core.util.RandomUtil;
import com.arbfintech.microservice.customer.object.constant.CustomerJsonKey;
import com.arbfintech.microservice.customer.object.dto.ActivateAccountDTO;
import com.arbfintech.microservice.customer.object.dto.CustomerAccountDTO;
import com.arbfintech.microservice.customer.object.dto.CustomerAccountPasswordDTO;
import com.arbfintech.microservice.customer.object.entity.CustomerAccountData;
import com.arbfintech.microservice.customer.object.entity.CustomerProfile;
import com.arbfintech.microservice.customer.object.enumerate.ChangePasswordTypeEnum;
import com.arbfintech.microservice.customer.object.enumerate.CustomerAccountStatusEnum;
import com.arbfintech.microservice.customer.object.enumerate.CustomerErrorCode;
import com.arbfintech.microservice.customer.object.enumerate.CustomerEventTypeEnum;
import com.arbfintech.microservice.customer.restapi.component.SystemLogComponent;
import com.arbfintech.microservice.customer.restapi.repository.CustomerReader;
import com.arbfintech.microservice.customer.restapi.repository.reader.CommonReader;
import com.arbfintech.microservice.customer.restapi.repository.writer.CommonWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class CustomerAccountService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerAccountService.class);

    @Autowired
    private CommonReader commonReader;

    @Autowired
    private CommonWriter commonWriter;

    @Autowired
    private CustomerReader customerReader;

    @Autowired
    private SystemLogComponent systemLogComponent;

    // TODO get accountId from token
    public String getAccountInfo(Long id) {
        CustomerAccountData customerAccountData = commonReader.getEntityByCustomerId(CustomerAccountData.class, id);
        CustomerAccountDTO customerAccountDTO = JSON.parseObject(JSON.toJSONString(customerAccountData), CustomerAccountDTO.class);
        customerAccountDTO.setCreatedAt(DateUtil.timeStampToStr(customerAccountData.getCreatedAt()));

        return AjaxResult.success(customerAccountDTO);
    }

    public String saveAccountInfo(CustomerAccountDTO customerAccountDTO) throws ProcedureException {
        Long customerId = customerAccountDTO.getId();
        CustomerAccountData customerAccountData = JSON.parseObject(JSON.toJSONString(customerAccountDTO), CustomerAccountData.class);
        Long resultCode = commonWriter.save(CustomerAccountData.class, JSON.toJSONString(customerAccountData));

        if (resultCode < CodeConst.SUCCESS) {
            LOGGER.warn("[Update Account]Failed to save customer account data. customerId:{}", customerId);
            throw new ProcedureException(CustomerErrorCode.FAILURE_FAILED_TO_UPDATE_DATA);
        }

        return AjaxResult.success();
    }

    public String changePassword(CustomerAccountPasswordDTO customerAccountPasswordDTO) throws ProcedureException {
        ChangePasswordTypeEnum typeEnum = Optional.ofNullable(EnumUtil.getByValue(ChangePasswordTypeEnum.class, customerAccountPasswordDTO.getChangeType()))
                .orElseThrow(() -> new ProcedureException(CustomerErrorCode.FAILURE_CHANGE_TYPE_NOT_EXIST));
        Long customerId = customerAccountPasswordDTO.getId();
        CustomerAccountData customerAccountData = commonReader.getEntityByCustomerId(CustomerAccountData.class, customerId);

        switch (typeEnum) {
            case LOGIN: {
                String currentLoginPassword = customerAccountPasswordDTO.getCurrentLoginPassword();
                if (!Objects.equals(generalPassword(currentLoginPassword, customerAccountData.getSalt()), customerAccountData.getLoginPassword())) {
                    LOGGER.warn("[Update Account]Current login password is incorrect. customerId:{}", customerId);
                    throw new ProcedureException(CustomerErrorCode.FAILURE_LOGIN_PASSWORD_INCORRECT);
                }

                String newLoginPassword = customerAccountPasswordDTO.getLoginPassword();

                customerAccountData.setLoginPassword(generalPassword(newLoginPassword, customerAccountData.getSalt()));
                Long resultCode = commonWriter.save(customerAccountData);

                if (resultCode < CodeConst.SUCCESS) {
                    LOGGER.warn("[Update Account]Failed to change login password. customerId:{}", customerId);
                    throw new ProcedureException(CustomerErrorCode.FAILURE_FAILED_TO_UPDATE_DATA);
                }

                return AjaxResult.success();
            }
            case PAYMENT: {
                String currentPaymentPassword = customerAccountPasswordDTO.getCurrentPaymentPassword();
                if (!Objects.equals(generalPassword(currentPaymentPassword, customerAccountData.getSalt()), customerAccountData.getPaymentPassword())) {
                    LOGGER.warn("[Update Account]Current payment password is incorrect. customerId:{}", customerId);
                    throw new ProcedureException(CustomerErrorCode.FAILURE_LOGIN_PASSWORD_INCORRECT);
                }

                String newPaymentPassword = customerAccountPasswordDTO.getPaymentPassword();
                customerAccountData.setPaymentPassword(generalPassword(newPaymentPassword, customerAccountData.getSalt()));
                Long resultCode = commonWriter.save(customerAccountData);

                if (resultCode < CodeConst.SUCCESS) {
                    LOGGER.warn("[Update Account]Failed to change payment password. customerId:{}", customerId);
                    throw new ProcedureException(CustomerErrorCode.FAILURE_FAILED_TO_UPDATE_DATA);
                }

                return AjaxResult.success();
            }
            default:
                throw new ProcedureException(CustomerErrorCode.FAILURE_CHANGE_TYPE_NOT_EXIST);
        }
    }

    public String activateAccount(ActivateAccountDTO activateAccountDTO) throws ProcedureException {
        String email = activateAccountDTO.getEmail().toLowerCase();
        LOGGER.info("[Activate Account]Start activate. Email:{}", email);
        CustomerProfile customerProfile = customerReader.getCustomerInfoByEmail(email);
        if (Objects.isNull(customerProfile)) {
            LOGGER.warn("[Activate Account]Failed to find customer profile. Email:{}", email);
            throw new ProcedureException(CustomerErrorCode.FAILURE_ACTIVATE_ACCOUNT_EMAIL_NOT_EXIST);
        }

        CustomerAccountData accountData = commonReader.getEntityByCustomerId(CustomerAccountData.class, customerProfile.getId());
        Integer accountStatus = accountData.getStatus();
        if (CustomerAccountStatusEnum.ACTIVE.getValue().equals(accountStatus)) {
            LOGGER.warn("[Activate Account]Account has been activated. CustomerId: {},Email:{}", customerProfile.getId(), email);
            throw new ProcedureException(CustomerErrorCode.FAILURE_ACTIVATE_ACCOUNT_HAS_BEEN_ACTIVATED);
        }

        String salt = RandomUtil.getAlphaNumeric();
        String passwordInMd5 = generalPassword(activateAccountDTO.getPassword(), salt);

        accountData.setSalt(salt);
        accountData.setLoginPassword(passwordInMd5);
        accountData.setStatus(CustomerAccountStatusEnum.ACTIVE.getValue());

        Long resultCode = commonWriter.save(accountData);

        if (resultCode < CodeConst.SUCCESS) {
            LOGGER.warn("[Activate Account]Failed to save account info. customerId:{}", customerProfile.getId());
            throw new ProcedureException(CustomerErrorCode.FAILURE_FAILED_TO_UPDATE_DATA);
        }

        // TODO 登录
        Long currentTimestamp = DateUtil.getCurrentTimestamp();
        JSONObject logData = new JSONObject();
        logData.put(CustomerJsonKey.COUNTRY, "America");
        logData.put(CustomerJsonKey.CITY, "New York");
        logData.put(CustomerJsonKey.PHONE_MODEL, "iOS 15.1");
        logData.put(CustomerJsonKey.IP, "192.168.0.1");
        logData.put(CustomerJsonKey.SIGN_IN_TIME,
                DateUtil.timeStampToStr(currentTimestamp, DateUtil.DEFAULT_DATETIME_PATTERN));

        systemLogComponent.addSystemLog(
                customerProfile.getId(), logData, CustomerEventTypeEnum.CUSTOMER_SIGN_IN.getValue(),
                null, null, currentTimestamp
        );


        return AjaxResult.success();
    }

    public String signIn(ActivateAccountDTO activateAccountDTO) throws ProcedureException {
        String email = activateAccountDTO.getEmail().toLowerCase();
        LOGGER.info("[Sign In]Start activate. Email:{}", email);
        CustomerProfile customerProfile = customerReader.getCustomerInfoByEmail(email);
        if (Objects.isNull(customerProfile)) {
            LOGGER.warn("[Sign In]Failed to find customer account. Email:{}", email);
            throw new ProcedureException(CustomerErrorCode.FAILURE_SIGN_IN_ACCOUNT_NOT_EXIST);
        }

        CustomerAccountData accountData = commonReader.getEntityByCustomerId(CustomerAccountData.class, customerProfile.getId());
        Long customerId = accountData.getId();
        if (!CustomerAccountStatusEnum.ACTIVE.getValue().equals(accountData.getStatus())) {
            LOGGER.warn("[Sign In]Account has been disabled. CustomerId: {}, Email:{}", customerId, email);
            throw new ProcedureException(CustomerErrorCode.FAILURE_SIGN_IN_ACCOUNT_HAS_BEEN_DISABLED);
        }

        String salt = accountData.getSalt();
        String passwordInMd5 = generalPassword(activateAccountDTO.getPassword(), salt);
        if (!Objects.equals(accountData.getLoginPassword(), passwordInMd5)) {
            LOGGER.warn("[Sign In]Incorrect password.. CustomerId: {}, Email:{}", customerId, email);
            throw new ProcedureException(CustomerErrorCode.FAILURE_SIGN_IN_INCORRECT_PASSWORD);
        }

        // TODO 登录
        Long currentTimestamp = DateUtil.getCurrentTimestamp();
        JSONObject logData = new JSONObject();
        logData.put(CustomerJsonKey.COUNTRY, "America");
        logData.put(CustomerJsonKey.CITY, "New York");
        logData.put(CustomerJsonKey.PHONE_MODEL, "iOS 15.1");
        logData.put(CustomerJsonKey.IP, "192.168.0.1");
        logData.put(CustomerJsonKey.SIGN_IN_TIME,
                DateUtil.timeStampToStr(currentTimestamp, DateUtil.DEFAULT_DATETIME_PATTERN));

        systemLogComponent.addSystemLog(
                customerProfile.getId(), logData, CustomerEventTypeEnum.CUSTOMER_SIGN_IN.getValue(),
                null, null, currentTimestamp
        );

        return AjaxResult.success();
    }

    private String generalPassword(String password, String salt) {
        return CryptUtil.md5(password.toLowerCase() + salt);
    }

}
