package com.arbfintech.microservice.customer.restapi.service;

import com.arbfintech.framework.component.core.type.AjaxResult;
import com.arbfintech.microservice.customer.object.dto.CalculationResultDTO;
import com.arbfintech.microservice.customer.object.entity.*;
import com.arbfintech.microservice.customer.object.enumerate.IBVRequestCodeStatusEnum;
import com.arbfintech.microservice.customer.object.util.CustomerFeildKey;
import com.arbfintech.microservice.customer.object.util.ExtentionJsonUtil;
import com.arbfintech.microservice.customer.restapi.repository.reader.CommonReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;

@Service
public class BusinessService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BusinessService.class);

    @Autowired
    private CommonReader commonReader;

    public String calculateLoanAmount (Long customerId) {

        CalculationResultDTO calculationResultDTO = new CalculationResultDTO();

        /**
         *  1、校验参数是否齐全（personal, employment, bank, ibv）
         */
        CustomerProfile customerProfile = commonReader.getEntityByCustomerId(CustomerProfile.class, customerId);
        Boolean personalResult = ExtentionJsonUtil.containsKey(
                customerProfile, CustomerFeildKey.getPersonalCalculateFactorList());

        CustomerEmploymentData customerEmployment = commonReader.getEntityByCustomerId(CustomerEmploymentData.class, customerId);
        Boolean employmentResult = ExtentionJsonUtil.containsKey(
                customerEmployment, CustomerFeildKey.getEmploymentCalculateFactorList());

        List<CustomerBankData> bankDataList = commonReader.listEntityByCustomerId(CustomerBankData.class, customerId);
        List<CustomerBankCardData> bankCardDataList = commonReader.listEntityByCustomerId(CustomerBankCardData.class, customerId);
        Boolean bankResult = CollectionUtils.isEmpty(bankDataList) && CollectionUtils.isEmpty(bankCardDataList);

        CustomerDecisionLogicAuthorizationRecord ibvData = commonReader.getEntityByCustomerId(
                CustomerDecisionLogicAuthorizationRecord.class, customerId);
        Boolean ibvResult = Objects.nonNull(ibvData) &&
                IBVRequestCodeStatusEnum.VERIFIED.getValue().equals(ibvData.getRequestCodeStatus());

        calculationResultDTO.setPersonalResult(personalResult);
        calculationResultDTO.setEmploymentResult(employmentResult);
        calculationResultDTO.setBankResult(bankResult);
        calculationResultDTO.setIbvResult(ibvResult);

        if (!(personalResult && employmentResult && bankResult && ibvResult)) {
            LOGGER.info("[Calculate Loan Amount]Fail to calculate loanAmount, missing parameter. " +
                    "CustomerId: {}, PersonalResult: {}, EmploymentResult: {}, BankResult: {}, IbvResult: {}",
                    customerId, personalResult, employmentResult, bankResult, ibvResult);
            return AjaxResult.success(calculationResultDTO);
        }

        // TODO get statement





        return AjaxResult.success(calculationResultDTO);
    }
}
