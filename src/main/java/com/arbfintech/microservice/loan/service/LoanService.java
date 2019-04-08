package com.arbfintech.microservice.loan.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.arbfintech.component.core.constant.JsonKeyConst;
import com.arbfintech.component.core.enumerate.LoanStatusEnum;
import com.arbfintech.component.core.enumerate.converter.LoanStatusConverter;
import com.arbfintech.component.core.enumerate.converter.WithdrawnResonConverter;
import com.arbfintech.component.core.util.BigDecimalUtil;
import com.arbfintech.component.core.util.DateUtil;
import com.arbfintech.component.core.util.EnumUtil;
import com.arbfintech.component.core.util.StringUtil;
import com.arbfintech.microservice.loan.client.BusinessFeignClient;
import com.arbfintech.microservice.loan.client.MetadataFeignClient;
import com.arbfintech.microservice.loan.entity.*;
import com.arbfintech.microservice.loan.repository.*;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

/**
 * @author Richard
 */
@Service
public class LoanService {

    private static final Logger logger = LoggerFactory.getLogger(LoanService.class);

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private PersonalRepository personalRepository;

    @Autowired
    private BankRepository bankRepository;

    @Autowired
    private EmploymentRepository employmentRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private OamApiService oamApiService;

    @Autowired
    private TimeLineApiService timeLineApiService;

    @Autowired
    private LoanScheduleApiService loanScheduleApiService;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private TransactionItemApiService transactionItemApiService;

    @Autowired
    private MetadataFeignClient metadataFeignClient;

    @Autowired
    private BusinessFeignClient businessFeignClient;

    public String getContractNoByCategoryAndStatus(Integer catetory, List<Integer> statusList, String operatorNo, String operatorName) {
        String result = "";
        Loan loan = loanRepository.findTopByCategoryEqualsAndStatusInOrderByCreateTimeDesc(catetory, statusList);

        if (loan != null) {
            result = loan.getContractNo();
        }

        return result;
    }

    public String getNewContractNoOfAgentReview(Integer category, List<Integer> statusList, String operatorNo, String operatorName) {
        String result = "";
        Loan loan = loanRepository.findTopByCategoryEqualsAndStatusInAndLockedOperatorNoIsNullOrderByCreateTimeDesc(category, statusList);

        if (loan != null) {
            result = loan.getContractNo();

            if (LoanStatusEnum.INITIALIZED.getValue().equals(loan.getStatus())) {
                Integer oldStatus = LoanStatusEnum.INITIALIZED.getValue();
                Integer newStatus = LoanStatusEnum.AGENT_REVIEW.getValue();
                loan.setStatus(newStatus);
                loan.setLockedOperatorNo(operatorNo);
                loan.setLockedOperatorName(operatorName);
                loan.setLockedAt(new Date());
                loanRepository.save(loan);

                JSONObject additionalObj = new JSONObject();
                additionalObj.put("operatorNo", operatorNo);
                additionalObj.put("operatorName", operatorName);
                //timeLineApiService.addLoanStatusChangeTimeline(loan.getContractNo(), oldStatus, newStatus, additionalObj.toJSONString());
            }
        }

        return result;
    }

