package com.arbfintech.microservice.loan.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.arbfintech.microservice.loan.client.TransactionItemfeignClient;

@Service
public class TransactionItemApiService {

	@Autowired
	private TransactionItemfeignClient transactionItemfeignClient;
	
	
	public String getTransactionItemBycontractNo(String contractNo) {
		return transactionItemfeignClient.selectTransactionItemByContractNo(contractNo);
	}
}
