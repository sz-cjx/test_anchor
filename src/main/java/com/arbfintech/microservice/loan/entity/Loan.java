package com.arbfintech.microservice.loan.entity;

import com.alibaba.fastjson.JSON;

import javax.persistence.*;

/**
 * @author Wade He
 */
@Entity
public class Loan {

    @Id
    @Column(length = 10)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(length = 10)
    private Integer portfolioId;

    @Column(length = 10)
    private Integer leadId;

    @Column(length = 10)
    private Integer leadStatus;

    @Column(length = 10)
    private Integer customerId;

    @Column(length = 32, unique = true)
    private String contractNo;

    @Column(length = 16)
    private Long receiveTime;

    @Column(length = 16)
    private Long createTime;

    @Column(length = 16)
    private Long updateTime;

    @Column(length = 32)
    private String lockedOperatorNo;

    @Column(length = 32)
    private String lockedOperatorName;

    @Column(length = 16)
    private Long lockedAt;

    @Column(length = 10)
    private Integer loanStatus;

    @Column(length = 64)
    private String loanStatusText;

    @Column(length = 10)
    private Integer category;

    @Column(length = 10)
    private Integer priority;

    @Column(length = 10)
    private Integer leadProviderId;

    @Column(length = 10)
    private Integer withdrawnCode;

    @Column
    private String customerIdentifyKey;

    @Column(length = 16)
    private Long followUp;

    @Column(length = 32)
    private String operatorNo;

    @Column(length = 10)
    private Integer flags;

    @Column(length = 10)
    private Integer customerInAutoId;

    @Column
    @Lob
    private String reviewData;

    @Column
    @Lob
    private String onlineData;

    @Transient
    private Personal personal;

    @Transient
    private Bank bank;

    @Transient
    private Employment employment;

    @Transient
    private Payment payment;

    @Transient
    private Document document;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPortfolioId() {
        return portfolioId;
    }

    public void setPortfolioId(Integer portfolioId) {
        this.portfolioId = portfolioId;
    }

    public Integer getLeadId() {
        return leadId;
    }

    public void setLeadId(Integer leadId) {
        this.leadId = leadId;
    }

    public Integer getLeadStatus() {
        return leadStatus;
    }

    public void setLeadStatus(Integer leadStatus) {
        this.leadStatus = leadStatus;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public String getContractNo() {
        return contractNo;
    }

    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
    }

    public Long getReceiveTime() {
        return receiveTime;
    }

    public void setReceiveTime(Long receiveTime) {
        this.receiveTime = receiveTime;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

    public String getLockedOperatorNo() {
        return lockedOperatorNo;
    }

    public void setLockedOperatorNo(String lockedOperatorNo) {
        this.lockedOperatorNo = lockedOperatorNo;
    }

    public String getLockedOperatorName() {
        return lockedOperatorName;
    }

    public void setLockedOperatorName(String lockedOperatorName) {
        this.lockedOperatorName = lockedOperatorName;
    }

    public Long getLockedAt() {
        return lockedAt;
    }

    public void setLockedAt(Long lockedAt) {
        this.lockedAt = lockedAt;
    }

    public Integer getLoanStatus() {
        return loanStatus;
    }

    public void setLoanStatus(Integer loanStatus) {
        this.loanStatus = loanStatus;
    }

    public String getLoanStatusText() {
        return loanStatusText;
    }

    public void setLoanStatusText(String loanStatusText) {
        this.loanStatusText = loanStatusText;
    }

    public Integer getCategory() {
        return category;
    }

    public void setCategory(Integer category) {
        this.category = category;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Integer getLeadProviderId() {
        return leadProviderId;
    }

    public void setLeadProviderId(Integer leadProviderId) {
        this.leadProviderId = leadProviderId;
    }

    public Integer getWithdrawnCode() {
        return withdrawnCode;
    }

    public String getCustomerIdentifyKey() {
        return customerIdentifyKey;
    }

    public void setCustomerIdentifyKey(String customerIdentifyKey) {
        this.customerIdentifyKey = customerIdentifyKey;
    }

    public void setWithdrawnCode(Integer withdrawnCode) {
        this.withdrawnCode = withdrawnCode;
    }

    public Personal getPersonal() {
        return personal;
    }

    public void setPersonal(Personal personal) {
        this.personal = personal;
    }

    public Bank getBank() {
        return bank;
    }

    public void setBank(Bank bank) {
        this.bank = bank;
    }

    public Employment getEmployment() {
        return employment;
    }

    public void setEmployment(Employment employment) {
        this.employment = employment;
    }

    public Long getFollowUp() {
        return followUp;
    }

    public void setFollowUp(Long followUp) {
        this.followUp = followUp;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public Document getDocument() {
        return document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }

    public String getOperatorNo() {
        return operatorNo;
    }

    public void setOperatorNo(String operatorNo) {
        this.operatorNo = operatorNo;
    }

    public Integer getFlags() {
        return flags;
    }

    public void setFlags(Integer flags) {
        this.flags = flags;
    }

    public String getReviewData() {
        return reviewData;
    }

    public void setReviewData(String reviewData) {
        this.reviewData = reviewData;
    }

    public Integer getCustomerInAutoId() {
        return customerInAutoId;
    }

    public void setCustomerInAutoId(Integer customerInAutoId) {
        this.customerInAutoId = customerInAutoId;
    }

    public String getOnlineData() {
        return onlineData;
    }

    public void setOnlineData(String onlineData) {
        this.onlineData = onlineData;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }


}