    public JSONObject getContractInfoByContractNo(String contractNo) {
        JSONObject resultData = new JSONObject();
        Loan loan = loanRepository.findByContractNo(contractNo);
        if(loan != null){
            Payment payment = paymentRepository.findByLoanId(loan.getId());
            if(payment != null){
                List<LoanItem> contractItemObjList = JSON.parseArray(payment.getItems(), LoanItem.class);
                BigDecimal maturityTotalPrincipal = BigDecimalUtil.toBigDecimal(0.00);
                BigDecimal maturityFinanceFee = BigDecimalUtil.toBigDecimal(0.00);
                for (LoanItem loanItem : contractItemObjList) {
                    maturityTotalPrincipal = BigDecimalUtil.add(maturityTotalPrincipal, loanItem.getPrincipal());
                    maturityFinanceFee = BigDecimalUtil.add(maturityFinanceFee, loanItem.getInterest());
                }
                BigDecimal maturityTotalAmount = BigDecimalUtil.add(maturityTotalPrincipal, maturityFinanceFee);
                resultData.put(JsonKeyConst.MATURITY_FINANCE_FEE, maturityFinanceFee);
                resultData.put(JsonKeyConst.MATURITY_TOTAL_AMOUNT, maturityTotalAmount);
            }

            Integer customerId = loan.getCustomerId();
            Customer customerObj = customerRepository.findById(customerId);
            resultData.put(JsonKeyConst.FIRST_NAME, customerObj.getFirstName());
            resultData.put(JsonKeyConst.MIDDLE_NAME, customerObj.getMiddleName());
            resultData.put(JsonKeyConst.LAST_NAME, customerObj.getLastName());
            resultData.put(JsonKeyConst.BIRTHDAY, customerObj.getBirthday());
            resultData.put(JsonKeyConst.EMAIL, "");
            resultData.put(JsonKeyConst.HOMEPHONE, "");
            resultData.put(JsonKeyConst.TELEPHONE, "");
            resultData.put(JsonKeyConst.FAX, "");
        }

        return resultData;
    }

    public JSONObject getContractAndContractNoItemByContractNo(String contractNo) {
        JSONObject resultData = null;
        Loan loan = loanRepository.findByContractNo(contractNo);
        if(loan != null){
            resultData = JSON.parseObject(JSON.toJSONString(loan));

            Payment payment = paymentRepository.findByLoanId(loan.getId());
            if(payment != null) {
                List<LoanItem> contractItemObjList = JSON.parseArray(payment.getItems(), LoanItem.class);
                String itemsStr = JSON.toJSONString(contractItemObjList,SerializerFeature.WriteMapNullValue);
                JSONArray jsonArray = JSON.parseArray(itemsStr);
                resultData.put(JsonKeyConst.CONTRACT_ITEM_LIST, jsonArray);
            }

            Integer customerId = loan.getCustomerId();
            Customer customer = customerRepository.findById(customerId);
            if(customer != null){
                String customerStr = JSON.toJSONString(customer,SerializerFeature.WriteMapNullValue);
                resultData.put(JsonKeyConst.CUSTOMER, JSON.parseObject(customerStr));
            }
        }
        return resultData;
    }

    public String getContractOnlyByContractNo(String contractNo) {
        Loan contract = loanRepository.findByContractNo(contractNo);
        String result = "";
        if (contract != null) {
            result = JSON.toJSONString(contract);
        }
        return result;
    }

    public List<Customer> getCustomerData(String ssns) {

        JSONArray ssnsArray = JSON.parseArray(ssns);
        List<String> ssnList = new ArrayList<>();
        for (int i = 0; i < ssnsArray.size(); i++) {
            JSONObject ssnJson = ssnsArray.getJSONObject(i);
            ssnList.add(ssnJson.getString(JsonKeyConst.SSN));
        }
        return customerRepository.findAllBySsnIn(ssnList);
    }

    public List<Loan> getAllLoanContracts() {
        return loanRepository.findAll();
    }

    public List<Loan> getLoanContractsByStatus(int loanStatus) {

        List<Loan> loans = null;
        LoanStatusEnum loanStatusEnum = EnumUtil.getByValue(LoanStatusEnum.class, loanStatus);
        logger.info("loanStatus:{}, the Enum Object is:{}", loanStatus, loanStatusEnum);
        if (loanStatusEnum != null) {
            loans = loanRepository.findAllByStatus(loanStatus);
            if (loans != null) {
                for (Loan loan : loans) {
                    fillPropertyForLoan(loan);
                }
            }
        }

        return loans;
    }

