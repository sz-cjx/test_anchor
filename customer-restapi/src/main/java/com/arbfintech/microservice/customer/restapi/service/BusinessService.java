package com.arbfintech.microservice.customer.restapi.service;

import com.alibaba.fastjson.JSON;
import com.arbfintech.framework.component.algorithm.service.AlgorithmService;
import com.arbfintech.framework.component.algorithm.type.DirectDeposit;
import com.arbfintech.framework.component.algorithm.type.LoanAmountParameter;
import com.arbfintech.framework.component.algorithm.type.LoanAmountRequest;
import com.arbfintech.framework.component.algorithm.type.LoanAmountResponse;
import com.arbfintech.framework.component.core.constant.CodeConst;
import com.arbfintech.framework.component.core.enumerate.LoanTypeEnum;
import com.arbfintech.framework.component.core.enumerate.ModeEnum;
import com.arbfintech.framework.component.core.enumerate.StateEnum;
import com.arbfintech.framework.component.core.type.AjaxResult;
import com.arbfintech.framework.component.core.type.ProcedureException;
import com.arbfintech.framework.component.core.util.DateUtil;
import com.arbfintech.framework.component.core.util.EnumUtil;
import com.arbfintech.microservice.customer.object.constant.CustomerCacheKey;
import com.arbfintech.microservice.customer.object.dto.CalculationProcessDTO;
import com.arbfintech.microservice.customer.object.dto.CalculationResultDTO;
import com.arbfintech.microservice.customer.object.entity.*;
import com.arbfintech.microservice.customer.object.enumerate.CustomerErrorCode;
import com.arbfintech.microservice.customer.object.enumerate.IBVRequestCodeStatusEnum;
import com.arbfintech.microservice.customer.object.enumerate.VOBTypeEnum;
import com.arbfintech.microservice.customer.object.util.CustomerFieldKey;
import com.arbfintech.microservice.customer.object.util.ExtentionJsonUtil;
import com.arbfintech.microservice.customer.restapi.repository.cache.AlgorithmRedisRepository;
import com.arbfintech.microservice.customer.restapi.repository.reader.CommonReader;
import com.arbfintech.microservice.customer.restapi.repository.writer.CommonWriter;
import com.arbfintech.microservice.loan.object.enumerate.LoanCategoryEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class BusinessService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BusinessService.class);

    private static final int FLAGS_STRICT = 1 << 9;

    @Autowired
    private CommonReader commonReader;

    @Autowired
    private CommonWriter commonWriter;

    @Autowired
    private AlgorithmRedisRepository algorithmRedisRepository;

    @Autowired
    private AlgorithmService algorithmService;

    public CalculationProcessDTO checkCalculationParam(Long customerId) throws ProcedureException {
        CalculationResultDTO calculationResultDTO = new CalculationResultDTO();
        LOGGER.info("[Calculate Loan Amount]Start calculate loan amount. CustomerId: {}", customerId);

        /**
         *  1、校验参数是否齐全（personal, employment, bank, ibv）
         */
        CustomerProfile customerProfile = commonReader.getEntityByCustomerId(CustomerProfile.class, customerId);
        Boolean personalResult = ExtentionJsonUtil.containsKey(
                customerProfile, CustomerFieldKey.getPersonalCalculateFactorList());

        CustomerEmploymentData customerEmployment = commonReader.getEntityByCustomerId(CustomerEmploymentData.class, customerId);
        Boolean employmentResult = ExtentionJsonUtil.containsKey(
                customerEmployment, CustomerFieldKey.getEmploymentCalculateFactorList());

        List<CustomerBankData> bankDataList = commonReader.listEntityByCustomerId(CustomerBankData.class, customerId);
        List<CustomerBankCardData> bankCardDataList = commonReader.listEntityByCustomerId(CustomerBankCardData.class, customerId);
        Boolean bankResult = !(CollectionUtils.isEmpty(bankDataList) || CollectionUtils.isEmpty(bankCardDataList));

        CustomerDecisionLogicAuthorizationRecord ibvData = commonReader.getEntityByCustomerId(
                CustomerDecisionLogicAuthorizationRecord.class, customerId);
        Boolean ibvResult = Objects.nonNull(ibvData) &&
                IBVRequestCodeStatusEnum.VERIFIED.getValue().equals(ibvData.getRequestCodeStatus());

        calculationResultDTO.setPersonalResult(personalResult);
        calculationResultDTO.setEmploymentResult(employmentResult);
        calculationResultDTO.setBankResult(bankResult);
        calculationResultDTO.setIbvResult(ibvResult);

        List<CustomerStatementData> statementDataList = commonReader.listEntityByCustomerId(CustomerStatementData.class, customerId);

        CalculationProcessDTO calculationProcessDTO = new CalculationProcessDTO();
        calculationProcessDTO.setCalculationResultDTO(calculationResultDTO);
        calculationProcessDTO.setCustomerProfile(customerProfile);
        calculationProcessDTO.setCustomerEmploymentData(customerEmployment);
        calculationProcessDTO.setStatementList(statementDataList);
        return calculationProcessDTO;
    }

    public String calculateLoanAmount (Long customerId, CalculationProcessDTO calculationProcessDTO) {
        CalculationResultDTO calculationResultDTO = calculationProcessDTO.getCalculationResultDTO();
        Boolean personalResult = calculationResultDTO.getPersonalResult();
        Boolean employmentResult = calculationResultDTO.getEmploymentResult();
        Boolean bankResult = calculationResultDTO.getBankResult();
        Boolean ibvResult = calculationResultDTO.getIbvResult();

        LOGGER.info("[Calculate Loan Amount]Check calculate parameter" +
                        "CustomerId: {}, PersonalResult: {}, EmploymentResult: {}, BankResult: {}, IbvResult: {}",
                customerId, personalResult, employmentResult, bankResult, ibvResult);
        if (!(personalResult && employmentResult && bankResult && ibvResult)) {
            return AjaxResult.success(calculationResultDTO);
        }

        CustomerProfile customerProfile = calculationProcessDTO.getCustomerProfile();
        CustomerEmploymentData customerEmployment = calculationProcessDTO.getCustomerEmploymentData();
        List<CustomerStatementData> statementDataList  = calculationProcessDTO.getStatementList();

        try {
            /*
             * Step 1: 从redis中获取用于计算loan amount的参数列表
             * Step 2: 组装用于计算loan amount的参数 - LoanAmountRequest对象
             * Step 3: 计算loan amount
             */
            String loanAmountParameterStr = algorithmRedisRepository.fetchString(CustomerCacheKey.LOAN_AMOUNT_PARAMETER, 8L);
            List<LoanAmountParameter> loanAmountParameterList = JSON.parseArray(loanAmountParameterStr, LoanAmountParameter.class);

            // profile中的state转换: int -> String
            String state = EnumUtil.getTextByValue(StateEnum.class, customerProfile.getState());

            LOGGER.info("[Calculate Loan Amount]Start assemble calculate parameter. CustomerId: {}", customerId);
            LoanAmountRequest amountRequest = new LoanAmountRequest();
            amountRequest.setLastPayday(customerEmployment.getLastPayday());
            amountRequest.setState(state);
            amountRequest.setVoe(customerEmployment.getVoe());
            amountRequest.setCategory(LoanCategoryEnum.NEW.getValue());
            amountRequest.setPayrollFrequency(customerEmployment.getPayrollFrequency());

            // TODO need update
            amountRequest.setRoutingNo("123456789");
            amountRequest.setNumberOfOpenLoans(0);
            amountRequest.setLoanType(LoanTypeEnum.MULTIPLE_ADVANCE_LOAN.getValue());
            amountRequest.setAccountBalance(new BigDecimal(0));
            amountRequest.setAmountOfOpenLoans(new BigDecimal(0));
            amountRequest.setFlags(FLAGS_STRICT);
            amountRequest.setMode(ModeEnum.AUDIT.getValue());
            amountRequest.setTransferredAmount(new BigDecimal(0));

            if (CollectionUtils.isEmpty(statementDataList)) {
                LOGGER.info("[Calculate Loan Amount]Fail to calculate loan amount, Missing statement. CustomerId: {}", customerId);
                throw new ProcedureException(CustomerErrorCode.CALCULATOR_STOP_TO_MISSING_PARAMETER);
            }
            List<DirectDeposit> depositList = new ArrayList<>();
            for (CustomerStatementData statement : statementDataList) {
                DirectDeposit deposit = new DirectDeposit();
                deposit.setAmount(statement.getAmount());
                deposit.setBalance(statement.getBalance());
                deposit.setPayrollDate(statement.getPayrollDate());
                deposit.setPayrollType(statement.getPayrollType());
                deposit.setVob(VOBTypeEnum.DECISION_LOGIC.getValue());
                depositList.add(deposit);
            }
            amountRequest.setStatement(depositList);

            // TODO get program from redis
            amountRequest.setInterestRate(new BigDecimal("7.8"));
            amountRequest.setMinimumLoanAmount(new BigDecimal("200"));
            amountRequest.setMaximumLoanAmount(new BigDecimal("1500"));

            LoanAmountResponse loanAmountResponse = null;
            try {
                loanAmountResponse = algorithmService.calcLoanAmount(
                        amountRequest, loanAmountParameterList
                );
            } catch (ProcedureException e) {
                LOGGER.warn("[Calculate Loan Amount]Fail to calculate loan amount. CustomerId: {}, Exception: {}", customerId, e);
                throw new ProcedureException(CustomerErrorCode.CALCULATOR_STOP_TO_CALCULATE_FAILURR);
            }

            if (!(CodeConst.SUCCESS == loanAmountResponse.getResultCode())) {
                LOGGER.warn("[Calculate Loan Amount]Fail to calculate loanAmount. CustomerId:{}", customerId);
                throw new ProcedureException(CustomerErrorCode.CALCULATOR_STOP_TO_CALCULATE_FAILURR);
            }

            BigDecimal loanAmount = loanAmountResponse.getLoanAmount();
            calculationResultDTO.setLoanAmount(loanAmount);

            CustomerCreditData creditData = commonReader.getEntityByCustomerId(CustomerCreditData.class, customerId);
            if (Objects.isNull(creditData)) {
                creditData = new CustomerCreditData();
                creditData.setCustomerId(customerId);
            }
            creditData.setMaximumCreditAmount(loanAmount);
            creditData.setUpdateAt(DateUtil.getCurrentTimestamp());
            commonWriter.save(creditData);

            return AjaxResult.success(calculationResultDTO);
        } catch (ProcedureException e) {
            return AjaxResult.success(calculationResultDTO);
        }
    }
}
