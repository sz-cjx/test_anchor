package com.arbfintech.microservice.loan.service;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

@Service
public class OamApiService {

	/**
	 * 获取到ContractNo的suffix
	 * @return
	 */
	public JSONObject getOptions(Integer portfolioId) {
		JSONObject options=new JSONObject();
		options.put("suffix", "C");
		
		return options;
	}
}