    public List<Loan> getLoanContractsByStatusAndTime(int loanStatus, Date startTime) {

        List<Loan> loans = null;
        LoanStatusEnum loanStatusEnum = EnumUtil.getByValue(LoanStatusEnum.class, loanStatus);
        logger.info("loanStatus:{}, the Enum Object is:{}", loanStatus, loanStatusEnum);
        if (loanStatusEnum != null) {
            loans = loanRepository.findAllByStatusAndCreateTimeAfter(loanStatus, startTime);
            if (loans != null) {
                for (Loan loan : loans) {
                    fillPropertyForLoan(loan);
                }
            }
        }

        return loans;
    }

    private Loan fillPropertyForLoan(Loan loan){
        Integer loanId = loan.getId();
        Personal personal = personalRepository.findByLoanId(loanId);
        Bank bank = bankRepository.findByLoanId(loanId);
        Employment employment = employmentRepository.findByLoanId(loanId);
        Payment payment = paymentRepository.findByLoanId(loanId);
        Document document = documentRepository.findByLoanId(loanId);

        loan.setPersonal(personal);
        loan.setBank(bank);
        loan.setEmployment(employment);
        loan.setPayment(payment);
        loan.setDocument(document);

        return loan;
    }

    public List<LoanOverView> getLoanContractsOverView(int loanStatus, Date startTime) {
        List<LoanOverView> loanOverViewList = new ArrayList<>();

        List<Loan> loans = getLoanContractsByStatusAndTime(loanStatus, startTime);
        if (loans != null) {
            for (Loan loan : loans) {
                LoanOverView loanOverView = new LoanOverView();
                loanOverView.setLoanId(loan.getId());
                loanOverView.setContractNo(loan.getContractNo());
                loanOverView.setCreateTime(loan.getCreateTime());
                loanOverView.setUpdateTime(loan.getUpdateTime());
                loanOverView.setLockedOperatorName(loan.getLockedOperatorName());
                loanOverView.setLockedOperatorNo(loan.getLockedOperatorNo());
                loanOverView.setLockTime(loan.getLockedAt());

                Personal personal = personalRepository.findByLoanId(loan.getId());
                if(personal != null){
                    loanOverView.setFirstName(personal.getFirstName());
                    loanOverView.setLastName(personal.getLastName());
                    loanOverView.setRequestPrincipal(personal.getRequestPrincipal());
                    loanOverView.setState(personal.getState());
                }
                loanOverViewList.add(loanOverView);
            }
        }

        return loanOverViewList;
    }

    /**
     * @param loanId
     * @return
     */
    public Loan getLoanByLoanId(int loanId) {
        Loan loan = loanRepository.findById(loanId);
        if (loan != null) {
            fillPropertyForLoan(loan);
        }
        return loan;
    }

    /**
     * @param contractNo
     * @return
     */
    public Loan getLoanByContactNo(String contractNo) {
        Loan loan = loanRepository.findByContractNo(contractNo);
        if (loan != null) {
            fillPropertyForLoan(loan);
        }
        return loan;
    }


    public JSONObject getFormedLoanDataById(Integer loanId){
        JSONObject jsonObject = new JSONObject();
        Loan loan = loanRepository.findById(loanId);
        String jsonStr = "";
        if (loan != null) {
            jsonStr = JSON.toJSONString(fillPropertyForLoan(loan));
            jsonObject = JSON.parseObject(jsonStr);
        }
        return jsonObject;
    }

