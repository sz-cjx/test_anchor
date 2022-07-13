package com.sztus.azeroth.microservice.customer.client.api;

import com.sztus.azeroth.microservice.customer.client.object.parameter.request.SaveCreditEvaluationRequest;
import com.sztus.azeroth.microservice.customer.client.object.parameter.request.UpdateCreditAmountRequest;
import com.sztus.azeroth.microservice.customer.client.object.parameter.response.GetCreditEvaluationResponse;
import com.sztus.framework.component.core.type.ResponseResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "microservice-customer-ext", path = "/v4", contextId = "customer-credit-evaluation-api")
public interface CustomerCreditEvaluationApi {

    @PostMapping("/customer/credit-evaluation/credit-amount")
    ResponseResult updateCreditAmount(
            @RequestBody UpdateCreditAmountRequest request
    );

    @PostMapping("/customer/credit-evaluation")
    void saveCreditEvaluation(
            @RequestBody SaveCreditEvaluationRequest request
    );

    @GetMapping("/customer/credit-evaluation")
    GetCreditEvaluationResponse getCreditEvaluation(
            @RequestParam("customerId") Long customerId,
            @RequestParam("portfolioId") Long portfolioId
    );

}
