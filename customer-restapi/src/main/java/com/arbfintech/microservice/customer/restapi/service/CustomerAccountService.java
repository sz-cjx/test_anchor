package com.arbfintech.microservice.customer.restapi.service;

import com.alibaba.fastjson.JSON;
import com.arbfintech.framework.component.core.constant.CodeConst;
import com.arbfintech.framework.component.core.type.AjaxResult;
import com.arbfintech.framework.component.core.type.ProcedureException;
import com.arbfintech.framework.component.core.util.DateUtil;
import com.arbfintech.framework.component.core.util.EnumUtil;
import com.arbfintech.microservice.customer.object.dto.ActivateAccountDTO;
import com.arbfintech.microservice.customer.object.dto.CustomerAccountDTO;
import com.arbfintech.microservice.customer.object.dto.CustomerAccountPasswordDTO;
import com.arbfintech.microservice.customer.object.entity.CustomerAccountData;
import com.arbfintech.microservice.customer.object.entity.CustomerProfile;
import com.arbfintech.microservice.customer.object.enumerate.ChangePasswordTypeEnum;
import com.arbfintech.microservice.customer.object.enumerate.CustomerAccountStatusEnum;
import com.arbfintech.microservice.customer.object.enumerate.CustomerErrorCode;
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
                if (!Objects.equals(currentLoginPassword, customerAccountData.getLoginPassword())) {
                    throw new ProcedureException(CustomerErrorCode.FAILURE_LOGIN_PASSWORD_INCORRECT);
                }

                String newLoginPassword = customerAccountPasswordDTO.getLoginPassword();
                CustomerAccountData saveData = JSON.parseObject(JSON.toJSONString(customerAccountPasswordDTO), CustomerAccountData.class);
                saveData.setLoginPassword(newLoginPassword);

                Long resultCode = commonWriter.save(CustomerAccountData.class, JSON.toJSONString(saveData));

                if (resultCode < CodeConst.SUCCESS) {
                    LOGGER.warn("[Update Account]Failed to change login password. customerId:{}", customerId);
                    throw new ProcedureException(CustomerErrorCode.FAILURE_FAILED_TO_UPDATE_DATA);
                }

                return AjaxResult.success();
            }
            case PAYMENT: {
                String currentPaymentPassword = customerAccountPasswordDTO.getCurrentPaymentPassword();
                if (!Objects.equals(currentPaymentPassword, customerAccountData.getPaymentPassword())) {
                    throw new ProcedureException(CustomerErrorCode.FAILURE_LOGIN_PASSWORD_INCORRECT);
                }

                String newPaymentPassword = customerAccountPasswordDTO.getPaymentPassword();
                CustomerAccountData saveData = JSON.parseObject(JSON.toJSONString(customerAccountPasswordDTO), CustomerAccountData.class);
                saveData.setPaymentPassword(newPaymentPassword);

                Long resultCode = commonWriter.save(CustomerAccountData.class, JSON.toJSONString(saveData));

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

        // TODO 加密、登录
        accountData.setPaymentPassword(activateAccountDTO.getPassword());
        accountData.setStatus(CustomerAccountStatusEnum.INACTIVE.getValue());

        Long resultCode = commonWriter.save(accountData);

        if (resultCode < CodeConst.SUCCESS) {
            LOGGER.warn("[Activate Account]Failed to save account info. customerId:{}", customerProfile.getId());
            throw new ProcedureException(CustomerErrorCode.FAILURE_FAILED_TO_UPDATE_DATA);
        }

        return AjaxResult.success();
    }

}
