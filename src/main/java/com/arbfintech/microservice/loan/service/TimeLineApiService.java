package com.arbfintech.microservice.loan.service;

import com.alibaba.fastjson.JSONObject;
import com.arbfintech.component.core.enumerate.EventTypeEnum;
import com.arbfintech.microservice.loan.client.TimelineFeignClient;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashSet;

/**
 * @author Wade He
 */
@Service
public class TimeLineApiService {
	
	
	@Autowired
	private TimelineFeignClient timelineFeignClient;

	public String getLoanStatus(String contractNo) {
		
		JSONObject loanStatus=new  JSONObject();
		String status = timelineFeignClient.getLoanStatusByContractNo(contractNo);
		
		loanStatus.put("status", status);
				
		return status;
	}

	public HashSet<String> getTodoContractNo(String operatorNo,Integer evebtType){

		return timelineFeignClient.getToDoListContractNo(operatorNo, evebtType);
	}


	public String addLoanStatusChangeTimeline(String contractNo, Integer sourceStatus, Integer targetStatus, String additionData){

		JSONObject sourceData = new JSONObject();
		sourceData.put("loanStatus", sourceStatus == null ? "" : sourceStatus);

		JSONObject targetData = new JSONObject();
		targetData.put("loanStatus", targetStatus);

		JSONObject timelineObj = new JSONObject();
		timelineObj.put("contractNo", contractNo);
		timelineObj.put("eventTime", new Date());
		timelineObj.put("eventType", EventTypeEnum.UPDATE_LOAN_STATUS.getValue().toString());
		timelineObj.put("eventDescription", "Loan Status change Operation");
		timelineObj.put("sourceData", sourceData);
		timelineObj.put("targetData", targetData);

		if(StringUtils.isNotEmpty(additionData)){
			JSONObject additionalObj = JSONObject.parseObject(additionData);
			timelineObj.put("note", additionalObj.getString("note"));
			timelineObj.put("operatorName", additionalObj.getString("operatorName"));
			timelineObj.put("operatorNo", additionalObj.getString("operatorNo"));
		}

		return timelineFeignClient.addTimeline(JSONObject.toJSONString(timelineObj));
	}

}