    /**
     * @param contractNo
     * @return
     */
    public JSONObject getFormedContractDataByContactNo(String contractNo) {

        Loan loan = loanRepository.findByContractNo(contractNo);
        if (loan == null) {
            return new JSONObject();
        }

        int loanId = loan.getId();
        int portfolioId = loan.getPortfolioId();
        String jsonStr = JSON.toJSONString(loan);
        JSONObject jsonObject = JSONObject.parseObject(jsonStr);

        // set loan items
        Payment payment = paymentRepository.findByLoanId(loanId);
        if(payment != null){
            String itemStr = payment.getItems();
            if(StringUtils.isNotEmpty(itemStr)){
                List<LoanItem> items = JSONArray.parseArray(itemStr, LoanItem.class);
                if (items != null) {
                    JSONArray jsonItemArray = new JSONArray();
                    for (int i = 1; i < items.size(); ++i) {
                        LoanItem item = items.get(i);
                        Date date = new Date(item.getInstallmentDate());
                        String dateStr = DateUtil.date2str(date);
                        String amountStr = StringUtil.toCurrency(item.getAmount());
                        //JSONObject jsonItem = (JSONObject) JSON.toJSON(item);
                        JSONObject jsonItem = new JSONObject();
                        jsonItem.put("installmentDate", dateStr);
                        jsonItem.put("amount", amountStr);
                        jsonItemArray.add(jsonItem);
                    }
                    jsonObject.put("items", jsonItemArray);
                }
            }

            jsonObject.put("paymentPlanPrincipal", StringUtil.toCurrency(payment.getTotalPrincipal()));
            jsonObject.put("summaryTotalFinanceFee", StringUtil.toCurrency(payment.getTotalInterest()));
            jsonObject.put("summaryTotalPaymentAmount", StringUtil.toCurrency(payment.getTotalAmount()));
            jsonObject.put("summaryRegularInstallment", StringUtil.toCurrency(payment.getRegularAmount()));
            jsonObject.put("summaryAPR", payment.getAnnualPercentageRate() + "%");

            DecimalFormat df = new DecimalFormat("#.00");
            double interestRate = payment.getInterestRate() * 100;
            String newRateVal = df.format(interestRate) + "%";
            jsonObject.put("paymentInterestRate", newRateVal);
        }

        Bank bank = bankRepository.findByLoanId(loanId);
        if(bank != null){
            jsonObject.put("bankAvailableBalance", StringUtil.toCurrency(bank.getBankAvailableBalance()));
        }

        Document document = documentRepository.findByLoanId(loanId);
        if(document != null){
            String keyDocumentSignatureTime = "documentSignatureTime";
            if (StringUtils.isEmpty(jsonObject.getString(keyDocumentSignatureTime))) {
                jsonObject.put(keyDocumentSignatureTime, DateUtil.date2str(new Date()));
            }else {
                if(document.getDocumentSignatureTime() != 0L){
                    Date date = new Date(document.getDocumentSignatureTime());
                    jsonObject.put(keyDocumentSignatureTime, DateUtil.date2str(date));
                }
            }
        }

        String portfolioStr = metadataFeignClient.getPortfolioById(portfolioId);
        logger.info("get portfolio information: " + portfolioStr);
        if (StringUtils.isNotEmpty(portfolioStr)) {
            Portfolio portfolio = JSONObject.parseObject(portfolioStr, Portfolio.class);

            String portfolioName = portfolio.getPortfolioName();
            if (StringUtils.isNotEmpty(portfolioName)) {
                jsonObject.put("portfolioName", portfolioName);
            }

            String portfolioDisplayName = portfolio.getDisplayName();
            if (StringUtils.isNotEmpty(portfolioDisplayName)) {
                jsonObject.put("portfolioDisplayName", portfolioDisplayName);
            }

            String portfolioAddress = portfolio.getAddress();
            if (StringUtils.isNotEmpty(portfolioAddress)) {
                jsonObject.put("portfolioAddress", portfolioAddress);
            }

            String portfolioCity = portfolio.getCity();
            if (StringUtils.isNotEmpty(portfolioCity)) {
                jsonObject.put("portfolioCity", portfolioCity);
            }

            String portfolioState = portfolio.getState();
            if (StringUtils.isNotEmpty(portfolioState)) {
                jsonObject.put("portfolioState", portfolioState);
            }

            String portfolioZip = portfolio.getZip();
            if (StringUtils.isNotEmpty(portfolioZip)) {
                jsonObject.put("portfolioZip", portfolioZip);
            }

            jsonObject.put("portfolioLateFee", StringUtil.toCurrency(portfolio.getLateFee()));

            String tribeName = portfolio.getTribeName();
            if (StringUtils.isNotEmpty(tribeName)) {
                jsonObject.put("tribeName", tribeName);
            }

            String portfolioEmail = portfolio.getEmail();
            if (StringUtils.isNotEmpty(portfolioEmail)) {
                jsonObject.put("portfolioEmail", portfolioEmail);
            }

            String portfolioTelephone = portfolio.getTelephone();
            if (StringUtils.isNotEmpty(portfolioTelephone)) {
                jsonObject.put("portfolioTelephone", portfolioTelephone);
            }

            jsonObject.put("portfolioNsfFee", StringUtil.toCurrency(portfolio.getNsfFee()));

            String portfolioFax = portfolio.getFax();
            if (StringUtils.isNotEmpty(portfolioFax)) {
                jsonObject.put("portfolioFax", portfolioFax);
            }

            String portfolioWebsite = portfolio.getWebsite();
            if (StringUtils.isNotEmpty(portfolioWebsite)) {
                jsonObject.put("portfolioWebsite", portfolioWebsite);
            }

        }

        return jsonObject;
    }


