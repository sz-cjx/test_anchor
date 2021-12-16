package com.arbfintech.microservice.customer.restapi.service;

import com.alibaba.fastjson.JSON;
import com.arbfintech.framework.component.core.constant.CodeConst;
import com.arbfintech.framework.component.core.type.AjaxResult;
import com.arbfintech.framework.component.core.type.ProcedureException;
import com.arbfintech.framework.component.core.util.DateUtil;
import com.arbfintech.microservice.customer.object.dto.CustomerAccountDTO;
import com.arbfintech.microservice.customer.object.entity.CustomerAccountData;
import com.arbfintech.microservice.customer.object.enumerate.CustomerErrorCode;
import com.arbfintech.microservice.customer.restapi.repository.reader.CommonReader;
import com.arbfintech.microservice.customer.restapi.repository.writer.CommonWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerAccountService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerAccountService.class);

    @Autowired
    private CommonReader commonReader;

    @Autowired
    private CommonWriter commonWriter;

    // TODO get accountId from token
    public String getAccountInfo(Long accountId) {
        CustomerAccountData customerAccountData = commonReader.getEntityByCustomerId(CustomerAccountData.class, accountId);
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

}
