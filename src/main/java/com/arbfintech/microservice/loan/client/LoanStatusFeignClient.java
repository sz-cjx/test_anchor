package com.arbfintech.microservice.loan.client;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("microservice-business")
@RequestMapping("/business/timeline")
public interface LoanStatusFeignClient {

    @GetMapping("/loan-status")
    public String getLoanStatusByContractNo(@RequestParam("contractNo") String contractNo);
}
