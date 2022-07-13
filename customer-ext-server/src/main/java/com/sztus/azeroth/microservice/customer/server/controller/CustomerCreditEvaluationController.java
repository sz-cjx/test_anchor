package com.sztus.azeroth.microservice.customer.server.controller;

import com.sztus.azeroth.microservice.customer.client.object.parameter.enumerate.CustomerErrorCode;
import com.sztus.azeroth.microservice.customer.client.object.parameter.request.SaveCreditEvaluationRequest;
import com.sztus.azeroth.microservice.customer.client.object.parameter.request.UpdateCreditAmountRequest;
import com.sztus.azeroth.microservice.customer.client.object.parameter.response.GetCreditEvaluationResponse;
import com.sztus.azeroth.microservice.customer.server.converter.CustomerConverter;
import com.sztus.azeroth.microservice.customer.server.object.domain.CustomerCreditEvaluationInfo;
import com.sztus.azeroth.microservice.customer.server.service.CustomerCreditEvaluationService;
import com.sztus.framework.component.core.type.ProcedureException;
import com.sztus.framework.component.core.type.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customer")
public class CustomerCreditEvaluationController {

    @Autowired
    private CustomerCreditEvaluationService creditEvaluationService;

    @PostMapping("/credit-evaluation/credit-amount")
    public ResponseResult updateCreditAmount(
            @RequestBody UpdateCreditAmountRequest request
    ) {
        try {
            creditEvaluationService.updateCreditAmount(request);
            return ResponseResult.success();
        } catch (ProcedureException e) {
            return ResponseResult.failure(e);
        }
    }

    @GetMapping("/credit-evaluation")
    public GetCreditEvaluationResponse getCreditEvaluation(
            @RequestParam("customerId") Long customerId,
            @RequestParam("portfolioId") Long portfolioId
    ) throws ProcedureException {
        CustomerCreditEvaluationInfo creditEvaluation = creditEvaluationService.getCustomerCreditEvaluation(customerId, portfolioId);

        return CustomerConverter.INSTANCE.CustomerCreditEvaluationToView(creditEvaluation);
    }

    @PostMapping("/credit-evaluation")
    public void saveCreditEvaluation(
            @RequestBody SaveCreditEvaluationRequest request
    ) throws ProcedureException {
        CustomerCreditEvaluationInfo customerCreditEvaluationInfo = CustomerConverter.INSTANCE.CustomerCreditEvaluationViewToData(request);
        creditEvaluationService.saveCreditEvaluation(customerCreditEvaluationInfo);
    }

}
