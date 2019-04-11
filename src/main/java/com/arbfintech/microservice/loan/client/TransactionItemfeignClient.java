package com.arbfintech.microservice.loan.client;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "microservice-business")
@RequestMapping("/business/transaction")
public interface TransactionItemfeignClient {

	@GetMapping("/transaction-item")
	public String selectTransactionItemByContractNo(@RequestParam(name = "contractNo") String contractNo);

}
