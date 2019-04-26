package com.arbfintech.microservice.loan.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.arbfintech.component.core.enumerate.LoanStatusEnum;
import com.arbfintech.component.core.message.RabbitMessage;
import com.arbfintech.microservice.loan.entity.Customer;
import com.arbfintech.microservice.loan.entity.Loan;
import com.arbfintech.microservice.loan.entity.LoanOverView;
import com.arbfintech.microservice.loan.service.LoanService;
import com.arbfintech.microservice.loan.service.SendLoanService;
import com.arbfintech.microservice.loan.service.TimeLineApiService;
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

	private static final Logger LOG = LoggerFactory.getLogger(LoanRestController.class);

	@Autowired
	private TimeLineApiService timeLineApiService;

	@Autowired
    private LoanService loanService;

	@Autowired
	private SendLoanService sendLoanService;

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
        loanService.saveLoanProperty(loanId, section, properties);
        timeLineApiService.addSaveTimeline(properties, addictionData);
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
		LOG.info("Start query loan loan by Id:{}",loanContactId);
		Loan loan = loanService.getLoanByLoanId(loanContactId);

		String result = "";
		if(loan != null){
			result = loan.toString();
		}
		LOG.info("Find the Loan Data:" + result);
		return result;
	}

	@GetMapping("/loan/{loanContractNo}")
	public String getLoanContractByContactNo(@PathVariable("loanContractNo") String loanContactNo){
		LOG.info("Start query loan loan by contractNo:{}",loanContactNo);
		Loan loan = loanService.getLoanByContactNo(loanContactNo);

		String result = "";
		if(loan != null){
			result = loan.toString();
		}
		LOG.info("Find the Loan Data:" + result);
		return result;
	}

	@GetMapping("/loan_formed/{loanId}")
	public String getFormedLoanById(@PathVariable("loanId") int loanId){
		LOG.info("Start query loan loan by Id:{}",loanId);
		JSONObject jsonObject = loanService.getFormedLoanDataById(loanId);

		String result = "";
		if(jsonObject != null){
			result = JSON.toJSONString(jsonObject);
		}
		LOG.info("Find the Formed Loan Data:" + result);
		return result;
	}

	@GetMapping("/contract_formed/{contractNo}")
	public String getFormedContractByContactNo(@PathVariable("contractNo") String contactNo){
		LOG.info("Start query loan loan by Number:{}",contactNo);
		JSONObject jsonObject = loanService.getFormedContractDataByContactNo(contactNo);

		String result = "";
		if(jsonObject != null){
			result = JSON.toJSONString(jsonObject);
		}
		LOG.info("Find the Formed Contract Data:" + result);
		return result;
	}

	@GetMapping("/exited_loan_info/{leadId}")
	public String findExitedLoanInfo(@PathVariable("leadId")Integer leadId) {
		return loanService.findByLeadId(leadId);
	}

    @PostMapping("/message")
	public String sendMessageTest(@RequestParam(value = "contractId") String contractId) {

		String contract = JSON.toJSONString(loanService.getFormedLoanDataById(Integer.parseInt(contractId)));

		LOG.error("发送数据：" + contract);
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
								   @RequestParam(value = "status") String status){

		Loan loan = loanService.getLoanByContactNo(contractNo);

		if (loan !=null) {
			boolean isStatusFound = false;
			for (LoanStatusEnum e : LoanStatusEnum.values()) {

				if (e.getText().equals(status)) {
					isStatusFound = true;
					loan.setStatus(e.getValue());
					loanService.saveLoanOnly(loan);
				}
			}

			if(isStatusFound){
				LOG.info("Update Loan status to ：{} for loan: {}", status, contractNo);
				return "OK";
			}else {
				LOG.error("Invalid status ：{}", status);
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
					loan.setStatus(e.getValue());
					loan.setWithdrawnCode(withdrawnCode);
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
	public String getToDolist(@RequestParam(value = "operatorNo")String operatorNo,
							  @RequestParam(value = "eventtype") Integer eventtype ) {

//		String operatorNo = "12";
//		Integer eventtype = 2001;
		String toDoArr= loanService.getTodoListLoanInfo(operatorNo,eventtype);

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
}
