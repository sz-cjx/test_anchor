package com.sztus.azeroth.microservice.customer.server.service;

import com.sztus.azeroth.microservice.customer.client.object.parameter.enumerate.CustomerErrorCode;
import com.sztus.azeroth.microservice.customer.client.object.parameter.request.UpdateCreditAmountRequest;
import com.sztus.azeroth.microservice.customer.server.object.domain.CustomerCreditEvaluationInfo;
import com.sztus.azeroth.microservice.customer.server.object.domain.CustomerCreditEvaluationRecord;
import com.sztus.azeroth.microservice.customer.server.respository.reader.CommonReader;
import com.sztus.azeroth.microservice.customer.server.respository.writer.CommonWriter;
import com.sztus.azeroth.microservice.customer.server.type.constant.DbKey;
import com.sztus.azeroth.microservice.customer.server.util.CustomerCheckUtil;
import com.sztus.framework.component.core.type.ProcedureException;
import com.sztus.framework.component.core.util.DateUtil;
import com.sztus.framework.component.database.type.SqlOption;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Objects;

@Service
public class CustomerCreditEvaluationService {

    @Autowired
    private CommonReader commonReader;

    @Autowired
    private CommonWriter commonWriter;

    public void updateCreditAmount(UpdateCreditAmountRequest request) throws ProcedureException {
        Long customerId = request.getCustomerId();
        Long portfolioId = request.getPortfolioId();
        BigDecimal creditAmount = request.getCreditAmount();

        CustomerCreditEvaluationInfo creditEvaluation = getCustomerCreditEvaluation(customerId, portfolioId);
        if (Objects.isNull(creditEvaluation)) {
            throw new ProcedureException(CustomerErrorCode.CREDIT_EVALUATION_NOT_FIND);
        }

        CustomerCreditEvaluationRecord creditEvaluationRecord = new CustomerCreditEvaluationRecord();
        creditEvaluationRecord.setCustomerId(customerId);
        creditEvaluationRecord.setPortfolioId(portfolioId);
        creditEvaluationRecord.setCreditAmount(creditEvaluation.getCreditAmount());
        creditEvaluationRecord.setCreditPoint(creditEvaluation.getCreditPoint());
        creditEvaluationRecord.setEvaluatedAt(creditEvaluation.getUpdatedAt());
        creditEvaluationRecord.setCreatedAt(creditEvaluation.getCreatedAt());

        creditEvaluation.setCreditAmount(creditAmount);
        creditEvaluation.setUpdatedAt(DateUtil.getCurrentTimestamp());

        Long result = commonWriter.saveEntity(creditEvaluation);
        CustomerCheckUtil.checkSaveResult(result);

        commonWriter.save(creditEvaluationRecord);
    }

    public void saveCreditEvaluation(CustomerCreditEvaluationInfo creditEvaluation) throws ProcedureException {
        Long currentTimestamp = DateUtil.getCurrentTimestamp();
        CustomerCreditEvaluationInfo customerCreditEvaluation = getCustomerCreditEvaluation(creditEvaluation.getCustomerId(), creditEvaluation.getPortfolioId());
        if (Objects.nonNull(customerCreditEvaluation)) {
            creditEvaluation.setCreatedAt(customerCreditEvaluation.getCreatedAt());
        } else {
            creditEvaluation.setCreatedAt(currentTimestamp);
        }

        creditEvaluation.setUpdatedAt(currentTimestamp);
        Long result = commonWriter.saveEntity(creditEvaluation);
        CustomerCheckUtil.checkSaveResult(result);
    }

    public CustomerCreditEvaluationInfo getCustomerCreditEvaluation(Long customerId, Long portfolioId) throws ProcedureException {
        if (Objects.isNull(customerId) || Objects.isNull(portfolioId)) {
            throw new ProcedureException(CustomerErrorCode.PARAMETER_IS_INCOMPLETE);
        }

        SqlOption sqlOption = SqlOption.getInstance();
        sqlOption.whereEqual(DbKey.CUSTOMER_ID, customerId);
        sqlOption.whereEqual(DbKey.PORTFOLIO_ID, portfolioId);
        return commonReader.findByOptions(CustomerCreditEvaluationInfo.class, sqlOption.toString());
    }
}
