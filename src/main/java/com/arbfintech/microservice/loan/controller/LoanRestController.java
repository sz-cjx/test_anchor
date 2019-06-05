package com.arbfintech.microservice.loan.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.arbfintech.framework.component.core.enumerate.LoanStatusEnum;
import com.arbfintech.framework.component.core.type.RabbitMessage;
import com.arbfintech.framework.component.core.util.EmailUtil;
import com.arbfintech.framework.component.core.util.FreeMarkerUtil;
import com.arbfintech.microservice.loan.client.MaintenanceFeignClient;
import com.arbfintech.microservice.loan.entity.*;
import com.arbfintech.microservice.loan.service.LoanService;
import com.arbfintech.microservice.loan.service.SendLoanService;
import com.arbfintech.microservice.loan.service.TimeLineApiService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * 2018-12-24
 * 1.完成pps所需数据的修改
 * 
 * @author Ernest Gou
 *
 */
@RestController
@RequestMapping("/loan")
public class LoanRestController {

	private static final Logger logger = LoggerFactory.getLogger(LoanRestController.class);

	@Autowired
	private TimeLineApiService timeLineApiService;

	@Autowired
    private LoanService loanService;

	@Autowired
	private SendLoanService sendLoanService;

	@Autowired
	private MaintenanceFeignClient maintenanceFeignClient;

    @PostMapping("/newLead")
    public String saveLoanFromLeads(@RequestBody String leads){
        loanService.saveLoanFromLead(leads);
        return null;
    }

	@GetMapping("/newLoan")
	public String getNewLoan(@RequestParam("category") Integer category,
							 @RequestParam("priority") Integer priority,
							 @RequestParam("status") String statusListStr,
							 @RequestParam(value = "operatorNo", required = false) String operatorNo,
							 @RequestParam(value = "operatorName", required = false) String operatorName){
		String result;
		List<Integer> statusList = JSON.parseArray(statusListStr, Integer.class);

		if(statusList.contains(LoanStatusEnum.INITIALIZED.getValue())){
			result = loanService.getNewContractNoOfAgentReview(category, statusList, operatorNo, operatorName);
		}else {
			result = loanService.getContractNoByCategoryAndStatus(category, statusList, operatorNo, operatorName);
		}

		return result;
	}

    @PostMapping("/properties")
    String updateLoanProperty(@RequestParam (value = "loanId")Integer loanId,
                              @RequestParam (value = "section")String section,
                              @RequestParam(value ="properties" ) String properties,
							  @RequestParam(value = "additionalData", required = false) String addictionData){
        loanService.saveLoanProperty(loanId, section, properties, addictionData);
        return "OK";
    }

    @GetMapping("/contractInfo/{contractNo}")
	public String getContractNoInfo(@PathVariable("contractNo") String contractNo){
		JSONObject resultData = loanService.getContractInfoByContractNo(contractNo);
		return JSON.toJSONString(resultData);
	}

	@GetMapping("/contractOnly")
	public String getContractOnlyByContractNo(@RequestParam("contractNo") String contractNo){
		return  loanService.getContractOnlyByContractNo(contractNo);
	}

	@GetMapping("/contractNo")
	public String getContractAndContractNoItemByContractNo(@RequestParam("contractNo") String contractNo){
		JSONObject resultData = loanService.getContractAndContractNoItemByContractNo(contractNo);
		return JSON.toJSONString(resultData,SerializerFeature.WriteMapNullValue);
	}

	@GetMapping("/consumer")
	public String getConsumerData(@RequestParam("ssns") String ssns){
		List<Customer> customerData = loanService.getCustomerData(ssns);
		return JSON.toJSONString(customerData);
	}

	@GetMapping("/loan_contracts")
	public String getLoanContractsByStatus(@RequestParam("loanStatus") int loanStatus){
		String result = "";
		List<Loan> loans = loanService.getLoanContractsByStatus(loanStatus);
		if(loans != null){
			result = JSON.toJSONString(loans);
		}

		return result;
	}

	@GetMapping("/loan_contract_overview")
	public String getLoanContractOverview(@RequestParam("loanStatus") int loanStatus, @RequestParam("startTime") Date startTime){
		List<LoanOverView> loanOverViewList = loanService.getLoanContractsOverView(loanStatus, startTime);
		return JSON.toJSONString(loanOverViewList, SerializerFeature.WRITE_MAP_NULL_FEATURES);
	}

	@GetMapping("/loan_contract/{loanContractId}")
	public String getLoanContractByContactId(@PathVariable("loanContractId") int loanContactId){
		logger.info("Start query loan loan by Id:{}",loanContactId);
		Loan loan = loanService.getLoanByLoanId(loanContactId);

		String result = "";
		if(loan != null){
			result = loan.toString();
		}
		logger.info("Find the Loan Data:" + result);
		return result;
	}

