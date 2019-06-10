package com.arbfintech.microservice.loan.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.arbfintech.framework.component.core.enumerate.*;
import com.arbfintech.framework.component.core.util.DateUtil;
import com.arbfintech.framework.component.core.util.EnumUtil;
import com.arbfintech.framework.component.core.util.StringUtil;
import com.arbfintech.microservice.loan.client.LoanStatusFeignClient;
import com.arbfintech.microservice.loan.client.TimelineFeignClient;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.HashSet;

/**
 * @author Wade He
 */
@Service
public class TimeLineApiService {

	private Logger logger = LoggerFactory.getLogger(TimeLineApiService.class);
	
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

	public HashSet<String> getTodoContractNo(String operatorNo,String operationNameListStr, String queryStatusList){
		logger.info("operationNameListStr:" + operationNameListStr);
		return timelineFeignClient.getToDoListContractNo(operatorNo, operationNameListStr, queryStatusList);
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
			timelineObj.put("appData", additionalObj.getString("appData"));
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
		String appDataStr = "";
		if(StringUtils.isNotEmpty(additionalData)){
			JSONObject additionalObj = JSONObject.parseObject(additionalData);
			loanStatus = additionalObj.getInteger("loanStatus");
			note = additionalObj.getString("note");
			operatorNo = additionalObj.getString("operatorNo");
			operatorName = additionalObj.getString("operatorName");
			contractNo = additionalObj.getString("contractNo");
			appDataStr = additionalObj.getString("appData");
		}

		JSONObject targetSnapshot = new JSONObject();
		JSONArray updateArr = JSONArray.parseArray(updateProperties);

		for(int i = 0; i < updateArr.size(); i ++){
			JSONObject updateObj = updateArr.getJSONObject(i);
			if (updateObj.containsKey("fieldKey")){
				convertData(updateObj);
			}
		}

		targetSnapshot.put("updateProperties",updateArr);
		targetSnapshot.put("status",loanStatus);

		resultOb.put("eventTime", new Date());
		resultOb.put("eventType", EventTypeEnum.UPDATE_REGISTER_INFORAMTION.getValue().toString());
		resultOb.put("eventDescription", "Stamp Operation");
		resultOb.put("targetSnapshot", targetSnapshot);
		resultOb.put("relationNo", contractNo);
		resultOb.put("contractNo", contractNo);
		resultOb.put("note",note);
		resultOb.put("operatorName", operatorName);
		resultOb.put("operatorNo", operatorNo);
		resultOb.put("appData",appDataStr);
		timelineFeignClient.addTimeline(JSONObject.toJSONString(resultOb));


		return JSONObject.toJSONString(resultOb);
	}

