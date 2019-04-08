package com.arbfintech.microservice.loan.entity;
import com.alibaba.fastjson.JSON;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.Date;

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
	private Integer customerId;

	@Column(length = 32, unique = true)
	private String contractNo;

	@Column
	@Temporal(TemporalType.TIMESTAMP)
	@CreationTimestamp
	private Date createTime;

	@Column
	@Temporal(TemporalType.TIMESTAMP)
	@UpdateTimestamp
	private Date updateTime;

	@Column(length = 32)
	private String lockedOperatorNo;

	@Column(length = 32)
	private String lockedOperatorName;

	@Column
	@Temporal(TemporalType.TIMESTAMP)
	@UpdateTimestamp
	private Date lockedAt;

	@Column(length = 10)
	private Integer status;

	@Column(length = 10)
	private Integer category;

	@Column(length = 10)
	private Integer priority;

    @Column(length = 10)
    private Integer leadProviderId;

    @Column(length = 10)
    private Integer withdrawnCode;

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

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
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

	public Date getLockedAt() {
		return lockedAt;
	}

	public void setLockedAt(Date lockedAt) {
		this.lockedAt = lockedAt;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
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

	@Override
    public String toString() {
        return JSON.toJSONString(this);
    }

	
}