	@GetMapping("/loan/{loanContractNo}")
	public String getLoanContractByContactNo(@PathVariable("loanContractNo") String loanContactNo){
		logger.info("Start query loan loan by contractNo:{}",loanContactNo);
		Loan loan = loanService.getLoanByContactNo(loanContactNo);

		String result = "";
		if(loan != null){
			result = loan.toString();
		}
		logger.info("Find the Loan Data:" + result);
		return result;
	}

	@GetMapping("/loan_formed/{loanId}")
	public String getFormedLoanById(@PathVariable("loanId") int loanId){
		logger.info("Start query loan loan by Id:{}",loanId);
		JSONObject jsonObject = loanService.getFormedLoanDataById(loanId);

		String result = "";
		if(jsonObject != null){
			result = JSON.toJSONString(jsonObject);
		}
		logger.info("Find the Formed Loan Data:" + result);
		return result;
	}

	@GetMapping("/contract_formed/{contractNo}")
	public String getFormedContractByContactNo(@PathVariable("contractNo") String contactNo){
		logger.info("Start query loan loan by Number:{}",contactNo);
		JSONObject jsonObject = loanService.getFormedContractDataByContactNo(contactNo);

		String result = "";
		if(jsonObject != null){
			result = JSON.toJSONString(jsonObject);
		}
		logger.info("Find the Formed Contract Data:" + result);
		return result;
	}

	@GetMapping("/exited_loan_info/{leadId}")
	public String findExitedLoanInfo(@PathVariable("leadId")Integer leadId) {
		return loanService.findByLeadId(leadId);
	}

    @PostMapping("/message")
	public String sendMessageTest(@RequestParam(value = "contractId") String contractId) {

		JSONObject formatLoan = loanService.getFormedLoanDataById(Integer.parseInt(contractId));
		formatLoan.put("bankInterestDue", 0);
		String contract = JSON.toJSONString(formatLoan);
		logger.error("发送数据：" + contract);
        RabbitMessage message=new RabbitMessage();
        message.setCreateTime(new Date());
        message.setMessageData(contract);
        message.setOperationName("send loan to loanSchedule");
        message.setProducer("Contract service");

        sendLoanService.send(message);
        return "send success!";
    }
	
	@GetMapping("/stat/{status}")
	public String countContractByLeadIdAndStatus(@PathVariable("status") Integer status,
			@RequestParam(value = "leadIdsArray") String leadIdsArray) {

		return loanService.countContractByLeadIdAndStatus(leadIdsArray, status);
	}

	@PostMapping("/loan-status")
	public String updateLoanStatus(@RequestParam(value = "contractNo") String contractNo,
								   @RequestParam(value = "status") String status,
								   @RequestParam(value = "additionalData", required = false) String additionalData){

		Loan loan = loanService.getLoanByContactNo(contractNo);
		if (loan !=null) {
			boolean isStatusFound = false;
			Integer sourseStatus = loan.getLoanStatus();
			for (LoanStatusEnum e : LoanStatusEnum.values()) {

				if (e.getText().equals(status)) {
					isStatusFound = true;
					loan.setLoanStatus(e.getValue());
					loan.setLoanStatusText(status);
					loan.setUpdateTime((new Date()).getTime());
					loan.setLockedOperatorNo(null);
					loan.setLockedOperatorName(null);
					loanService.saveLoanOnly(loan);

					JSONObject jsonObject = JSON.parseObject(additionalData);
					Payment payment = loan.getPayment();
					if(payment!=null) {
						payment.setItems("");
						loan.setPayment(payment);
					}
					jsonObject.put("appData",JSON.toJSONString(loan));
					timeLineApiService.addLoanStatusChangeTimeline(sourseStatus,e.getValue(),jsonObject.toJSONString());
				}
			}
			String email = "";
			if(isStatusFound){
				logger.info("Update Loan status to ：{} for loan: {}", status, contractNo);

				if("Approved Application".equals(status)){
					try {
						String title = "Your loan has been approved";

						String tempaltes = maintenanceFeignClient.listEmailTemplateByPortfolioId(1);
						JSONArray jsonArray = JSON.parseArray(tempaltes);
						for(Object o : jsonArray){
							JSONObject jsObject = (JSONObject)o;
							if("LOS-003".equals(jsObject.getString("code"))){
								String template = jsObject.getString("template");
								JSONObject dataObject = new JSONObject();

								Personal personal = loan.getPersonal();
								if(personal != null){
									String firstName = personal.getFirstName();
									if(StringUtils.isEmpty(firstName)){
										firstName = "";
									}
									dataObject.put("firstName", firstName);

									String lastName = personal.getLastName();
									if(StringUtils.isEmpty(lastName)){
										lastName = "";
									}
									dataObject.put("lastName",lastName);

									email = personal.getEmail();
								}

								String content = FreeMarkerUtil.fillHtmlTemplate(template, dataObject);
								EmailUtil.emailSender(email,false,title,	content, "","");
							}
						}
						logger.info("Mail has been sent to :" + email);
					}catch (Exception e){
						logger.info("Mail sent Failed to :" + email);
						e.printStackTrace();
					}
				}

				return "OK";
			}else {
				logger.error("Invalid status ：{}", status);
				return "NOK";
			}
		}else{
			return "NOK";
		}
	}