    public void saveLoanContractFromLeads(String leads) {

        Loan loan = JSON.parseObject(leads, Loan.class);

        JSONObject options = oamApiService.getOptions(loan.getPortfolioId());
        //String contractNo = getContractNo(options);
        int maxLength = 7;
        String portfolioCode = "IC";
        String year = DateUtil.date2str(new Date(), "yy");
        String flowNumber = businessFeignClient.getSeed(maxLength, portfolioCode, year);
        String contractNo = portfolioCode + year + flowNumber;
        logger.info("Create loan No:{}", contractNo);
        if (StringUtils.isNotEmpty(contractNo)) {
            logger.info("save Loan Contract:" + loan.toString());
            loan.setContractNo(contractNo);
            loan.setLeadId(loan.getId());
            loan.setId(null);
            loan.setStatus(LoanStatusEnum.INITIALIZED.getValue());
            Integer loanId = saveContractOnly(loan);
            loan.setId(loanId);

            Personal personal = loan.getPersonal();
            if(personal != null){
                personal.setLoanId(loanId);
                personalRepository.save(personal);
            }

            Bank bank = loan.getBank();
            if(bank != null){
                bank.setLoanId(loanId);
                bankRepository.save(bank);
            }

            Employment employment = loan.getEmployment();
            if(employment != null){
                employment.setLoanId(loanId);
                employmentRepository.save(employment);
            }

            //timeLineApiService.addLoanStatusChangeTimeline(contractNo, null, LoanStatusEnum.INITIALIZED.getValue(), "");
        } else {
            logger.error("Can't create a contractNo");
        }
    }

    public Integer saveContractOnly(Loan loan) {
        return loanRepository.save(loan).getId();
    }

    public String findByLeadId(Integer leadId) {

        JSONObject loanInfo = new JSONObject();

        Loan loan = loanRepository.findByLeadId(leadId);

        if (loan != null) {

            String scheduleItemsInfo = loanScheduleApiService.getScheduleItemsInfo(loan.getContractNo());
            String loanStatus = timeLineApiService.getLoanStatus(loan.getContractNo());
            String transactionItems = transactionItemApiService.getTransactionItemBycontractNo(loan.getContractNo());


            if (!("").equals(scheduleItemsInfo) && !("").equals(transactionItems) && !("").equals(loanStatus)) {
                loanInfo = JSONObject.parseObject(scheduleItemsInfo);

                loanInfo.put("loan", JSON.toJSON(loan));
                loanInfo.put("loanStatus", JSONObject.parse(loanStatus));
                loanInfo.put("loanTransactionItem", JSONArray
                        .parse(transactionItems));
            } else {
                logger.error("please check the exited loan Info!");
            }

        }

        logger.info(loanInfo.toString());
        return loanInfo.toString();
    }