	private void convertData(JSONObject updateObj) {
		String fieldKey = updateObj.getString("fieldKey");
		if ("documentCreateTime".equals(fieldKey) || ("documentSignatureTime".equals(fieldKey))){
			Long originFieldValue = updateObj.getLong("originFieldValue");
			Long fieldValue = updateObj.getLong("fieldValue");
			updateObj.put("originFieldValue", DateUtil.datetime2str(DateUtil.long2date(originFieldValue)));
			updateObj.put("fieldValue", DateUtil.datetime2str(DateUtil.long2date(fieldValue)));
		}
		if ("language".equals(fieldKey)){
			String originFieldValue = updateObj.getString("originFieldValue");
			String fieldValue = updateObj.getString("fieldValue");
			fieldValue = EnumUtil.getByCode(LanguageEnum.class,fieldValue).getText();
			updateObj.put("fieldValue", fieldValue);
			LanguageEnum byCode = EnumUtil.getByCode(LanguageEnum.class, originFieldValue);
			if (byCode == null){
				updateObj.put("originFieldValue", "");
			}else{
				originFieldValue = byCode.getText();
				updateObj.put("originFieldValue", originFieldValue);
			}
		}
		if ("verifiedBy".equals(fieldKey)){
			String originFieldValue = updateObj.getString("originFieldValue");
			String fieldValue = updateObj.getString("fieldValue");
			fieldValue = EnumUtil.getByCode(VerifiedByBankEnum.class,fieldValue).getText();
			updateObj.put("fieldValue", fieldValue);
			VerifiedByBankEnum byCode = EnumUtil.getByCode(VerifiedByBankEnum.class, originFieldValue);
			if (byCode == null){
				updateObj.put("originFieldValue", "");
			}else{
				originFieldValue = byCode.getText();
				updateObj.put("originFieldValue", originFieldValue);
			}
		}
		if ("bankAccountType".equals(fieldKey)){
			Integer originFieldValue = updateObj.getInteger("originFieldValue");
			Integer fieldValue = updateObj.getInteger("fieldValue");
			updateObj.put("fieldValue", EnumUtil.getByValue(BankAccountTypeEnum.class,fieldValue).getText());
			BankAccountTypeEnum byValue = EnumUtil.getByValue(BankAccountTypeEnum.class, originFieldValue);
			if (byValue == null){
				updateObj.put("originFieldValue", "");
			}else{
				updateObj.put("originFieldValue", byValue.getText());
			}
		}
		if ("bankAccountOwner".equals(fieldKey)){
			String originFieldValue = updateObj.getString("originFieldValue");
			String fieldValue = updateObj.getString("fieldValue");
			fieldValue = EnumUtil.getByCode(BankAccountOwnerEnum.class,fieldValue).getText();
			updateObj.put("fieldValue", fieldValue);
			BankAccountOwnerEnum byCode = EnumUtil.getByCode(BankAccountOwnerEnum.class, originFieldValue);
			if (byCode == null){
				updateObj.put("originFieldValue", "");
			}else{
				originFieldValue = byCode.getText();
				updateObj.put("originFieldValue", originFieldValue);
			}
		}
		if ("payrollType".equals(fieldKey)){
			Integer originFieldValue = updateObj.getInteger("originFieldValue");
			Integer fieldValue = updateObj.getInteger("fieldValue");
			updateObj.put("fieldValue", EnumUtil.getByValue(PayrollTypeEnum.class,fieldValue).getText());
			PayrollTypeEnum byValue = EnumUtil.getByValue(PayrollTypeEnum.class, originFieldValue);
			if (byValue == null){
				updateObj.put("originFieldValue", "");
			}else{
				updateObj.put("originFieldValue", byValue.getText());
			}
		}
		if ("payrollFrequency".equals(fieldKey)){
			Integer originFieldValue = updateObj.getInteger("originFieldValue");
			Integer fieldValue = updateObj.getInteger("fieldValue");
			updateObj.put("fieldValue", EnumUtil.getByValue(PayrollFrequencyEnum.class,fieldValue).getText());
			PayrollFrequencyEnum byValue = EnumUtil.getByValue(PayrollFrequencyEnum.class, originFieldValue);
			if (byValue == null){
				updateObj.put("originFieldValue", "");
			}else{
				updateObj.put("originFieldValue", byValue.getText());
			}
		}
		if ("bankException".equals(fieldKey)){
			String originFieldValue = updateObj.getString("originFieldValue");
			String fieldValue = updateObj.getString("fieldValue");
			fieldValue = EnumUtil.getByCode(ExceptionBankEnum.class,fieldValue).getText();
			updateObj.put("fieldValue", fieldValue);
			ExceptionBankEnum byCode = EnumUtil.getByCode(ExceptionBankEnum.class, originFieldValue);
			if (byCode == null){
				updateObj.put("originFieldValue", "");
			}else{
				originFieldValue = byCode.getText();
				updateObj.put("originFieldValue", originFieldValue);
			}
		}
		if ("transactionMode".equals(fieldKey)){
			Integer originFieldValue = updateObj.getInteger("originFieldValue");
			Integer fieldValue = updateObj.getInteger("fieldValue");
			updateObj.put("fieldValue", EnumUtil.getByValue(FuturePaymentModeEnum.class,fieldValue).getText());
			FuturePaymentModeEnum byValue = EnumUtil.getByValue(FuturePaymentModeEnum.class, originFieldValue);
			if (byValue == null){
				updateObj.put("originFieldValue", "");
			}else{
				updateObj.put("originFieldValue", byValue.getText());
			}
		}
		if ("program".equals(fieldKey)){
			Integer originFieldValue = updateObj.getInteger("originFieldValue");
			Integer fieldValue = updateObj.getInteger("fieldValue");
			updateObj.put("fieldValue", EnumUtil.getByValue(ProgramEnum.class,fieldValue).getText());
			ProgramEnum byValue = EnumUtil.getByValue(ProgramEnum.class, originFieldValue);
			if (byValue == null){
				updateObj.put("originFieldValue", "");
			}else{
				updateObj.put("originFieldValue", byValue.getText());
			}
		}
		if ("monthlyPayday".equals(fieldKey) || "secondPayday".equals(fieldKey)){
			String originFieldValue = updateObj.getString("originFieldValue");
			String fieldValue = updateObj.getString("fieldValue");
			if ("-1".equals(originFieldValue)){
				updateObj.put("originFieldValue", "Last Day Of Month");
			}
			if ("-1".equals(fieldValue)){
				updateObj.put("fieldValue", "Last Day Of Month");
			}
		}
		if ("bankAvailableBalance".equals(fieldKey) || "amountOfOpenLoans".equals(fieldKey) || "totalPrincipal".equals(fieldKey)){
			String originFieldValue = updateObj.getString("originFieldValue");
			String fieldValue = updateObj.getString("fieldValue");
			if (StringUtils.isNotEmpty(originFieldValue)){
				updateObj.put("originFieldValue", StringUtil.toCurrency(originFieldValue));
			}
			if (StringUtils.isNotEmpty(fieldValue)){
				updateObj.put("fieldValue", StringUtil.toCurrency(fieldValue));
			}
		}
	}

