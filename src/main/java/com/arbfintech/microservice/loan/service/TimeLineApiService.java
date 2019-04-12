package com.arbfintech.microservice.loan.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.arbfintech.component.core.enumerate.EventTypeEnum;
import com.arbfintech.microservice.loan.client.LoanStatusFeignClient;
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


	@Autowired
	private LoanStatusFeignClient loanStatusFeignclient;

	public String getLoanStatus(String contractNo) {
		
		JSONObject loanStatus=new  JSONObject();
		String status = loanStatusFeignclient.getLoanStatusByContractNo(contractNo);
		
		loanStatus.put("status", status);
				
		return status;
	}

	public HashSet<String> getTodoContractNo(String operatorNo,Integer evebtType){

		return timelineFeignClient.getToDoListContractNo(operatorNo, evebtType);
	}

	public String addLoanStatusChangeTimeline(Integer sourceStatus, Integer targetStatus, String additionData){

		JSONObject sourceData = new JSONObject();
		sourceData.put("loanStatus", sourceStatus == null ? "" : sourceStatus);

		JSONObject targetData = new JSONObject();
		targetData.put("loanStatus", targetStatus);

		JSONObject timelineObj = new JSONObject();
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
			timelineObj.put("contractNo", additionalObj.getString("contractNo"));
		}
		return timelineFeignClient.addTimeline(JSONObject.toJSONString(timelineObj));
	}

	public String addSaveTimeline(String updateProperties, String additionalData){
		JSONObject resultOb = new JSONObject();

		String contractNo = "";
		String note = "";
		String operatorNo ="";
		String operatorName = "";
		Integer loanStatus = 0;
		if(StringUtils.isNotEmpty(additionalData)){
			JSONObject additionalObj = JSONObject.parseObject(additionalData);
			loanStatus = additionalObj.getInteger("loanStatus");
			note = additionalObj.getString("note");
			operatorNo = additionalObj.getString("operatorNo");
			operatorName = additionalObj.getString("operatorName");
			contractNo = additionalObj.getString("contractNo");
		}

		JSONObject targetSnapshot = new JSONObject();
		JSONArray updateArr = JSONArray.parseArray(updateProperties);
		targetSnapshot.put("updateProperties",updateArr);
		targetSnapshot.put("status",loanStatus);

		resultOb.put("eventTime", new Date());
		resultOb.put("eventType", EventTypeEnum.UPDATE_REGISTER_INFORAMTION.getValue().toString());
		resultOb.put("eventDescription", "Save Operation");
		resultOb.put("targetSnapshot", targetSnapshot);
		resultOb.put("relationNo", contractNo);
		resultOb.put("contractNo", contractNo);
		resultOb.put("note",note);
		resultOb.put("operatorName", operatorName);
		resultOb.put("operatorNo", operatorNo);
		timelineFeignClient.addTimeline(JSONObject.toJSONString(resultOb));

		return JSONObject.toJSONString(resultOb);
	}
}