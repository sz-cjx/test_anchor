package com.arbfintech.microservice.customer.restapi.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.arbfintech.framework.component.algorithm.service.AlgorithmService;
import com.arbfintech.framework.component.algorithm.type.*;
import com.arbfintech.framework.component.cache.core.SimpleRedisRepository;
import com.arbfintech.framework.component.core.constant.CodeConst;
import com.arbfintech.framework.component.core.constant.GlobalConst;
import com.arbfintech.framework.component.core.enumerate.LoanTypeEnum;
import com.arbfintech.framework.component.core.enumerate.ModeEnum;
import com.arbfintech.framework.component.core.enumerate.StateEnum;
import com.arbfintech.framework.component.core.type.AjaxResult;
import com.arbfintech.framework.component.core.type.ProcedureException;
import com.arbfintech.framework.component.core.util.DateUtil;
import com.arbfintech.framework.component.core.util.EnumUtil;
import com.arbfintech.microservice.customer.object.constant.CustomerCacheKey;
import com.arbfintech.microservice.customer.object.constant.CustomerJsonKey;
import com.arbfintech.microservice.customer.object.dto.*;
import com.arbfintech.microservice.customer.object.entity.*;
import com.arbfintech.microservice.customer.object.enumerate.*;
import com.arbfintech.microservice.customer.object.util.CustomerFieldKey;
import com.arbfintech.microservice.customer.object.util.ExtentionJsonUtil;
import com.arbfintech.microservice.customer.restapi.repository.cache.AlgorithmRedisRepository;
import com.arbfintech.microservice.customer.restapi.repository.reader.CommonReader;
import com.arbfintech.microservice.customer.restapi.repository.writer.CommonWriter;
import com.arbfintech.microservice.loan.object.enumerate.LoanCategoryEnum;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
    private SimpleRedisRepository simpleRedisRepository;

    @Autowired
    private AlgorithmService algorithmService;

    public CalculationProcessDTO checkCalculationParam(Long customerId) throws ProcedureException {
        CalculationResultDTO calculationResultDTO = new CalculationResultDTO();
        LOGGER.info("[Calculate Loan Amount]Start calculate loan amount. CustomerId: {}", customerId);

        /**
         *  1、校验参数是否齐全（personal, contact, employment, bank, ibv）
         */
        CustomerProfile customerProfile = commonReader.getEntityByCustomerId(CustomerProfile.class, customerId);
        Boolean personalResult = ExtentionJsonUtil.containsKey(
                customerProfile, CustomerFieldKey.getPersonalCalculateFactorList());

        List<CustomerContactData> contactList = commonReader.listEntityByCustomerId(CustomerContactData.class, customerId);
        Boolean contactResult = false;
        if (!CollectionUtils.isEmpty(contactList)){
            List<Integer> contactTypeList = contactList.stream().map(CustomerContactData::getContactType).collect(Collectors.toList());
            if (contactTypeList.contains(CustomerContactTypeEnum.EMAIL.getValue())
                    && contactTypeList.contains(CustomerContactTypeEnum.CELL_PHONE.getValue())) {
                contactResult = true;
            }
        }

        CustomerEmploymentData customerEmployment = commonReader.getEntityByCustomerId(CustomerEmploymentData.class, customerId);
        Boolean employmentResult = ExtentionJsonUtil.containsKey(
                customerEmployment, CustomerFieldKey.getEmploymentCalculateFactorList());

        List<CustomerBankData> bankDataList = commonReader.listEntityByCustomerId(CustomerBankData.class, customerId);
        List<CustomerBankCardData> bankCardDataList = commonReader.listEntityByCustomerId(CustomerBankCardData.class, customerId);
        Boolean bankResult = !(CollectionUtils.isEmpty(bankDataList) || CollectionUtils.isEmpty(bankCardDataList));

        CustomerIbvData ibvData = commonReader.getEntityByCustomerId(
                CustomerIbvData.class, customerId);
        Boolean ibvResult = Objects.nonNull(ibvData) &&
                IBVRequestCodeStatusEnum.VERIFIED.getValue().equals(ibvData.getRequestCodeStatus());

        calculationResultDTO.setPersonalResult(personalResult);
        calculationResultDTO.setContactResult(contactResult);
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
        Boolean contactResult = calculationResultDTO.getContactResult();
        Boolean employmentResult = calculationResultDTO.getEmploymentResult();
        Boolean bankResult = calculationResultDTO.getBankResult();
        Boolean ibvResult = calculationResultDTO.getIbvResult();

        LOGGER.info("[Calculate Loan Amount]Check calculate parameter" +
                        "CustomerId:{}, PersonalResult:{}, ContactResult:{}, EmploymentResult:{}, BankResult:{}, IbvResult:{}",
                customerId, personalResult, contactResult, employmentResult, bankResult, ibvResult);
        if (!(personalResult && contactResult && employmentResult && bankResult && ibvResult)) {
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

    public String preCalculateInstallment (PaymentScheduleDTO paymentScheduleDTO) throws ProcedureException, ParseException {
        Long customerId = paymentScheduleDTO.getCustomerId();
        if (Objects.isNull(paymentScheduleDTO.getApprovedAmount())) {
            String PaymentScheduleStr = simpleRedisRepository.fetchString(CustomerCacheKey.PAYMENT_SCHEDULE_DATA, customerId);
            PaymentScheduleDTO result = null;
            if (!StringUtils.isEmpty(PaymentScheduleStr)) {
                JSONObject jsonObject = JSON.parseObject(PaymentScheduleStr) ;
                result = jsonObject.toJavaObject(PaymentScheduleDTO.class);
            }
            return AjaxResult.success(result);
        }
        CustomerEmploymentData employmentData = commonReader.getEntityByCustomerId(CustomerEmploymentData.class, customerId);
        InstallmentRequest installmentRequest = assembleInstallmentRequest(paymentScheduleDTO, employmentData);
        InstallmentResponse installmentResponse = algorithmService.calculateInstallmentList(
                installmentRequest
        );
        Double apr = null;
        try {
            apr = algorithmService.calculateApr(assembleAprRequest(employmentData, installmentResponse));
        } catch (ProcedureException e) {
            LOGGER.info("[Calculate Apr]Fail to calculate Apr. CustomerId:{}", customerId);
        }

        long firstDebitDate = installmentResponse.getFirstDebitDate();
        long lastDebitDate = installmentResponse.getLastDebitDate();

        paymentScheduleDTO.setEffectiveDate(DateUtil.timeStampToStr(installmentRequest.getEffectiveDate()));
        paymentScheduleDTO.setFirstDebitDate(DateUtil.timeStampToStr(firstDebitDate));
        paymentScheduleDTO.setLastDebitDate(DateUtil.timeStampToStr(lastDebitDate));
        paymentScheduleDTO.setRegularAmount(installmentResponse.getRegularAmount());
        paymentScheduleDTO.setTotalAmount(installmentResponse.getTotalAmount());
        paymentScheduleDTO.setTotalInterest(installmentResponse.getTotalInterest());
        paymentScheduleDTO.setTotalPrincipal(installmentResponse.getTotalPrincipal());
        paymentScheduleDTO.setTotalUnpaidFee(installmentResponse.getTotalUnpaidFee());
        paymentScheduleDTO.setApr(BigDecimal.valueOf(apr));

        JSONArray jsonArray = new JSONArray();
        for (Installment installment : installmentResponse.getInstallmentList()) {
            JSONObject jsonObject = JSON.parseObject(JSON.toJSONString(installment));
            jsonObject.put(CustomerJsonKey.INSTALLMENT_DATE, DateUtil.timeStampToStr(installment.getInstallmentDate()));
            jsonObject.put(CustomerJsonKey.TRANSACTION_DATE, DateUtil.timeStampToStr(installment.getTransactionDate()));
            jsonArray.add(jsonObject);
        }
        paymentScheduleDTO.setInstallmentList(jsonArray);

        simpleRedisRepository.cacheObjectByArgs(
                CustomerCacheKey.PAYMENT_SCHEDULE_DATA,
                JSON.toJSONString(paymentScheduleDTO),
                GlobalConst.SECONDS_IN_DAY * GlobalConst.MILLISECONDS_UNIT,
                customerId);

        return AjaxResult.success(paymentScheduleDTO);
    }

    private InstallmentRequest assembleInstallmentRequest(PaymentScheduleDTO paymentScheduleDTO, CustomerEmploymentData employmentData) throws ParseException {
        Long customerId = paymentScheduleDTO.getCustomerId();
        BigDecimal approvedAmount = paymentScheduleDTO.getApprovedAmount();
        String effectiveDateStr = paymentScheduleDTO.getEffectiveDate();
        Long effectiveDate = StringUtils.isNotBlank(effectiveDateStr) ?
                DateUtil.strToTimeStamp(effectiveDateStr) : DateUtil.getCurrentDate().getTime();
        LOGGER.info("[Pre-Calculate Payment Schedule]Assemble InstallmentRequest data. CustomerId:{}", customerId);

        InstallmentRequest request = new InstallmentRequest();
        // TODO need Complete
        request.setLoanId(-1L);
        request.setPortfolioId(-1L);
        request.setDefaultAchProvider(1L);
        request.setDefaultDebitCardProvider(9L);
        request.setDefaultOutHouseProvider(10L);
        request.setDefaultDisbursementMode(15014);
        request.setDefaultRepaymentMode(15014);

        request.setPaydayOnHoliday(employmentData.getPaydayOnHoliday());
        request.setPaydayOnAvailable(employmentData.getPaydayOnAvailable());
        request.setPayrollFrequency(employmentData.getPayrollFrequency());
        request.setLastPayday(employmentData.getLastPayday());
        request.setEffectiveDate(effectiveDate);
        request.setInterestRate(new BigDecimal("7.8"));
        request.setApprovedAmount(approvedAmount);
        request.setTotalPrincipal(approvedAmount);
        request.setFirstDayOfWeek(employmentData.getFirstDayOfWeek());
        request.setFirstDayOfMonth(employmentData.getFirstDayOfMonth());
        request.setSecondDayOfMonth(employmentData.getSecondDayOfMonth());

        return request;
    }

    public AprRequest assembleAprRequest(CustomerEmploymentData employmentData, InstallmentResponse installmentResponse) {
        AprRequest aprRequest = new AprRequest();
        List<Installment> installmentList = installmentResponse.getInstallmentList();
        LOGGER.info("[Pre-Calculate Payment Schedule]assemble AprRequest data");
        aprRequest.setPayrollFrequency(employmentData.getPayrollFrequency());
        aprRequest.setInstallmentList(installmentList);
        aprRequest.setTotalPrincipal(installmentResponse.getTotalPrincipal());
        aprRequest.setInterestRate(new BigDecimal("7.8"));
        aprRequest.setFirstDayOfMonth(employmentData.getFirstDayOfMonth());
        aprRequest.setSecondDayOfMonth(employmentData.getSecondDayOfMonth());
        return aprRequest;
    }


    public String verifyContactInformation (ContactVerifyDTO contactVerifyDTO) {
        Long customerId = contactVerifyDTO.getCustomerId();
        String verifyCode = contactVerifyDTO.getVerifyCode();

        HashMap<String, Object> condition = new HashMap<>();
        condition.put("customerId", customerId);
        condition.put("contactType", contactVerifyDTO.getContactType());

        CustomerContactData customerContactData = commonReader.getEntityByCondition(CustomerContactData.class, condition);
        customerContactData.setVerifiedStatus(VerifyEnum.VERIFIED.getValue());
        customerContactData.setVerifiedAt(DateUtil.getCurrentTimestamp());
        commonWriter.save(customerContactData);

        return AjaxResult.success();
    }

    public String customerToLoan(CustomerToLoanDTO customerToLoanDTO) throws ProcedureException {
        Long customerId = customerToLoanDTO.getCustomerId();
        Long bankId = customerToLoanDTO.getBankId();
        Long cardId = customerToLoanDTO.getCardId();
        LOGGER.info("[Customer TO Loan]Start to query customer relative information. CustomerId:{}", customerId);

        JSONObject dataJson = new JSONObject();
        assemblePaymentData(dataJson, customerId);
        assembleContactData(dataJson, customerId);
        assembleCreditData(dataJson, customerId);
        assembleEmploymentData(dataJson, customerId);
        assembleProfileData(dataJson, customerId);
        assembleStatementData(dataJson, customerId);
        assembleBankData(dataJson, customerId, bankId, cardId);

        // TODO assemble portfolio...
        dataJson.put(CustomerJsonKey.PORTFOLIO_ID, 3);

        return AjaxResult.success(dataJson);
    }

    private void assembleContactData(JSONObject dataJson, Long customerId) {
        LOGGER.info("[Customer TO Loan]Start to assemble Contact data. CustomerId:{}", customerId);
        List<CustomerContactData> customerContactData = commonReader.listEntityByCustomerId(CustomerContactData.class, customerId);

        customerContactData.parallelStream().peek(
                item -> {
                    CustomerContactTypeEnum contactTypeEnum = CustomerContactTypeEnum.getEnumByvalue(item.getContactType());
                    dataJson.put(contactTypeEnum.getKey(), item.getValue());
                }
        );
        LOGGER.info("[Customer TO Loan]Success to assemble Contact data. CustomerId:{}", customerId);
    }

    private void assembleCreditData(JSONObject dataJson, Long customerId) {
        LOGGER.info("[Customer TO Loan]Loan to assemble Credit data. CustomerId:{}", customerId);
        CustomerCreditData creditData = commonReader.getEntityByCustomerId(CustomerCreditData.class, customerId);
        dataJson.put(CustomerJsonKey.LOAN_AMOUNT, creditData.getMaximumCreditAmount());
        LOGGER.info("[Customer TO Loan]Success to assemble Credit data. CustomerId:{}", customerId);
    }

    private void assembleEmploymentData(JSONObject dataJson, Long customerId) {
        LOGGER.info("[Customer TO Loan]Loan to assemble Employment data. CustomerId:{}", customerId);
        CustomerEmploymentData employmentData = commonReader.getEntityByCustomerId(CustomerEmploymentData.class, customerId);
        JSONObject employmentJson = JSON.parseObject(JSON.toJSONString(employmentData));
        employmentJson.remove(CustomerJsonKey.ID);
        dataJson.putAll(employmentJson);
        LOGGER.info("[Customer TO Loan]Success to assemble Employment data. CustomerId:{}", customerId);
    }

    private void assembleProfileData(JSONObject dataJson, Long customerId) {
        LOGGER.info("[Customer TO Loan]Loan to assemble Profile data. CustomerId:{}", customerId);
        CustomerProfile customerProfile = commonReader.getEntityByCustomerId(CustomerProfile.class, customerId);
        JSONObject customerProfileJson = JSON.parseObject(JSON.toJSONString(customerProfile));
        customerProfileJson.remove(CustomerJsonKey.ID);
        customerProfileJson.remove(CustomerJsonKey.CREATED_AT);
        customerProfileJson.remove(CustomerJsonKey.UPDATED_AT);
        StateEnum stateEnume = StateEnum.getStateEnumByValue(customerProfileJson.getInteger(CustomerJsonKey.STATE));
        customerProfileJson.put(CustomerJsonKey.STATE, stateEnume.getText());
        dataJson.putAll(customerProfileJson);
        LOGGER.info("[Customer TO Loan]Success to assemble Profile data. CustomerId:{}", customerId);
    }

    private void assembleStatementData(JSONObject dataJson, Long customerId) {
        LOGGER.info("[Customer TO Loan]Loan to assemble Statement data. CustomerId:{}", customerId);
        List<CustomerStatementData> statementList = commonReader.listEntityByCustomerId(CustomerStatementData.class, customerId);
        dataJson.put(CustomerJsonKey.STATEMENT, statementList);
        LOGGER.info("[Customer TO Loan]Success to assemble Statement data. CustomerId:{}", customerId);
    }

    private void assembleBankData(JSONObject dataJson, Long customerId, Long bankId, Long cardId) {
        LOGGER.info("[Customer TO Loan]Loan to assemble Bank data. CustomerId:{}", customerId);
        HashMap<String, Object> condition = new HashMap<>();
        condition.put("id", bankId);

        CustomerBankData bankData = commonReader.getEntityByCondition(CustomerBankData.class, condition);
        JSONObject bankJson = JSON.parseObject(JSON.toJSONString(bankData));
        bankJson.remove(CustomerJsonKey.ID);
        dataJson.putAll(bankJson);

        condition.put("id", cardId);
        CustomerBankCardData cardData = commonReader.getEntityByCondition(CustomerBankCardData.class, condition);
        dataJson.put(CustomerJsonKey.CARD_NO, cardData.getCardNo());
        LOGGER.info("[Customer TO Loan]Success to assemble Bank data. CustomerId:{}", customerId);
    }

    private void assemblePaymentData(JSONObject dataJson, Long customerId) throws ProcedureException {
        String PaymentScheduleStr = simpleRedisRepository.fetchString(CustomerCacheKey.PAYMENT_SCHEDULE_DATA, customerId);
        if (StringUtils.isEmpty(PaymentScheduleStr)) {
            throw new ProcedureException(CustomerErrorCode.CUSTOMER_TO_LOAN_MISSING_APPROVED_AMOUNT);
        }
        JSONObject paymentJson = JSON.parseObject(PaymentScheduleStr) ;
        PaymentScheduleDTO paymentScheduleDTO = paymentJson.toJavaObject(PaymentScheduleDTO.class);

        dataJson.put(CustomerJsonKey.EFFECTIVE_DATE, paymentScheduleDTO.getEffectiveDate());
        dataJson.put(CustomerJsonKey.LOAN_AMOUNT, paymentScheduleDTO.getApprovedAmount());
        dataJson.put(CustomerJsonKey.APPROVED_AMOUNT, paymentScheduleDTO.getApprovedAmount());
        dataJson.put(CustomerJsonKey.TOTAL_AMOUNT, paymentScheduleDTO.getApprovedAmount());
        dataJson.put(CustomerJsonKey.FIRST_CREDIT_DATE, paymentScheduleDTO.getEffectiveDate());
        dataJson.put(CustomerJsonKey.FIRST_DEBIT_DATE, paymentScheduleDTO.getFirstDebitDate());
        dataJson.put(CustomerJsonKey.LAST_DEBIT_DATE, paymentScheduleDTO.getLastDebitDate());
        dataJson.put(CustomerJsonKey.REGULAR_AMOUNT, paymentScheduleDTO.getRegularAmount());
        dataJson.put(CustomerJsonKey.DEBIT_COUNT, paymentScheduleDTO.getInstallmentList().size() - 1);
        dataJson.put(CustomerJsonKey.INSTALLMENT, paymentScheduleDTO.getInstallmentList());
        dataJson.put(CustomerJsonKey.APR, paymentScheduleDTO.getApr());
    }
}
