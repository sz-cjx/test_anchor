package com.arbfintech.microservice.loan.entity;

import com.alibaba.fastjson.JSON;

import javax.persistence.*;

/**
 * @author Wade He
 */
@Entity
@Table(name="loan_payment")
public class Payment {

	@Id
	@Column(length = 10)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@Column(length = 10)
	private Integer loanId;

	@Column(length = 32)
	private String paymentSessionId;

	@Column(length = 32)
	private String program;

	@Column(length = 32)
	private Integer transactionMode;

	@Column(length = 32)
	private Double totalPrincipal;

	@Column(length = 32)
	private Integer expeditePayment;

	@Column(length = 32)
	private Boolean isWaiveAdditionalFee;

	@Column(length = 32)
	private Long effectiveDate;

	@Column(length = 32)
	private Long interestRate;

	@Column(length = 32)
	private Long firstInstallmentDate;

	@Column(length = 32)
	private Long lastInstallmentDate;

	@Column(length = 32)
	private Double annualPercentageRate;

	@Column(length = 32)
	private Integer installmentCount;

	@Column(length = 32)
	private Double regularAmount;

	@Column(length = 32)
	private Double totalInterest;

	@Column(length = 32)
	private Double totalAmount;

	@Column
	@Lob
	private String items;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getLoanId() {
		return loanId;
	}

	public void setLoanId(Integer loanId) {
		this.loanId = loanId;
	}

	public String getPaymentSessionId() {
		return paymentSessionId;
	}

	public void setPaymentSessionId(String paymentSessionId) {
		this.paymentSessionId = paymentSessionId;
	}

	public String getProgram() {
		return program;
	}

	public void setProgram(String program) {
		this.program = program;
	}

	public Integer getTransactionMode() {
		return transactionMode;
	}

	public void setTransactionMode(Integer transactionMode) {
		this.transactionMode = transactionMode;
	}

	public Double getTotalPrincipal() {
		return totalPrincipal;
	}

	public void setTotalPrincipal(Double totalPrincipal) {
		this.totalPrincipal = totalPrincipal;
	}

	public Integer getExpeditePayment() {
		return expeditePayment;
	}

	public void setExpeditePayment(Integer expeditePayment) {
		this.expeditePayment = expeditePayment;
	}

	public Boolean getWaiveAdditionalFee() {
		return isWaiveAdditionalFee;
	}

	public void setWaiveAdditionalFee(Boolean waiveAdditionalFee) {
		isWaiveAdditionalFee = waiveAdditionalFee;
	}

	public Long getEffectiveDate() {
		return effectiveDate;
	}

	public void setEffectiveDate(Long effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	public Long getInterestRate() {
		return interestRate;
	}

	public void setInterestRate(Long interestRate) {
		this.interestRate = interestRate;
	}

	public Long getFirstInstallmentDate() {
		return firstInstallmentDate;
	}

	public void setFirstInstallmentDate(Long firstInstallmentDate) {
		this.firstInstallmentDate = firstInstallmentDate;
	}

	public Long getLastInstallmentDate() {
		return lastInstallmentDate;
	}

	public void setLastInstallmentDate(Long lastInstallmentDate) {
		this.lastInstallmentDate = lastInstallmentDate;
	}

	public Double getAnnualPercentageRate() {
		return annualPercentageRate;
	}

	public void setAnnualPercentageRate(Double annualPercentageRate) {
		this.annualPercentageRate = annualPercentageRate;
	}

	public Integer getInstallmentCount() {
		return installmentCount;
	}

	public void setInstallmentCount(Integer installmentCount) {
		this.installmentCount = installmentCount;
	}

	public Double getRegularAmount() {
		return regularAmount;
	}

	public void setRegularAmount(Double regularAmount) {
		this.regularAmount = regularAmount;
	}

	public Double getTotalInterest() {
		return totalInterest;
	}

	public void setTotalInterest(Double totalInterest) {
		this.totalInterest = totalInterest;
	}

	public Double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getItems() {
		return items;
	}

	public void setItems(String items) {
		this.items = items;
	}

	@Override
    public String toString() {
        return JSON.toJSONString(this);
    }

	
}