	public JSONArray getWorkedConteactNo(String operatorNo, Integer eventType){
		String contractNos=timelineFeignClient.getContractNoByEventTypeAndOperatorNo(operatorNo, eventType);
		return JSONArray.parseArray(contractNos);
	}

	public String addFollowUpTimeline(String contractNo, String additionalStr){
		JSONObject resultOb = new JSONObject();

		if(StringUtils.isNotEmpty(additionalStr)){
			JSONObject additionalObj = JSONObject.parseObject(additionalStr);
			String operatorNo = additionalObj.getString("operatorNo");
			String operatorName = additionalObj.getString("operatorName");

			resultOb.put("contractNo", contractNo);
			resultOb.put("relationNo", contractNo);
			resultOb.put("operatorNo", operatorNo);
			resultOb.put("operatorName", operatorName);
		}

		resultOb.put("eventTime", new Date());
		resultOb.put("eventType", EventTypeEnum.UPDATE_REGISTER_INFORAMTION.getValue().toString());
		resultOb.put("eventDescription", "Follow Up Operation");

		return timelineFeignClient.addTimeline(JSONObject.toJSONString(resultOb));
	}

	public String addLockOrUnlockOrGrabLockTimeLine(String contractNo, String additionalStr, String eventDescription){
		JSONObject resultOb = new JSONObject();

		if(StringUtils.isNotEmpty(additionalStr)){
			JSONObject additionalObj = JSONObject.parseObject(additionalStr);
			String operatorNo = additionalObj.getString("operatorNo");
			String operatorName = additionalObj.getString("operatorName");

			resultOb.put("contractNo", contractNo);
			resultOb.put("relationNo", contractNo);
			resultOb.put("operatorNo", operatorNo);
			resultOb.put("operatorName", operatorName);
		}

		resultOb.put("eventTime", new Date());
		resultOb.put("eventType", EventTypeEnum.UPDATE_REGISTER_INFORAMTION.getValue().toString());
		resultOb.put("eventDescription", eventDescription);

		return timelineFeignClient.addTimeline(JSONObject.toJSONString(resultOb));
	}

}