	@PostMapping("/loan-withdrawn")
	public String withdrawLoan(
			@RequestParam(value = "contractNo") String contractNo,
			@RequestParam(value = "status") String status,
			@RequestParam("withdrawnCode")Integer withdrawnCode){
		Loan loan = loanService.getLoanByContactNo(contractNo);
		if (loan !=null) {
			boolean isStatusFound = false;
			for (LoanStatusEnum e : LoanStatusEnum.values()) {

				if (e.getText().equals(status)) {
					isStatusFound = true;
					loan.setLoanStatus(e.getValue());
					loan.setWithdrawnCode(withdrawnCode);
					loan.setUpdateTime((new Date()).getTime());
					loanService.saveLoanOnly(loan);
				}
			}
			if(isStatusFound){
				return "OK";
			}else {
				return "NOK";
			}
		}else{
			return "NOK";
		}
	}

	@GetMapping("/searching")
	public String searchLeads(
			@RequestParam(value = "name") String name,
			@RequestParam(value = "ssn") String ssn,
			@RequestParam(value = "applicationNumber") String applicationNumber,
			@RequestParam(value = "emailAddress") String emailAddress,
			@RequestParam(value = "phoneNumber") String phoneNumber,
			@RequestParam(value = "accountNumber") String accountNumber
	){
		return loanService.searchLeadsInfo(name, ssn, applicationNumber, emailAddress, phoneNumber, accountNumber);
	}

	@GetMapping("/todo-list")
	public String getToDoList(@RequestParam(value = "operatorNo") String operatorNo,
							  @RequestParam(value = "operationNameList") String operationNameListStr,
							  @RequestParam(value = "queryStatusList") String queryStatusList){

		String toDoArr= loanService.getTodoListLoanInfo(operatorNo,operationNameListStr, queryStatusList);

		return toDoArr;
	}


	@GetMapping("/withdrawn-loan")
	public String countWithdrawnLoan(@RequestParam(value = "startTime") String startTime,
									 @RequestParam(value = "endTime") String endTime){
		return loanService.countWithDrawnLoan(startTime,endTime);
	}

	@GetMapping("/pending-summary")
	public String countPendingApplicationSummary(@RequestParam(value = "pendingSummaryIds") String pendingSummaryIds){
//		return loanService.countPendingSummary(pendingSummaryIds);
		return loanService.getLoanAndPersonalInfoByIds(pendingSummaryIds);
	}

	@GetMapping("/dashboard/pending-summary")
	public String pendingSummary(@RequestParam(value = "startTime") String startTime,
								 @RequestParam(value = "endTime") String endTime){
		return loanService.newPendingSummary(Long.parseLong(startTime),Long.parseLong(endTime));
	}

	@GetMapping("/recent-loans")
	public String listRecentLoans(@RequestParam(value = "loanId")Integer loanId){
		String result = loanService.listReturningCustomerRecentLoans(loanId);
		return result;
	}

	@PostMapping("/follow-up")
	public String setFollowUp(@RequestParam(value = "type") String type,
							  @RequestParam(value = "timeData")String timeData,
							  @RequestParam(value = "contractNo")String contractNo){
		return loanService.setFollowUp(type, timeData, contractNo);
	}

	@GetMapping("/generation/newloan")
	public String generateNewLoan(@RequestParam(value = "operatorNo")String operatorNo,
								  @RequestParam(value = "loanStatus")Integer loanStatus){
		return loanService.generateNewLoan(operatorNo,loanStatus);
	}

	@PostMapping("/grab-loan")
	public String grabLoan(@RequestParam(value = "contractNo") String contractNo,
						   @RequestParam(value = "operatorNo") String operatorNo,
						   @RequestParam(value = "operatorName")String operatorName) {
		return loanService.grabLoan(contractNo, operatorNo,operatorName);
	}


	@PostMapping("/grab-loan-accept")
	public String acceptGrab(@RequestParam(value = "grabId")Integer grabId){
		return loanService.acceptGrabLoan(grabId);
	}

	@PostMapping("/grab-loan-reject")
	public String rejectGrabLoan(@RequestParam(value = "grabId") Integer grabId){
		return loanService.rejectGrabLoan(grabId);
	}

	@PostMapping("/unlock")
	public String unlockLoan(@RequestParam(value = "contractNo")String contractNo){
		return loanService.unlockLoan(contractNo);
	}


	@PostMapping("/loans-auto")
	public String saveLoanFromCustomerInAuto(@RequestParam(value = "customerInAutoStr")String customerInAutoStr){
		return loanService.saveLoanFromCustomerInAuto(customerInAutoStr);
	}

}