    public String countContractByLeadIdAndStatus(String leadIdsStr, Integer status) {

        List<Integer> leadIds = (List<Integer>) JSONArray.parse(leadIdsStr);
        JSONObject count = new JSONObject();
        Loan contract = new Loan();
        int i = 0;
//        for (Integer leadId : leadIds) {
//            loan = loanContractRepository.findByLeadId(leadId);
//            if (loan != null && loan.getStatus() == status) {
//                i++;
//            }
//        }
            List<Loan> loans = loanRepository.getLoanByStatusAndLeadId(status, leadIds);
            if (loans != null) {
                i = loans.size();
            }
        count.put("count", i);
        return JSONObject.toJSONString(count);
    }

    public String searchLeadsInfo(String name, String ssn, String applicationNumber,
                                  String email, String phoneNumber, String accountNumber) {
        JSONArray result = new JSONArray();

        HashSet<Integer> resultIds = new HashSet<>();
        if (StringUtils.isNotEmpty(name)) {
            resultIds.addAll(personalRepository.findAllLoanIdsByFullName(name));
        }

        if (StringUtils.isNotEmpty(ssn)) {
            HashSet<Integer> ssnResults = new HashSet<>(personalRepository.findAllLoanIdsBySsn(ssn));

            if (resultIds.size() == 0) {
                resultIds = ssnResults;
            } else {
                resultIds.retainAll(ssnResults);
            }
        }

        if (StringUtils.isNotEmpty(applicationNumber)) {

            Loan loan = loanRepository.findByContractNo(applicationNumber);
            if (loan != null) {
                HashSet<Integer> appNoResultes = new HashSet<>();
                appNoResultes.add(loan.getId());

                if (resultIds.size() == 0) {
                    resultIds = appNoResultes;
                } else {
                    resultIds.retainAll(appNoResultes);
                }
            }
        }

        if (StringUtils.isNotEmpty(email)) {
            HashSet<Integer> emailResults = new HashSet<>(personalRepository.findAllLoanIdsByEmail(email));
            if (resultIds.size() == 0) {
                resultIds = emailResults;
            } else {
                resultIds.retainAll(emailResults);
            }
        }

        if (StringUtils.isNotEmpty(phoneNumber)) {
            HashSet<Integer> hPhoneResults = new HashSet<>(personalRepository.findAllLoanIdsByHomePhone(phoneNumber));
            hPhoneResults.addAll(personalRepository.findAllLoanIdsByMobilePhone(phoneNumber));

            if (resultIds.size() == 0) {
                resultIds = hPhoneResults;
            } else {
                resultIds.retainAll(hPhoneResults);
            }
        }

        if (StringUtils.isNotEmpty(accountNumber)) {
            HashSet<Integer> accResults = new HashSet<>(bankRepository.findAllLoanIdsByAccountNumber(accountNumber));
            if (resultIds.size() == 0) {
                resultIds = accResults;
            } else {
                resultIds.retainAll(accResults);
            }
        }

        logger.info("search Result LoanIdList" + JSON.toJSONString(resultIds));

        if (resultIds.size() != 0) {
            JSONObject contractInfo;
            String contractNum;
            Integer loanStatus = null;

            for (Integer id : resultIds) {
                contractInfo = getFormedLoanDataById(id);

                if (contractInfo != null) {
                    contractNum = contractInfo.getString("contractNo");
                    Util.report(timeLineApiService.getLoanStatus(contractNum));
                    loanStatus = Integer.parseInt(JSONObject.parseObject(timeLineApiService.getLoanStatus(contractNum)).getString("status"));
                    contractInfo.put("status", loanStatus);
                }
                result.add(contractInfo);
            }
        }

        return JSONArray.toJSONString(result);
    }

