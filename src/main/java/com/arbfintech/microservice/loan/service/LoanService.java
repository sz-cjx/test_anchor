package com.arbfintech.microservice.loan.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.arbfintech.framework.component.core.constant.JsonKeyConst;
import com.arbfintech.framework.component.core.enumerate.*;
import com.arbfintech.framework.component.core.type.RabbitMessage;
import com.arbfintech.framework.component.core.util.BigDecimalUtil;
import com.arbfintech.framework.component.core.util.DateUtil;
import com.arbfintech.framework.component.core.util.EnumUtil;
import com.arbfintech.framework.component.core.util.StringUtil;
import com.arbfintech.microservice.loan.client.BusinessFeignClient;
import com.arbfintech.microservice.loan.client.EmployeeFeignClient;
import com.arbfintech.microservice.loan.client.GrabLoanFeignClient;
import com.arbfintech.microservice.loan.client.RuntimeFeignClient;
import com.arbfintech.microservice.loan.entity.*;
import com.arbfintech.microservice.loan.repository.*;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;

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
    private TimeLineApiService timeLineApiService;

    @Autowired
    private LoanScheduleApiService loanScheduleApiService;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private TransactionItemApiService transactionItemApiService;

    @Autowired
    private BusinessFeignClient businessFeignClient;

    @Autowired
    private RuntimeFeignClient runtimeFeignClient;

    @Autowired
    private EmployeeFeignClient employeeFeignClient;

    @Autowired
    private GrabLoanFeignClient grabLoanFeignClient;


    public String getContractNoByCategoryAndStatus(Integer catetory, List<Integer> statusList, String operatorNo, String operatorName) {
        String result = "";
        Loan loan = loanRepository.findTopByCategoryEqualsAndLoanStatusInOrderByCreateTimeDesc(catetory, statusList);

        if (loan != null) {
            result = loan.getContractNo();
        }

        return result;
    }

    public String getNewContractNoOfAgentReview(Integer category, List<Integer> statusList, String operatorNo, String operatorName) {
        String result = "";
        Loan loan = loanRepository.findTopByCategoryEqualsAndLoanStatusInAndLockedOperatorNoIsNullOrderByCreateTimeDesc(category, statusList);
        //Loan loan = loanRepository.findTopByCategoryEqualsAndLoanStatusInOrderByCreateTimeDesc(category, statusList);

        if (loan != null) {
            result = loan.getContractNo();

            if (LoanStatusEnum.INITIALIZED.getValue().equals(loan.getLoanStatus())) {
                Integer oldStatus = LoanStatusEnum.INITIALIZED.getValue();
                Integer newStatus = LoanStatusEnum.AGENT_REVIEW.getValue();
                loan.setLoanStatus(newStatus);
                loan.setLoanStatusText(LoanStatusEnum.AGENT_REVIEW.getText());
                loan.setLockedOperatorNo(operatorNo);
                loan.setLockedOperatorName(operatorName);
                loan.setLockedAt(DateUtil.getCurrentTimestamp());
                loan.setUpdateTime(DateUtil.getCurrentTimestamp());
                loanRepository.save(loan);

                JSONObject additionalObj = new JSONObject();
                additionalObj.put("operatorNo", operatorNo);
                additionalObj.put("operatorName", operatorName);
                additionalObj.put("contractNo", loan.getContractNo());
                additionalObj.put("appData",JSON.toJSONString(loan));
                timeLineApiService.addLoanStatusChangeTimeline(oldStatus, newStatus, additionalObj.toJSONString());
            }
        }

        return result;
    }

    public String saveLoanProperty(Integer loanId, String section, String properties, String additionalData) {
        String oldStr;
        String newStr;
        logger.info("start to save (section:{}) property:{} ", section, properties);

        if (StringUtils.isEmpty(section) || StringUtils.isEmpty(properties)) {
            logger.error("Empty parameter for (section:{}) property:{}", section, properties);
            return "";
        }

        String[] sectionArray = section.split(",");
        for (String sectionNumber : sectionArray) {
            switch (sectionNumber) {
                case "1":
                    Personal personal = personalRepository.findByLoanId(loanId);
                    oldStr = (personal == null ? "" : JSON.toJSONString(personal));
                    newStr = updatePropertyInJSON(oldStr, properties);
                    Personal newPersonal = JSON.parseObject(newStr, Personal.class);
                    newPersonal.setLoanId(loanId);

                    String firstName = newPersonal.getFirstName();
                    String middleName = newPersonal.getMiddleName();
                    String lastName = newPersonal.getLastName();
                    String fullName = (StringUtils.isEmpty(firstName) ? "" : firstName) +
                            (StringUtils.isEmpty(middleName) ? "" : " " + middleName) +
                            (StringUtils.isEmpty(lastName) ? "" : " " + lastName);
                    newPersonal.setFullName(fullName);
                    logger.info("Save personal:" + JSON.toJSONString(newPersonal));
                    personalRepository.save(newPersonal);
                    break;
                case "2":
                    Bank bank = bankRepository.findByLoanId(loanId);
                    oldStr = (bank == null ? "" : JSON.toJSONString(bank));
                    newStr = updatePropertyInJSON(oldStr, properties);
                    Bank newBank = JSON.parseObject(newStr, Bank.class);
                    newBank.setLoanId(loanId);

                    Integer bankAccountType = newBank.getBankAccountType();
                    if(bankAccountType != null){
                        newBank.setBankAccountTypeText(EnumUtil.getByValue(BankAccountTypeEnum.class, bankAccountType).getText());
                    }

                    logger.info("Save bank:" + JSON.toJSONString(newBank));
                    bankRepository.save(newBank);
                    break;
                case "3":
                    Employment employment = employmentRepository.findByLoanId(loanId);
                    oldStr = (employment == null ? "" : JSON.toJSONString(employment));
                    newStr = updatePropertyInJSON(oldStr, properties);
                    Employment newEmployment = JSON.parseObject(newStr, Employment.class);
                    newEmployment.setLoanId(loanId);

                    Integer payrollType = newEmployment.getPayrollType();
                    if(payrollType != null){
                        newEmployment.setPayrollTypeText(EnumUtil.getByValue(PayrollTypeEnum.class, payrollType).getText());
                    }

                    logger.info("Save employ:" + JSON.toJSONString(newEmployment));
                    employmentRepository.save(newEmployment);
                    break;
                case "4":
                    Payment payment = paymentRepository.findByLoanId(loanId);
                    oldStr = (payment == null ? "" : JSON.toJSONString(payment));
                    newStr = updatePropertyInJSON(oldStr, properties);
                    Payment newPayment = JSON.parseObject(newStr, Payment.class);
                    newPayment.setLoanId(loanId);
                    logger.info("Save payment:" + JSON.toJSONString(newPayment));
                    paymentRepository.save(newPayment);
                    break;
                case "5":
                    Document document = documentRepository.findByLoanId(loanId);
                    oldStr = (document == null ? "" : JSON.toJSONString(document));
                    newStr = updatePropertyInJSON(oldStr, properties);
                    Document newDocument = JSON.parseObject(newStr, Document.class);
                    newDocument.setLoanId(loanId);
                    logger.info("Save document:" + JSON.toJSONString(newDocument));
                    documentRepository.save(newDocument);
                    break;
                default:
                    logger.error("invalid section number:{}", sectionNumber);
                    break;
            }
        }

        if(StringUtils.isNotEmpty(additionalData)){
            JSONObject jsonObject = JSON.parseObject(additionalData);
            Loan loan = getSimpleLoanByLoanId(loanId);
            jsonObject.put("appData",JSON.toJSONString(loan));
            timeLineApiService.addSaveTimeline(properties, jsonObject.toJSONString());
        }

        return "";
    }

    private String updatePropertyInJSON(String jsonStr, String properties) {
        JSONObject jsonObject;
        if (StringUtils.isNotEmpty(jsonStr)) {
            jsonObject = JSON.parseObject(jsonStr);
        } else {
            jsonObject = new JSONObject();
        }

        JSONArray jsonArray = JSONArray.parseArray(properties);
        for (Object o : jsonArray) {
            JSONObject jo = (JSONObject) o;
            String key = jo.getString("fieldKey");
            String newValue = jo.getString("fieldValue");
            jsonObject.put(key, newValue);
        }
        return JSON.toJSONString(jsonObject);
    }

    public JSONObject getContractInfoByContractNo(String contractNo) {
        JSONObject resultData = new JSONObject();
        Loan loan = loanRepository.findByContractNo(contractNo);
        if (loan != null) {
            Payment payment = paymentRepository.findByLoanId(loan.getId());
            if (payment != null) {
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
            resultData.put(JsonKeyConst.HOME_PHONE, "");
            resultData.put(JsonKeyConst.TELEPHONE, "");
            resultData.put(JsonKeyConst.FAX, "");
        }

        return resultData;
    }

    public JSONObject getContractAndContractNoItemByContractNo(String contractNo) {
        JSONObject resultData = null;
        Loan loan = loanRepository.findByContractNo(contractNo);
        if (loan != null) {
            resultData = JSON.parseObject(JSON.toJSONString(loan));

            Payment payment = paymentRepository.findByLoanId(loan.getId());
            if (payment != null) {
                List<LoanItem> contractItemObjList = JSON.parseArray(payment.getItems(), LoanItem.class);
                String itemsStr = JSON.toJSONString(contractItemObjList, SerializerFeature.WriteMapNullValue);
                JSONArray jsonArray = JSON.parseArray(itemsStr);
                resultData.put(JsonKeyConst.CONTRACT_ITEM_LIST, jsonArray);
            }

            Integer customerId = loan.getCustomerId();
            Customer customer = customerRepository.findById(customerId);
            if (customer != null) {
                String customerStr = JSON.toJSONString(customer, SerializerFeature.WriteMapNullValue);
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
            loans = loanRepository.findAllByLoanStatus(loanStatus);
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
            loans = loanRepository.findAllByLoanStatusAndCreateTimeAfter(loanStatus, startTime.getTime());
            if (loans != null) {
                for (Loan loan : loans) {
                    fillPropertyForLoan(loan);
                }
            }
        }

        return loans;
    }

    private Loan fillPropertyForLoan(Loan loan) {
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
                if (personal != null) {
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

    public Loan getSimpleLoanByLoanId(int loanId) {
        Loan loan = loanRepository.findById(loanId);
        if (loan != null) {
            fillPropertyForLoan(loan);

            Payment payment = loan.getPayment();
            if(payment!=null) {
                payment.setItems("");
                loan.setPayment(payment);
            }
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


    public JSONObject getFormedLoanDataById(Integer loanId) {
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

        //set personal info
        Personal personal = personalRepository.findByLoanId(loanId);
        if (personal != null) {
            fillContractAttr(jsonObject, "firstName", personal.getFirstName());
            fillContractAttr(jsonObject, "lastName", personal.getLastName());
            fillContractAttr(jsonObject, "fullName", personal.getFullName());
            fillContractAttr(jsonObject, "address", personal.getAddress());
            fillContractAttr(jsonObject, "city", personal.getCity());
            fillContractAttr(jsonObject, "state", personal.getState());
            fillContractAttr(jsonObject, "zipCode", personal.getZip());
            fillContractAttr(jsonObject, "homePhone", personal.getHomePhone());
            fillContractAttr(jsonObject, "email", personal.getEmail());
            fillContractAttr(jsonObject, "otherPhone", personal.getMobilePhone());
        }

        Bank bank = bankRepository.findByLoanId(loanId);
        if (bank != null) {
            jsonObject.put("bankAvailableBalance", StringUtil.toCurrency(bank.getBankAvailableBalance()));
            fillContractAttr(jsonObject, "bankName", bank.getBankName());
            fillContractAttr(jsonObject, "bankRoutingNumber", bank.getBankRoutingNo());
            fillContractAttr(jsonObject, "bankAccount", bank.getBankAccountNo());
        }

        Document document = documentRepository.findByLoanId(loanId);
        String keyDocumentSignatureTime = "documentSignatureTime";
        if (document != null && document.getDocumentSignatureTime() != null) {
            Date date = new Date(document.getDocumentSignatureTime());
            jsonObject.put(keyDocumentSignatureTime, DateUtil.date2str(date));
        } else {
            jsonObject.put(keyDocumentSignatureTime, DateUtil.date2str(new Date()));
        }


        // set loan items
        Payment payment = paymentRepository.findByLoanId(loanId);
        if (payment != null) {
            String itemStr = payment.getItems();
            if (StringUtils.isNotEmpty(itemStr)) {
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

            String effectiveDateStr = payment.getEffectiveDate();
            fillContractAttr(jsonObject, "paymentEffectiveDate", effectiveDateStr);

            if (StringUtils.isNotEmpty(effectiveDateStr)) {
                logger.debug(effectiveDateStr);
                long effectiveDateTimeStamp = DateUtil.str2date(effectiveDateStr).getTime();
                effectiveDateTimeStamp = effectiveDateTimeStamp - (1000 * 60 * 60 * 24);
                Date expireDate = DateUtil.long2date(effectiveDateTimeStamp);
                String expiredDateStr = DateUtil.date2str(expireDate);
                logger.debug(expiredDateStr);
                String expiredDateTimeStr = expiredDateStr + " 22:30:00";
                logger.debug(expiredDateTimeStr);
                Date expiredDate = DateUtil.str2datetime(expiredDateTimeStr);
                logger.debug(expiredDate.toString());
                Long expiredDateTimeStamp = expiredDate.getTime();
                logger.debug(expiredDateTimeStamp + "");
                logger.debug(DateUtil.long2date(expiredDateTimeStamp).toString());
                jsonObject.put("expiredTime", expiredDateTimeStamp);
            }

            DecimalFormat df = new DecimalFormat("#.00");
            double interestRate = payment.getInterestRate() * 100;
            String newRateVal = df.format(interestRate) + "%";
            jsonObject.put("paymentInterestRate", newRateVal);

            double apr = payment.getAnnualPercentageRate() * 100;
            String newAPR = df.format(apr) + "%";
            jsonObject.put("summaryAPR", newAPR);

            jsonObject.put("paymentPlanPrincipal", StringUtil.toCurrency(payment.getTotalPrincipal()));
            jsonObject.put("summaryTotalFinanceFee", StringUtil.toCurrency(payment.getTotalInterest()));
            jsonObject.put("summaryTotalPaymentAmount", StringUtil.toCurrency(payment.getTotalAmount()));
            jsonObject.put("summaryRegularInstallment", StringUtil.toCurrency(payment.getRegularAmount()));
        }

        String portfolioStr = runtimeFeignClient.getPortfolioParameter(portfolioId);
        logger.info("get portfolio information: " + portfolioStr);
        Portfolio portfolio;
        if (StringUtils.isNotEmpty(portfolioStr)) {
            portfolio = JSONObject.parseObject(portfolioStr, Portfolio.class);
        } else {
            portfolio = new Portfolio();
        }
        // this data is only for test
//        Portfolio portfolio = new Portfolio();
//        portfolio.setPortfolioName("inbox credit");
//        portfolio.setDisplayName("Inbox Credit");
//        portfolio.setAddress("Inbox Address");
//        portfolio.setCity("Norward");
//        portfolio.setState("AO");
//        portfolio.setZip("0303033");
//        portfolio.setLateFee(new BigDecimal(556.33));
//        portfolio.setTribeName("Tribe");
//        portfolio.setEmail("a@b.com");
//        portfolio.setTelephone("1223333");
//        portfolio.setNsfFee(new BigDecimal(3333.55));
//        portfolio.setFax("233322");
//        portfolio.setWebsite("http:aaa.com");

        fillContractAttr(jsonObject, "portfolioName", portfolio.getPortfolioName());
        fillContractAttr(jsonObject, "portfolioDisplayName", portfolio.getDisplayName());
        fillContractAttr(jsonObject, "portfolioAddress", portfolio.getAddress());
        fillContractAttr(jsonObject, "portfolioCity", portfolio.getCity());
        fillContractAttr(jsonObject, "portfolioState", portfolio.getState());
        fillContractAttr(jsonObject, "portfolioZip", portfolio.getZip());
        fillContractAttr(jsonObject, "portfolioLateFee", StringUtil.toCurrency(portfolio.getLateFee()));
        fillContractAttr(jsonObject, "tribeName", portfolio.getTribeName());
        fillContractAttr(jsonObject, "portfolioEmail", portfolio.getEmail());
        fillContractAttr(jsonObject, "portfolioTelephone", portfolio.getTelephone());
        fillContractAttr(jsonObject, "portfolioNsfFee", StringUtil.toCurrency(portfolio.getNsfFee()));
        fillContractAttr(jsonObject, "portfolioFax", portfolio.getFax());
        fillContractAttr(jsonObject, "portfolioWebsite", portfolio.getWebsite());

        String sbMailingAddress = (StringUtils.isEmpty(portfolio.getAddress()) ? "" : portfolio.getAddress()) +
                " " +
                (StringUtils.isEmpty(portfolio.getCity()) ? "" : portfolio.getCity()) +
                " " +
                (StringUtils.isEmpty(portfolio.getState()) ? "" : portfolio.getState()) +
                " " +
                (StringUtils.isEmpty(portfolio.getZip()) ? "" : portfolio.getZip());
        fillContractAttr(jsonObject, "portfolioMailingAddress", sbMailingAddress);
        return jsonObject;
    }

    private void fillContractAttr(JSONObject jsonObject, String key, String value) {
        if (StringUtils.isNotEmpty(value)) {
            jsonObject.put(key, value);
        } else {
            jsonObject.put(key, "");
        }
    }


    public void saveLoanFromLead(String leadStr) {

        String customerIdentifyKey = "";
        JSONObject jsonLeadObject = JSON.parseObject(leadStr);
        if (jsonLeadObject == null) {
            logger.error("Error happened during parse json data:" + leadStr);
            return;
        }

        JSONObject jsonBankObj = jsonLeadObject.getJSONObject("bank");
        if (jsonBankObj != null) {
            String keyBankAccountType = "bankAccountType";
            String keyBankAccountTypeText = "bankAccountTypeText";
            String bankAccountTypeVal = jsonBankObj.getString(keyBankAccountType);
            jsonBankObj.put(keyBankAccountType, EnumUtil.getByCode(BankAccountTypeEnum.class, bankAccountTypeVal).getValue());
            jsonBankObj.put(keyBankAccountTypeText, EnumUtil.getByCode(BankAccountTypeEnum.class, bankAccountTypeVal).getText());

            String bankAccountNoAfterFormat = combineAccountNo(jsonBankObj.getString("bankAccountNo"));
            String bankRoutingNo=jsonBankObj.getString("bankRoutingNo");
            customerIdentifyKey = bankAccountNoAfterFormat +"_"+ bankRoutingNo;
        }

        JSONObject jsonEmpObj = jsonLeadObject.getJSONObject("employment");
        if (jsonEmpObj != null) {
            String keyPayrollType = "payrollType";
            String keyPayrollTypeText = "payrollTypeText";
            String payrollTypeVal = jsonEmpObj.getString(keyPayrollType);
            jsonEmpObj.put(keyPayrollType, EnumUtil.getByCode(PayrollTypeEnum.class, payrollTypeVal).getValue());
            jsonEmpObj.put(keyPayrollTypeText,EnumUtil.getByCode(PayrollTypeEnum.class, payrollTypeVal).getText());

            String keyPayrollFrequency = "payrollFrequency";
            String payrollFrequencyVal = jsonEmpObj.getString(keyPayrollFrequency);
            jsonEmpObj.put(keyPayrollFrequency, EnumUtil.getByCode(PayrollFrequencyEnum.class, payrollFrequencyVal).getValue());
        }

        String newLeadStr = JSON.toJSONString(jsonLeadObject);
        Loan loan = JSON.parseObject(newLeadStr, Loan.class);

        int maxLength = 7;
        String portfolioCode = "IC";
        String year = DateUtil.date2str(new Date(), "yy");
        String flowNumber = businessFeignClient.getSeed(maxLength, portfolioCode, year);
        String contractNo = portfolioCode + year + flowNumber;
        logger.info("New Loan No Created: {}", contractNo);
        if (StringUtils.isNotEmpty(contractNo)) {
            logger.info("Start to save Loan Contract:" + loan.toString());
            loan.setContractNo(contractNo);
            loan.setLeadId(loan.getId());
            loan.setId(null);
            loan.setLoanStatus(LoanStatusEnum.INITIALIZED.getValue());
            loan.setLoanStatusText(LoanStatusEnum.INITIALIZED.getText());
            loan.setCreateTime(DateUtil.getCurrentTimestamp());
            loan.setUpdateTime(DateUtil.getCurrentTimestamp());
            loan.setCustomerIdentifyKey(customerIdentifyKey);
            Loan dbLoan = loanRepository.save(loan);
            Integer loanId = dbLoan.getId();
            loan.setId(loanId);

            Personal personal = loan.getPersonal();
            if (personal != null) {
                personal.setLoanId(loanId);

                String firstName = personal.getFirstName();
                String middleName = personal.getMiddleName();
                String lastName = personal.getLastName();

                String fullName = (StringUtils.isEmpty(firstName) ? "" : firstName) +
                        (StringUtils.isEmpty(middleName) ? "" : " " + middleName) +
                        (StringUtils.isEmpty(lastName) ? "" : " " + lastName);
                personal.setFullName(fullName);

                String address1 = personal.getAddress1();
                String address2 = personal.getAddress2();
                String address = (StringUtils.isEmpty(address1) ? "" : address1) + (StringUtils.isEmpty(address2) ? "" : " | " + address2);
                personal.setAddress(address);

                personalRepository.save(personal);
                logger.debug("save personal info success.");
            } else {
                logger.error("No Personal information found in lead.");
            }

            Employment employment = loan.getEmployment();
            if (employment != null) {
                employment.setLoanId(loanId);

                String address1 = employment.getEmployerAddress1();
                String address2 = employment.getEmployerAddress2();
                String address = (StringUtils.isEmpty(address1) ? "" : address1) + (StringUtils.isEmpty(address2) ? "" : " | " + address2);
                employment.setEmployerAddress(address);

                employmentRepository.save(employment);
                logger.debug("save employment info success.");
            } else {
                logger.error("No employment information found in lead.");
            }

            Bank bank = loan.getBank();
            if (bank != null) {
                bank.setLoanId(loanId);

                if (employment != null) {
                    if (employment.getPayrollType() != null) {
                        bank.setPayrollType(employment.getPayrollType());
                    }

                    if (employment.getPayrollFrequency() != null) {
                        bank.setPayrollFrequency(employment.getPayrollFrequency());
                    }

                    if (employment.getFirstPayDate() != null) {
                        bank.setFirstPayDate(employment.getFirstPayDate());
                    }
                }

                bankRepository.save(bank);
                logger.debug("save bank info success.");
            } else {
                logger.error("No Bank information found in lead.");
            }

            logger.info("Loan saved success.");
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("contractNo",contractNo);
            jsonObject.put("appData", JSON.toJSONString(loan));
            timeLineApiService.addLoanStatusChangeTimeline(null, LoanStatusEnum.INITIALIZED.getValue(), jsonObject.toJSONString());
        } else {
            logger.error("Can't create a contractNo");
        }
    }

    public Integer saveLoanOnly(Loan loan) {
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
//            if (loan != null && loan.getLoanStatus() == status) {
//                i++;
//            }
//        }
//            List<Loan> loans = loanRepository.getLoanByStatusAndLeadId(status, leadIds);
        List<Loan> loans = loanRepository.findAllByLoanStatusAndLeadIdIn(status, leadIds);
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
                    String loanStatusStr = timeLineApiService.getLoanStatus(contractNum);
                    logger.info(loanStatusStr);
                    if (StringUtils.isNotEmpty(loanStatusStr)) {
                        JSONObject jsonObject = JSONObject.parseObject(timeLineApiService.getLoanStatus(contractNum));
                        if (jsonObject != null) {
                            String statusValue = jsonObject.getString("status");
                            if (StringUtils.isNotEmpty(statusValue)) {
                                loanStatus = Integer.parseInt(statusValue);
                            }
                        }
                    }

                    contractInfo.put("status", loanStatus);
                }
                result.add(contractInfo);
            }
        }

        return JSONArray.toJSONString(result);
    }

    public String getTodoListLoanInfo(String operatorNo, String operationNameListStr, String queryStatusList) {
        HashSet<String> contractNos = timeLineApiService.getTodoContractNo(operatorNo, operationNameListStr,queryStatusList);
        JSONArray toDoList = new JSONArray();

        if (contractNos != null) {
            for (String contractNo : contractNos) {
                Loan loan = getLoanByContactNo(contractNo);
                if (loan != null) {
                    JSONObject contractInfo = getFormedLoanDataById(loan.getId());
                    if (contractInfo != null) {
                        contractInfo.put("eventType", EventTypeEnum.UPDATE_REGISTER_INFORAMTION.getValue());
                        toDoList.add(contractInfo);
                    }
                }
            }
        }

        return JSONArray.toJSONString(toDoList);
    }

    public String countWithDrawnLoan(String startTime, String endTime) {

        JSONArray withdrawnLoans = new JSONArray();
        HashSet<Integer> reasonsPositive = new HashSet<Integer>();
        HashSet<Integer> reasonsNegative = new HashSet<Integer>();

        Date start = new Date(Long.parseLong(startTime));
        Date end = new Date(Long.parseLong(endTime));
        List<Loan> withdrawnLoancs = loanRepository.countWithDrawnloans(start.getTime(), end.getTime());


        for (Loan contract : withdrawnLoancs) {
            if (((contract.getLoanStatus()) & (LoanStatusEnum.POSITIVE.getValue())) == LoanStatusEnum.POSITIVE.getValue()) {
                reasonsPositive.add(contract.getWithdrawnCode());
            } else if (((contract.getLoanStatus()) & (LoanStatusEnum.NEGATIVE.getValue())) == LoanStatusEnum.NEGATIVE.getValue()) {
                reasonsNegative.add(contract.getWithdrawnCode());
            }
        }


        for (Integer reasonCode : reasonsPositive) {
            JSONObject countDataP = new JSONObject();
            Integer i = 0;

            countDataP.put("type", "POSITIVE");
            countDataP.put("withdrawnReason", EnumUtil.getByValue(WithdrawnReasonEnum.class, reasonCode).getText());
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
            countDataN.put("withdrawnReason", EnumUtil.getByValue(WithdrawnReasonEnum.class, reasonCodeN).getText());
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

        return JSONArray.toJSONString(withdrawnLoans);

    }


    public String countPendingSummary(String ids) {


        JSONArray summaryArr = new JSONArray();
        JSONObject leadIds = JSONObject.parseObject(ids);

        JSONArray newcustIdArr = JSONArray.parseArray(leadIds.getString("newcustids"));

        JSONArray returnIdArr = leadIds.getJSONArray("returnIds");
//        JSONArray purcharsedIddArr = leadIds.getJSONArray("purchasedIds");
        JSONArray purcharsedIddArr = new JSONArray();

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

        JSONObject result = new JSONObject();
        Integer reapplied = 0;
        Integer purchased = countPendingByItems(purcharsedIddArr, status).getInteger("resultCount");
        Integer newcust = countPendingByItems(newcustIdArr, status).getInteger("resultCount");
        Integer returncust = countPendingByItems(returnIdArr, status).getInteger("resultCount");



        JSONArray newcustLoanIds = countPendingByItems(newcustIdArr, status).getJSONArray("loanIds");
        JSONArray purchasedLoanIds = countPendingByItems(purcharsedIddArr, status).getJSONArray("loanIds");
        JSONArray returncustLoanIds = countPendingByItems(returnIdArr, status).getJSONArray("loanIds");

//        logger.warn("newcustLoanIds: "+newcustLoanIds);

        newcustLoanIds.addAll(purchasedLoanIds);
        newcustLoanIds.addAll(returncustLoanIds);

        HashSet<Integer> loanIds = new HashSet<>();
        if (newcustLoanIds.size() != 0){
            for (Object newcustLoanId : newcustLoanIds) {
                loanIds.add((int) newcustLoanId);
            }
        }
        result.put("loanStatus", EnumUtil.getByValue(LoanStatusEnum.class, status).getText());
        result.put("purchased", purchased);
        result.put("reapplied", reapplied);
        result.put("newcust", newcust);
        result.put("returnCust", returncust);
        result.put("total", purchased + reapplied + newcust + returncust);
        result.put("loanIds", loanIds);
        return result;
    }

    public JSONObject countPendingByItems(JSONArray arr, Integer status) {

        JSONObject countResult = new JSONObject();

        Integer resultCount = 0;
        List<Integer> loanIds = new ArrayList();
        List<Integer> leads = new ArrayList<>();
        if (arr.size() != 0) {
            for (int i = 0; i < arr.size(); i++) {
                leads.add((int) arr.get(i));
            }
            List<Loan> loans = loanRepository.findAllByLoanStatusAndLeadIdIn(status, leads);
            if (loans != null) {
                resultCount = loans.size();

                for (Loan loan : loans) {
                    loanIds.add(loan.getId());
                }
            }
        }
        countResult.put("resultCount", resultCount);
        countResult.put("loanIds", loanIds);
        return countResult;
    }



    public String newPendingSummary(long startTime,long endTime){

        JSONObject leadIds = new JSONObject();
        List<Integer> newLeadIds=loanRepository.listLeadIdByTimeRange(1, startTime, endTime);
        List<Integer> returnLeadIds=loanRepository.listLeadIdByTimeRange(2, startTime, endTime);
        if (newLeadIds!=null && returnLeadIds!=null){
            leadIds.put("newcustids", newLeadIds);
            leadIds.put("returnIds", returnLeadIds);
        }

        String summaryResut =countPendingSummary(JSONObject.toJSONString(leadIds));

        return summaryResut;

    }

    public String getLoanAndPersonalInfoByIds(String ids){
        JSONArray idsArr = JSONArray.parseArray(ids);
        List<Integer> idsList = new ArrayList<>();
        if (idsArr!=null && idsArr.size()!=0) {
            for (Object loanId : idsArr) {
                idsList.add(((int) loanId));
            }
        }
        List<Loan> loans = loanRepository.findAllById(idsList);
        for (Loan loan:loans){
            loan.setPersonal(personalRepository.findByLoanId(loan.getId()));
        }

        return JSONArray.toJSONString(loans);
    }


    public String combineAccountNo(String accountNo){
        String ac1 = removePrefix(accountNo);
        String ac2 = removeSuffix(ac1);
        return ac2;
    }

    public String removePrefix(String target) {

        if (target.startsWith("0")) {
            String temp = target.substring(1);
            return removePrefix(temp);
        } else {
            return  target;
        }

    }

    public String removeSuffix(String target) {
        if (target.endsWith("0")) {
            String temp = target.substring(0,target.length()-1);
            return removeSuffix(temp);
        } else {
            return  target;
        }
    }


    public String listReturningCustomerRecentLoans(Integer loanId) {


        JSONArray recentLoanLists = new JSONArray();
//        String customerIdentifyKey = "303094440903_124303120";
        String customerIdentifyKey = loanRepository.findById(loanId).getCustomerIdentifyKey();
        List<Loan> loans=loanRepository.findAllByCustomerIdentifyKey(customerIdentifyKey);

        List<Integer> loanIds = new ArrayList<>();
        if (loans!=null && loans.size()!=0){
            for (Loan loan:loans){
               JSONObject recentLoan= getFormedLoanDataById(loan.getId());

                String contractNum;
                Integer loanStatus = null;
                if (recentLoan != null) {
                    contractNum = recentLoan.getString("contractNo");
                    String loanStatusStr = timeLineApiService.getLoanStatus(contractNum);
                    logger.info(loanStatusStr);
                    if (StringUtils.isNotEmpty(loanStatusStr)) {
                        JSONObject jsonObject = JSONObject.parseObject(timeLineApiService.getLoanStatus(contractNum));
                        if (jsonObject != null) {
                            String statusValue = jsonObject.getString("status");
                            if (StringUtils.isNotEmpty(statusValue)) {
                                loanStatus = Integer.parseInt(statusValue);
                            }
                        }
                    }
                    recentLoan.put("status", loanStatus);
                }
                recentLoanLists.add(recentLoan);
            }
        }
        return JSONArray.toJSONString(recentLoanLists);
    }

    public String setFollowUp(String type,String timeData,String contractNo){

        JSONObject additionObj = new JSONObject();

        long followUpDatetime = 0;

        JSONObject dateObj = JSONObject.parseObject(timeData);
        if (("relative").equals(type)){
            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date());
            int day = cal.get(Calendar.DATE);
            int hour = cal.get(Calendar.HOUR);
            int minute = cal.get(Calendar.MINUTE);
            cal.set(Calendar.DAY_OF_MONTH,day+dateObj.getInteger("Day"));
            cal.set(Calendar.HOUR_OF_DAY, hour+dateObj.getInteger("Hour"));
            cal.set(Calendar.MINUTE,minute+dateObj.getInteger("Minute"));
            followUpDatetime = cal.getTimeInMillis();
        }else if (("absolute").equals(type)){
            followUpDatetime = dateObj.getLong("absoluteTime");
        }else{
            logger.error("Save Follow Up Date Time failed!");
        }
        Loan loan=loanRepository.findByContractNo(contractNo);

        if (loan!=null){
            loan.setFollowUp(followUpDatetime);
            loanRepository.save(loan);
        }

        additionObj.put("operatorNo", loan.getLockedOperatorNo());
        additionObj.put("operatorName", loan.getLockedOperatorName());

        timeLineApiService.addFollowUpTimeline(contractNo, JSONObject.toJSONString(additionObj));
        return JSONObject.toJSONString(loan);
    }


    public String generateNewLoan(String operatorNo, Integer loanStatus) {

        String contractNo = "";
        String agentLevelObj = employeeFeignClient.getAgentLevel(operatorNo);
        String operatorName = "";
        int lockedMaxNumber = 2;

        if (agentLevelObj!=null){
            logger.info("get agent information: "+agentLevelObj);
            Integer portfolioId = JSONObject.parseObject(agentLevelObj).getInteger("portfolioId");
            Integer level = JSONObject.parseObject(agentLevelObj).getInteger("level");
            operatorName = JSONObject.parseObject(agentLevelObj).getString("employeeFullName");

            List<Loan> lockedLoans;
            if (loanStatus <= 2) {
                //for agent review
                List<Integer> loanStatusList = new ArrayList<>();
                loanStatusList.add(LoanStatusEnum.INITIALIZED.getValue());
                loanStatusList.add(LoanStatusEnum.AGENT_REVIEW.getValue());
                lockedLoans = getLockedLoans(portfolioId, operatorNo, loanStatusList);
                if (lockedLoans.size() < lockedMaxNumber ) {
                    String followupContractNo = getFollowUpLoans(portfolioId, operatorNo);
                    if (("There are no followup loan").equals(followupContractNo)) {
                        String workedContractNo = getWorkedLoan(operatorNo);
                        if (("There are not loan in worked").equals(workedContractNo)) {
                            contractNo = getNewApplication(operatorNo, loanStatusList,level);
                        } else {
                            contractNo = workedContractNo;
                        }
                    } else {
                        contractNo = followupContractNo;
                    }
                }
            } else {
                //for underwriter and tribe
                List<Integer> loanStatusList = new ArrayList<>();
                loanStatusList.add(loanStatus);
                lockedLoans = getLockedLoans(portfolioId, operatorNo, loanStatusList);
                if (lockedLoans.size() < lockedMaxNumber) {
                    contractNo = getNewApplication(operatorNo, loanStatusList,level);
                }
            }

            if(StringUtils.isEmpty(contractNo)){
                logger.info("Finally, try to find a loan from locked loans.");
                int lockedLoanSize = lockedLoans.size();
                if(lockedLoanSize <= 0){
                    contractNo = "";
                }else if(lockedLoanSize < 2){
                    contractNo = lockedLoans.get(0).getContractNo();
                }else {
                    sortLoanByLockedTime(lockedLoans);
                    contractNo = lockedLoans.get(0).getContractNo();
                }
            }

            if(StringUtils.isNotEmpty(contractNo)){
                Loan newLoan=loanRepository.findByContractNo(contractNo);
                if (newLoan!=null){
                    String oldOperatorNo = newLoan.getLockedOperatorNo();
                    newLoan.setLockedAt(DateUtil.getCurrentTimestamp());
                    newLoan.setLockedOperatorName(operatorName);
                    newLoan.setLockedOperatorNo(operatorNo);
                    newLoan.setOperatorNo(operatorNo);
                    newLoan.setFollowUp(null);
                    newLoan.setUpdateTime(DateUtil.getCurrentTimestamp());
                    loanRepository.save(newLoan);

                    if (oldOperatorNo == null ||!operatorNo.equals(oldOperatorNo)){
                        JSONObject additionObj = new JSONObject();
                        additionObj.put("operatorNo", operatorNo);
                        additionObj.put("operatorName", operatorName);
                        timeLineApiService.addLockOrUnlockOrGrabLockTimeLine(contractNo, JSONObject.toJSONString(additionObj), "Lock Operation");
                    }
                }
            }
        }else {
            return "Get Agent Information Error! Please Check And Make Your Account Correct ";
        }
        return contractNo;

    }


    private List<Loan> getLockedLoans(Integer portfolioId, String operatorNo, List<Integer> loanStatusList){
        logger.info("start to query the locked loan for portfolioId:{}, operatorNo:{}, loanStatus:{}", portfolioId, operatorNo, JSON.toJSONString(loanStatusList));
        List<Loan> lockedLoans = loanRepository.findAllByLockedOperatorNoAndPortfolioIdAndLoanStatusIn(operatorNo, portfolioId, loanStatusList);
        logger.info("Locked loan list size:{}", lockedLoans.size());
        logger.debug("Locked loan list:{}", JSON.toJSONString(lockedLoans));
        return lockedLoans;
    }

    /**
     * get loans from followped loans
     * @param portfolioId
     * @return
     */
    public String getFollowUpLoans(Integer portfolioId, String operatorNo){
        logger.info("Start to get followup loan for portfolioId:{}, operatorNo:{}", portfolioId, operatorNo);
        String contractNo="";
        long outtime = 5;

        List<Loan> followUpLoans = loanRepository.findAllFollowUpdLoans(portfolioId);
        if (followUpLoans!=null && followUpLoans.size()!=0){

            sortLoanByFollowupTime(followUpLoans);

            for (Loan loan:followUpLoans){
                if (operatorNo.equals(loan.getLockedOperatorNo())){
                    if (loan.getFollowUp()-(new Date()).getTime()<outtime*60*1000){
                        contractNo = loan.getContractNo();
                    }else{
                        continue;
                    }
                }else {
                    if ((new Date()).getTime()-loan.getFollowUp()>0){
                        contractNo = loan.getContractNo();
                    }else {
                        continue;
                    }
                }
            }

        }
        if (StringUtils.isEmpty(contractNo)){
            contractNo = "There are no followup loan";
        }

        logger.info("get followUpd loan!  "+contractNo);

        return contractNo;
    }


    public String getWorkedLoan(String operatorNo){
        logger.info("Start to get worked loan for operatorNo:{}", operatorNo);
        String workedContractNo = "";
        List<String> contractArr=(timeLineApiService.getWorkedConteactNo(operatorNo, EventTypeEnum.UPDATE_REGISTER_INFORAMTION.getValue())).toJavaList(String.class);

        List<String> agentContractNos = loanRepository.findContractNoByLoanStatus(LoanStatusEnum.AGENT_REVIEW.getValue());

        logger.info("contractArr = "+contractArr);
        logger.info("agentContractNos = "+agentContractNos);

        List<Loan> loans = new ArrayList<>();

        agentContractNos.retainAll(contractArr);

        logger.info("agentContractNos = "+agentContractNos);

        if (agentContractNos.size()>0){
            for (String contractNo:agentContractNos){
                Loan loan=loanRepository.findByContractNo(contractNo);
                if (loan!=null){
                    loans.add(loan);
                }
            }
        }
        if (loans.size()!=0) {
            sortLoanByUpdateTime(loans);
            workedContractNo = loans.get(0).getContractNo();
        }else{
            workedContractNo = "There are not loan in worked";
        }
        logger.info("get worked loan!  "+ workedContractNo);
        return workedContractNo;
    }

    public String getNewApplication(String operatorNo,List<Integer> loanStatus,Integer agentLevel){
        logger.info("Start to get new loan for operatorNo:{}, loanStatus:{}, agentLevel:{}", operatorNo, JSON.toJSONString(loanStatus), agentLevel);
        String contractNo = "";
        Integer agentCategory=employeeFeignClient.getCategoryByEmployeeNo(operatorNo);

        logger.info("agentCategory:"+agentCategory);
        List<Loan> newloans = new ArrayList<>();
        if (agentCategory!=null &&agentLevel!=null){
            if(agentCategory==2){
                newloans = loanRepository.findLoansByCategoryAndPriority(agentCategory, agentLevel,loanStatus,operatorNo);
            }else {
                newloans = loanRepository.findAllByCategoryOrderByCreateTimeDesc(agentCategory,loanStatus,operatorNo);
            }

            if (newloans.size()>0){
                contractNo = newloans.get(0).getContractNo();
            }
        }else {
            logger.error("The agent haven`t permission to get new loan!");
        }
        logger.info("get new application loan:  " + contractNo);
        return contractNo;
    }

    /**
     * sort loans by lockedTime DESC
     * @param loans
     */
    public void sortLoanByLockedTime(List<Loan> loans) {
        Collections.sort(loans, new Comparator<Loan>() {
            @Override
            public int compare(Loan loan1, Loan loan2) {
                if (loan1.getLockedAt() < loan2.getLockedAt()) {
                    return 1;
                } else if (loan1.getLockedAt().equals(loan2.getLockedAt()) ) {
                    return 0;
                } else {
                    return -1;
                }
            }
        });
    }

    /**
     * sort loans by FollowupTime DESC
     * @param loans
     */
    public void sortLoanByFollowupTime(List<Loan> loans) {
        Collections.sort(loans, new Comparator<Loan>() {
            @Override
            public int compare(Loan loan1, Loan loan2) {
                if (loan1.getFollowUp() < loan2.getFollowUp()) {
                    return 1;
                } else if (loan1.getFollowUp() == loan2.getFollowUp()) {
                    return 0;
                } else {
                    return -1;
                }
            }
        });
    }

    /**
     * sort loans by Update Time DESC
     * @param loans
     */
    public void sortLoanByUpdateTime(List<Loan> loans) {
        Collections.sort(loans, new Comparator<Loan>() {
            @Override
            public int compare(Loan loan1, Loan loan2) {
                if (loan1.getUpdateTime()< loan2.getUpdateTime()) {
                    return 1;
                } else if (loan1.getUpdateTime() == loan2.getUpdateTime()) {
                    return 0;
                } else {
                    return -1;
                }
            }
        });
    }


    /**
     * 提出Grab Loan
     * @param contractNo
     * @param operatorNo
     * @return
     */
    public String grabLoan(String contractNo,String operatorNo,String operatorName){

        String result = "";
        Loan loan=loanRepository.findByContractNo(contractNo);
        Timer timer = new Timer();
        if (loan!=null){
            String grabTargetOperatorNo=loan.getLockedOperatorNo();
            String agentLevelStr = employeeFeignClient.getAgentLevel(operatorNo);
            String targetAgentLevelStr=employeeFeignClient.getAgentLevel(grabTargetOperatorNo);

            JSONObject agentLevelObj = JSONObject.parseObject(agentLevelStr);
            JSONObject targetAgentLevelObj=JSONObject.parseObject(targetAgentLevelStr);

            Integer agentLevel = agentLevelObj.getInteger("level");
            Integer targetAgentLevel = targetAgentLevelObj.getInteger("level");

            JSONObject grabInfoObj = new JSONObject();
            grabInfoObj.put("contractNo", contractNo);
            grabInfoObj.put("grabBy", operatorNo);
            grabInfoObj.put("grabTarget", grabTargetOperatorNo);

            if (agentLevel>targetAgentLevel){
                grabInfoObj.put("isToSubordinate", 1);
                logger.error(JSONObject.toJSONString(grabInfoObj));
                result=grabLoanFeignClient.addGrabService(JSONObject.toJSONString(grabInfoObj));
                DelayTask delayTask = new DelayTask(JSONObject.parseObject(result).getInteger("id"),1,operatorNo,operatorName,grabLoanFeignClient,loanRepository);
                timer.schedule(delayTask,5*1000);
            }else if(agentLevel<targetAgentLevel){
                result ="false";
            }else {
                grabInfoObj.put("isToSubordinate", 2);
                result=grabLoanFeignClient.addGrabService(JSONObject.toJSONString(grabInfoObj));
                DelayTask delayTask = new DelayTask(JSONObject.parseObject(result).getInteger("id"),2,operatorNo,operatorName,grabLoanFeignClient,loanRepository);
                timer.schedule(delayTask,25*1000);
            }
        }

        JSONObject additionObj = new JSONObject();
        additionObj.put("operatorNo", operatorNo);
        additionObj.put("operatorName", operatorName);
        timeLineApiService.addLockOrUnlockOrGrabLockTimeLine(contractNo, JSONObject.toJSONString(additionObj),"Grab Request Operation");

        if (StringUtils.isNotEmpty(result)){
            return result;
        }else {
            logger.error("Grab Loan Failed!");
            return "Grab Loan Failed!";
        }
    }


    public String acceptGrabLoan(Integer grabId){

        String grabResultStr = grabLoanFeignClient.acceptGrab(grabId);

        if (StringUtils.isNotEmpty(grabResultStr)){
            String contractNo = JSONObject.parseObject(grabResultStr).getString("contractNo");
            Loan loan=loanRepository.findByContractNo(contractNo);

            String operatoeNo = JSONObject.parseObject(grabResultStr).getString("grabBy");
            String employeeInfo = employeeFeignClient.getAgentLevel(operatoeNo);
            String operatorName=JSONObject.parseObject(employeeInfo).getString("employeeFullName");

            String grabTarget = JSONObject.parseObject(grabResultStr).getString("grabTarget");
            String employeeInfoTarget = employeeFeignClient.getAgentLevel(grabTarget);
            String operatorNameTarget=JSONObject.parseObject(employeeInfoTarget).getString("employeeFullName");

            loan.setLockedAt(DateUtil.getCurrentTimestamp());
            loan.setLockedOperatorNo(operatoeNo);
            loan.setLockedOperatorName(operatorName);
            loan.setOperatorNo(operatoeNo);
            Loan grabbedLoanInfo=loanRepository.save(loan);

            JSONObject additionObj = new JSONObject();
            additionObj.put("operatorNo", grabTarget);
            additionObj.put("operatorName", operatorNameTarget);
            timeLineApiService.addLockOrUnlockOrGrabLockTimeLine(contractNo, JSONObject.toJSONString(additionObj),"Grab Accept Operation");

            return JSONObject.toJSONString(grabbedLoanInfo);
        }else {
            return "false";
        }

    }

    public String rejectGrabLoan(Integer grabId){
        String grabResultStr = grabLoanFeignClient.rejectGrab(grabId);

        String contractNo = JSONObject.parseObject(grabResultStr).getString("contractNo");
        String grabTarget = JSONObject.parseObject(grabResultStr).getString("grabTarget");
        String employeeInfoTarget = employeeFeignClient.getAgentLevel(grabTarget);
        String operatorNameTarget=JSONObject.parseObject(employeeInfoTarget).getString("employeeFullName");
        JSONObject additionObj = new JSONObject();
        additionObj.put("operatorNo", grabTarget);
        additionObj.put("operatorName", operatorNameTarget);
        timeLineApiService.addLockOrUnlockOrGrabLockTimeLine(contractNo, JSONObject.toJSONString(additionObj),"Grab Reject Operation");

        return grabResultStr;
    }


    public String unlockLoan(String contractNo){
        String result = "";
        Loan loan=loanRepository.findByContractNo(contractNo);

        JSONObject additionObj = new JSONObject();
        additionObj.put("operatorNo", loan.getLockedOperatorNo());
        additionObj.put("operatorName", loan.getLockedOperatorName());

        if (loan!=null){
            loan.setLockedOperatorName(null);
            loan.setLockedOperatorNo(null);
            loan.setLockedAt(null);
            loan.setFollowUp(null);
            Loan resultLoan=loanRepository.save(loan);
            result = JSONObject.toJSONString(resultLoan);
        }else {
            logger.error("Get Unlocked Loan Failed,Please Check And Make Sure The Loan Number Correct!");
            result = "false";
        }

        timeLineApiService.addLockOrUnlockOrGrabLockTimeLine(contractNo, JSONObject.toJSONString(additionObj), "Unlock Operation");

        if (StringUtils.isNotEmpty(result)){
            return result;
        }else {
            return "false";
        }

    }


    public String saveLoanFromCustomerInAuto(String customerStr) {

        String result = "";

        if (StringUtils.isEmpty(customerStr)) {
            return "Customer Information Error!";
        }
        JSONObject customerObj = JSONObject.parseObject(customerStr);
        Loan loan = new Loan();
        int maxLength = 7;
        String portfolioCode = "OIC";
        String year = DateUtil.date2str(new Date(), "yy");
        String flowNumber = businessFeignClient.getSeed(maxLength, portfolioCode, year);
        String contractNo = portfolioCode + year + flowNumber;
        loan.setContractNo(contractNo);
        loan.setCreateTime(DateUtil.getCurrentTimestamp());
        loan.setReceiveTime(DateUtil.getCurrentTimestamp());
        loan.setUpdateTime(DateUtil.getCurrentTimestamp());
        loan.setCustomerInAutoId(customerObj.getInteger("customerInOnlineId"));
        loan.setPortfolioId(customerObj.getInteger(JsonKeyConst.PORTFOLIO_ID));
        loan.setFlags(10);
        loan.setLoanStatus(LoanStatusEnum.INITIALIZED.getValue());
        loan.setLoanStatusText(LoanStatusEnum.INITIALIZED.getText());
        loan.setCategory(1);
        Loan dbLoan = loanRepository.save(loan);
        Integer loanId = dbLoan.getId();
        loan.setId(loanId);

        JSONObject personalObj = customerObj.getJSONObject(JsonKeyConst.PERSONAL);
        personalObj.put("loanId", loanId);
        Personal personal = JSONObject.parseObject(JSONObject.toJSONString(personalObj), Personal.class);
        personalRepository.save(personal);

        JSONObject bankObj = customerObj.getJSONObject(JsonKeyConst.BANK);
        bankObj.put("loanId", loanId);
        Bank bank = JSONObject.parseObject(JSONObject.toJSONString(bankObj), Bank.class);
        bankRepository.save(bank);

        JSONObject employmentObj = customerObj.getJSONObject(JsonKeyConst.EMPLOYMENT);
        employmentObj.put("loanId", loanId);
        Employment employment = JSONObject.parseObject(JSONObject.toJSONString(employmentObj), Employment.class);
        employmentRepository.save(employment);

        JSONObject additionDataObj = new JSONObject();
        additionDataObj.put("contractNo", loan.getContractNo());
        additionDataObj.put("appData", JSONObject.toJSONString(getLoanByLoanId(loan.getId())));
        timeLineApiService.addLoanStatusChangeTimeline(null, LoanStatusEnum.INITIALIZED.getValue(), JSONObject.toJSONString(additionDataObj));
        result = dbLoan.getContractNo();
        return result;
    }

    public String getLoanInAuto(Integer loanStatus){
        List<Loan> loans = loanRepository.findCustomerInAutoLoan(loanStatus);

        String result = "";
        if (loans!=null) {
            Loan loan = loans.get(0);
            if (loan != null) {
                Loan detailLoan = getLoanByLoanId(loan.getId());
                result = JSONObject.toJSONString(detailLoan);
            } else {
                result = "Get Auto Loan Failed!";
            }
        }else {
            logger.error("Get Auto Loan Error!");
        }

        return result;
    }

    //Underwrite Online
    public String updateLoanDetailInAuto(String loanStr){

        if(StringUtils.isEmpty(loanStr)){
            return "Update Loan Failed!";
        }

        JSONObject loanObj = JSONObject.parseObject(loanStr);
        Loan loan = loanRepository.findById(loanObj.getInteger("id"));
        if (loan!=null) {
            loan.setLoanStatus(LoanStatusEnum.TRIBE_REVIEW.getValue());
            loan.setReviewData(loanObj.getString("dataReview"));
            loanRepository.save(loan);

            JSONObject personalObj = loanObj.getJSONObject("personal");
            JSONObject bankObj = loanObj.getJSONObject("bank");
            JSONObject employmentObj = loanObj.getJSONObject("employment");

            Personal personal = personalRepository.findByLoanId(loan.getId());
            Bank bank = bankRepository.findByLoanId(loan.getId());
            Employment employment = employmentRepository.findByLoanId(loan.getId());

            personal.setFirstName(personalObj.getString(JsonKeyConst.FIRST_NAME));
            personal.setMiddleName(personalObj.getString(JsonKeyConst.MIDDLE_NAME));
            personal.setLastName(personalObj.getString(JsonKeyConst.LAST_NAME));
            personal.setAddress(personalObj.getString(JsonKeyConst.ADDRESS));
            personal.setCity(personalObj.getString(JsonKeyConst.CITY));
            personal.setState(personalObj.getString(JsonKeyConst.STATE));
            personal.setZip(personalObj.getString(JsonKeyConst.ZIP));
            personal.setHomePhone(personalObj.getString(JsonKeyConst.HOME_PHONE));
            personal.setMobilePhone(personalObj.getString(JsonKeyConst.MOBILE_PHONE));
            personal.setEmail(personalObj.getString(JsonKeyConst.EMAIL));
            personalRepository.save(personal);

            JSONObject addtionData = new JSONObject();
            addtionData.put("contractNo", loan.getContractNo());
            addtionData.put("appData", JSONObject.toJSONString(loan));
            timeLineApiService.addLoanStatusChangeTimeline(LoanStatusEnum.UNDERWRITER_REVIEW.getValue(), LoanStatusEnum.TRIBE_REVIEW.getValue(), JSONObject.toJSONString(addtionData));
            if (employment==null){
                Employment employmentNew = new Employment();
                employmentNew.setEmployerName(employmentObj.getString(JsonKeyConst.EMPLOYER_NAME));
                employmentNew.setLoanId(loan.getId());

                employmentRepository.save(employmentNew);
            }else {
                employment.setEmployerName(employmentObj.getString(JsonKeyConst.EMPLOYER_NAME));
                employmentRepository.save(employment);
            }

            bank.setBankAccountType(bankObj.getInteger(JsonKeyConst.BANK_ACCOUNT_TYPE));
            bank.setBankRoutingNo(bankObj.getString(JsonKeyConst.BANK_ROUTING_NO));
            bank.setBankName(bankObj.getString(JsonKeyConst.BANK_NAME));
            bank.setBankPhone(bankObj.getString("bankPhone"));
            bank.setBankAvailableBalance(bankObj.getDouble(JsonKeyConst.BANK_AVAILABLEBALANCE));
            bank.setPayrollFrequency(bankObj.getInteger(JsonKeyConst.PAYROLL_FREQUENCY));
            bank.setBankDeposits(bankObj.getString("bankDeposits"));
            bankRepository.save(bank);

        }else{
            logger.error("Catch Loan Failed When Save Loan Info!");
        }
        return JSONObject.toJSONString(loan);
    }

    public String saveLoanInAutoLoanSize(String loanStr){
        if(StringUtils.isEmpty(loanStr)){
            return "Update Loan Failed!";
        }
        JSONObject loanObj = JSONObject.parseObject(loanStr);
//        List<Loan> loans = loanRepository.findByCustomerInAutoIdOrderByUpdateTimeDesc(loanObj.getInteger("customerInOnlineId"));
//        Loan loan = loans.get(0);

        Loan loan = loanRepository.findByContractNo(loanObj.getString("contractNo"));
        if (loan!=null){

//            loan.setContractNo(loanObj.getString("contractNo"));
//            loan.setLoanStatus(LoanStatusEnum.UNDERWRITER_REVIEW.getValue());
            loanRepository.save(loan);
            JSONObject paymentObj = loanObj.getJSONObject("payment");
            JSONObject bankObj = loanObj.getJSONObject("bank");
            Bank bank = bankRepository.findByLoanId(loan.getId());
            Payment payment = paymentRepository.findByLoanId(loan.getId());

            bank.setPayrollFrequency(bankObj.getInteger("payrollFrequency"));
            bank.setBankException(bankObj.getString("bankException"));
            bank.setAmountOfOpenLoans(bankObj.getDouble("amountOfOpenLoans"));
            bank.setNumberOfOpenLoans(bankObj.getInteger("amountOfOpenLoans"));
            bankRepository.save(bank);

            if (payment!=null){
                payment.setTotalPrincipal(paymentObj.getDouble("totalPrincipal"));
                paymentRepository.save(payment);
            }else{
                Payment paymentNew = new Payment();
                paymentNew.setLoanId(loan.getId());
                paymentNew.setTotalPrincipal(paymentObj.getDouble("totalPrincipal"));
                paymentRepository.save(paymentNew);
            }
        }else{
            logger.error("Catch Loan Failed When Save Loan Size! and which customerId=");
        }
        return JSONObject.toJSONString(loan);

    }

    //online tribe
    public String updateLoanTribeOnline(String loanStr){
        if(StringUtils.isEmpty(loanStr)){
            return "Update Loan Failed!";
        }


        JSONObject loanObj = JSONObject.parseObject(loanStr);
        Loan loan = loanRepository.findById(loanObj.getInteger("id"));

        if (loan!=null){
            if (loanObj.getInteger("loanStatus")==32) {
                loan.setReviewData(loanObj.getString("reviewData"));
                loan.setLoanStatus(LoanStatusEnum.APPROVED.getValue());
                loanRepository.save(loan);
                JSONObject addtionData = new JSONObject();
                addtionData.put("contractNo", loan.getContractNo());
                addtionData.put("appData", JSONObject.toJSONString(loan));
                timeLineApiService.addLoanStatusChangeTimeline(LoanStatusEnum.TRIBE_REVIEW.getValue(), LoanStatusEnum.APPROVED.getValue(), JSONObject.toJSONString(addtionData));

                JSONObject formatLoan = getFormedLoanDataById(loan.getId());
                formatLoan.put("bankInterestDue", 0);
                String contract = JSON.toJSONString(formatLoan);
                RabbitMessage message=new RabbitMessage();
                message.setCreateTime(new Date());
                message.setMessageData(contract);
                message.setOperationName("send loan to loanSchedule");
                message.setProducer("Contract service");

            }else if (loanObj.getInteger("loanStatus")==4){
                loan.setReviewData(loanObj.getString("reviewData"));
                loan.setLoanStatus(LoanStatusEnum.UNDERWRITER_REVIEW.getValue());
                loanRepository.save(loan);
                JSONObject addtionData = new JSONObject();
                addtionData.put("contractNo", loan.getContractNo());
                addtionData.put("appData", JSONObject.toJSONString(loan));
                timeLineApiService.addLoanStatusChangeTimeline(LoanStatusEnum.TRIBE_REVIEW.getValue(), LoanStatusEnum.UNDERWRITER_REVIEW.getValue(), JSONObject.toJSONString(addtionData));
            }
        }else{
            logger.error("Save Online Tribe Loan Failed!");
        }
        return JSONObject.toJSONString(loan);

    }

    public String updatePaymentScheduleInAuto(String loanStr){
        if(StringUtils.isEmpty(loanStr)){
            logger.error("Loan info is Null in Payment!");
            return "Update Loan Failed!";
        }

        JSONObject loanObj = JSONObject.parseObject(loanStr);
//        List<Loan> loans = loanRepository.findByCustomerInAutoIdOrderByUpdateTimeDesc(loanObj.getInteger("customerInOnlineId"));
//        Loan loan = loans.get(0);

        Loan loan = loanRepository.findByContractNo(loanObj.getString("contractNo"));
        if (loan!=null) {

            JSONObject bankObj = loanObj.getJSONObject("bank");
            JSONObject paymentObj = loanObj.getJSONObject("payment");

            Payment payment = paymentRepository.findByLoanId(loan.getId());
            Bank bank = bankRepository.findByLoanId(loan.getId());

            payment.setTotalAmount(paymentObj.getDouble(JsonKeyConst.TOTAL_AMOUNT));
            payment.setTotalInterest(paymentObj.getDouble(JsonKeyConst.TOTAL_INTEREST));
            payment.setItems(paymentObj.getString("items"));
            payment.setEffectiveDate(paymentObj.getString("effectiveDate"));
            payment.setAnnualPercentageRate(paymentObj.getDouble("annualPercentageRate"));
            payment.setFirstInstallmentDate(paymentObj.getLong("firstInstallmentDate"));
            payment.setLastInstallmentDate(paymentObj.getLong("lastInstallmentDate"));
            paymentRepository.save(payment);

            String contractNo = loan.getContractNo();
            Loan loanDetail=getLoanByLoanId(loan.getId());
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("contractNo",contractNo);
            jsonObject.put("appData", JSON.toJSONString(loanDetail));
            loanRepository.save(loan);
        }else{
            logger.error("Update Loan Payment Info From Auto Failed");
        }
        return JSONObject.toJSONString(loan);
    }

    public String loanStausIsInitalized(String contractNo){
        String result = "";
        Loan loan = loanRepository.findByContractNo(contractNo);
        if (loan!=null){
            if ((loan.getLoanStatus() & LoanStatusEnum.INITIALIZED.getValue())==LoanStatusEnum.INITIALIZED.getValue() ||
                    (loan.getLoanStatus() & LoanStatusEnum.POSITIVE.getValue())==LoanStatusEnum.POSITIVE.getValue()
            ){
                result = "true";
            }else{
                result = loan.getLoanStatusText();
            }
        }else {
            logger.error("Loan Status In Online is Unknown!");
        }
        if (StringUtils.isEmpty(result)){
            result = "false";
        }

        return result;
    }

    public String getPortfolioById(Integer id) {
        String portfolioStr = runtimeFeignClient.getPortfolioParameter(id);
        return portfolioStr;
    }

    public String generateOnlineNewLoan(String operatorNo, Integer loanStatus) {
        String contractNo = "";
        String agentLevelObj = employeeFeignClient.getAgentLevel(operatorNo);
        String operatorName = "";
        int lockedMaxNumber = 2;
        Integer portfolioId = JSONObject.parseObject(agentLevelObj).getInteger("portfolioId");
        Integer level = JSONObject.parseObject(agentLevelObj).getInteger("level");
        operatorName = JSONObject.parseObject(agentLevelObj).getString("employeeFullName");
        if (LoanStatusEnum.UNDERWRITER_REVIEW.getValue().equals(loanStatus) ||
                LoanStatusEnum.TRIBE_REVIEW.getValue().equals(loanStatus)) {

            List<Integer> loanStatusList = new ArrayList<>();
            loanStatusList.add(loanStatus);
            List<Loan> lockedLoans = getLockedLoans(portfolioId, operatorNo, loanStatusList);
            List<Loan> lockedOnlineLoans = new ArrayList<>();
            for (Loan loan: lockedLoans){
                if (loan.getFlags() != null && loan.getFlags() == 10) {
                    lockedOnlineLoans.add(loan);
                }
            }
            if (lockedOnlineLoans.size()>0){
                sortLoanByLockedTime(lockedOnlineLoans);
                contractNo = lockedOnlineLoans.get(0).getContractNo();
            }else{
                List<Loan> newOnlineloans = loanRepository.findNewOnlineApplications(10, loanStatusList, operatorNo);
                if (newOnlineloans.size()>0){
                    contractNo = newOnlineloans.get(0).getContractNo();
                }else{
                    logger.error("there were not enough loan of new online loans!");
                    contractNo = "there were not enough loan of new online loans!";
                }
            }
        } else {
            logger.error("get new loan of online failed!");
            contractNo = "get new loan of online failed!";
        }

        Loan newLoan=loanRepository.findByContractNo(contractNo);
        if (newLoan!=null){
            String oldOperatorNo = newLoan.getLockedOperatorNo();
            newLoan.setLockedAt(DateUtil.getCurrentTimestamp());
            newLoan.setLockedOperatorName(operatorName);
            newLoan.setLockedOperatorNo(operatorNo);
            newLoan.setOperatorNo(operatorNo);
            newLoan.setFollowUp(null);
            newLoan.setUpdateTime(DateUtil.getCurrentTimestamp());
            loanRepository.save(newLoan);

            if (oldOperatorNo == null ||!operatorNo.equals(oldOperatorNo)){
                JSONObject additionObj = new JSONObject();
                additionObj.put("operatorNo", operatorNo);
                additionObj.put("operatorName", operatorName);
                timeLineApiService.addLockOrUnlockOrGrabLockTimeLine(contractNo, JSONObject.toJSONString(additionObj), "Lock Operation");
            }
        }
        return contractNo;
    }
}
