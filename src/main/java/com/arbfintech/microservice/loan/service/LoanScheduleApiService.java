package com.arbfintech.microservice.loan.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.arbfintech.microservice.loan.client.LoanScheduleFeignClient;

@Service
public class LoanScheduleApiService {

	@Autowired
	private LoanScheduleFeignClient loanScheduleFeignClient;
	
	
	
	
	public String getScheduleItemsInfo(String contractNo) {
		
		
		return loanScheduleFeignClient.getScheduleAndScheduleItem(contractNo);
	}




}