    public String getTodoListLoanInfo(String operatorNo, Integer eventType) {

        HashSet<String> contractNos = timeLineApiService.getTodoContractNo(operatorNo, eventType);
        JSONArray toDoList = new JSONArray();

        if (contractNos != null) {
            for (String contractNo : contractNos) {
                JSONObject contractInfo = getFormedContractDataByContactNo(contractNo);
                if (contractInfo != null) {
                    contractInfo.put("eventType", eventType);
                    toDoList.add(contractInfo);
                }
            }
        }

        return JSONArray.toJSONString(toDoList);
    }

    public String countWithDrawnLoan(String startTime, String endTime) {

        JSONArray withdrawnLoans = new JSONArray();
        HashSet<Integer> reasonsPositive = new HashSet<Integer>();
        HashSet<Integer> reasonsNegative = new HashSet<Integer>();
        WithdrawnResonConverter withdrawnResonConverter = new WithdrawnResonConverter();

        Date start = new Date(Long.parseLong(startTime));
        Date end = new Date(Long.parseLong(endTime));
        List<Loan> withdrawnLoancs = loanRepository.countWithDrawnloans(start, end);


        for (Loan contract : withdrawnLoancs) {
            if (((contract.getStatus()) & (LoanStatusEnum.POSITIVE.getValue())) == LoanStatusEnum.POSITIVE.getValue()) {
                reasonsPositive.add(contract.getWithdrawnCode());
            } else if (((contract.getStatus()) & (LoanStatusEnum.NEGATIVE.getValue())) == LoanStatusEnum.NEGATIVE.getValue()) {
                reasonsNegative.add(contract.getWithdrawnCode());
            }
        }


        for (Integer reasonCode : reasonsPositive) {
            JSONObject countDataP = new JSONObject();
            Integer i = 0;

            countDataP.put("type", "POSITIVE");
            countDataP.put("withdrawnReason", withdrawnResonConverter.convertToEntityAttribute(reasonCode).getText());
            for (Loan contractWithdrawn : withdrawnLoancs) {
                if (contractWithdrawn.getWithdrawnCode().equals(reasonCode)) {
                    i++;
                }
            }
            countDataP.put("purchasedWithdrawnNum", i);
            countDataP.put("reappliedWithdrawnNum", 0);
            countDataP.put("totalWithdrawn", i);
            withdrawnLoans.add(countDataP);
        }


        for (Integer reasonCodeN : reasonsNegative) {
            JSONObject countDataN = new JSONObject();
            Integer i = 0;

            countDataN.put("type", "NEGATIVE");
            countDataN.put("withdrawnReason", withdrawnResonConverter.convertToEntityAttribute(reasonCodeN).getText());
            for (Loan contractWithdrawn : withdrawnLoancs) {
                if (contractWithdrawn.getWithdrawnCode().equals(reasonCodeN)) {
                    i++;
                }
            }
            countDataN.put("purchasedWithdrawnNum", i);
            countDataN.put("reappliedWithdrawnNum", 0);
            countDataN.put("totalWithdrawn", i);
            withdrawnLoans.add(countDataN);
        }

//        return JSONArray.toJSONString(withdrawnLoancs);

        Util.report(JSONArray.toJSONString(withdrawnLoans));

        return JSONArray.toJSONString(withdrawnLoans);

    }


    public String countPendingSummary(String ids) {


        JSONArray summaryArr = new JSONArray();
//        Date startDate = new Date(Long.parseLong(start));
//        Date endDate = new Date(Long.parseLong(end));

//        List<Loan> initialiedLoans=loanContractRepository.getLoanByStatusAndTime(LoanStatusEnum.INITIALIZED.getValue(), startDate, endDate);
//        List<Loan> agentLoans=loanContractRepository.getLoanByStatusAndTime(LoanStatusEnum.AGENT_REVIEW.getValue(), startDate, endDate);
//        List<Loan> underwriterLoans=loanContractRepository.getLoanByStatusAndTime(LoanStatusEnum.UNDERWRITER_REVIEW.getValue(), startDate, endDate);
//        List<Loan> tribeLoans=loanContractRepository.getLoanByStatusAndTime(LoanStatusEnum.TRIBE_REVIEW.getValue(), startDate, endDate);
//        List<Loan> correctionLoans=loanContractRepository.getLoanByStatusAndTime(LoanStatusEnum.CORRECTION.getValue(), startDate, endDate);
//        List<Loan> approvedLoans=loanContractRepository.getLoanByStatusAndTime(LoanStatusEnum.APPROVED.getValue(), startDate, endDate);


        JSONObject leadIds = JSONObject.parseObject(ids);

        JSONArray newcustIdArr = JSONArray.parseArray(leadIds.getString("newcustids"));
        JSONArray returnIdArr = leadIds.getJSONArray("returnIds");
        JSONArray purcharsedIddArr = leadIds.getJSONArray("purchasedIds");

        Util.report(JSONArray.toJSONString(newcustIdArr));
        Util.report(JSONArray.toJSONString(returnIdArr));
        Util.report(JSONArray.toJSONString(newcustIdArr));

        JSONObject initSummary = countResult(purcharsedIddArr, returnIdArr, newcustIdArr, LoanStatusEnum.INITIALIZED.getValue());
        JSONObject agentSummary = countResult(purcharsedIddArr, returnIdArr, newcustIdArr, LoanStatusEnum.AGENT_REVIEW.getValue());
        JSONObject underwriteSummary = countResult(purcharsedIddArr, returnIdArr, newcustIdArr, LoanStatusEnum.UNDERWRITER_REVIEW.getValue());
        JSONObject tribeSummary = countResult(purcharsedIddArr, returnIdArr, newcustIdArr, LoanStatusEnum.TRIBE_REVIEW.getValue());
        JSONObject correctionSummary = countResult(purcharsedIddArr, returnIdArr, newcustIdArr, LoanStatusEnum.CORRECTION.getValue());
        JSONObject approvedSummary = countResult(purcharsedIddArr, returnIdArr, newcustIdArr, LoanStatusEnum.APPROVED.getValue());
        summaryArr.add(initSummary);
        summaryArr.add(agentSummary);
        summaryArr.add(underwriteSummary);
        summaryArr.add(tribeSummary);
        summaryArr.add(correctionSummary);
        summaryArr.add(approvedSummary);

        return JSONArray.toJSONString(summaryArr);
    }

    public JSONObject countResult(JSONArray purcharsedIddArr, JSONArray returnIdArr, JSONArray newcustIdArr, Integer status) {

        LoanStatusConverter loanStatusConverter = new LoanStatusConverter();
        JSONObject result = new JSONObject();
        Integer reapplied = 0;
        Integer purchased = countPendingByItems(purcharsedIddArr, status);
        Integer newcust = countPendingByItems(newcustIdArr, status);
        Integer returncust = countPendingByItems(returnIdArr, status);

        result.put("loanStatus", loanStatusConverter.convertToEntityAttribute(status));
        result.put("purchased", purchased);
        result.put("reapplied", reapplied);
        result.put("newcust", newcust);
        result.put("returnCust", returncust);
        result.put("total", purchased + reapplied + newcust + returncust);
        return result;
    }

    public Integer countPendingByItems(JSONArray arr, Integer status) {
        Integer resultCount = 0;

        List<Integer> leads = new ArrayList<>();


        if (arr.size() != 0) {
            for (int i = 0; i < arr.size(); i++) {
                leads.add((int) arr.get(i));
            }
            List<Loan> loans = loanRepository.getLoanByStatusAndLeadId(status, leads);

            if (loans != null) {
                resultCount = loans.size();
            }
        }
        return resultCount;
    }


}
