package com.arbfintech.microservice.loan.entity;

import com.alibaba.fastjson.JSON;

/**
 * @author Wade He
 */

public class LoanItem {
	private String contractNo;
	private Long installmentDate;
	private Integer transactionType;
	private Integer transactionMode;
	private Double unpaidFee;
	private Double additionalFee;
	private Double interest;
	private Double principal;
	private Double amount;

	public String getContractNo() {
		return contractNo;
	}

	public void setContractNo(String contractNo) {
		this.contractNo = contractNo;
	}

	public Long getInstallmentDate() {
		return installmentDate;
	}

	public void setInstallmentDate(Long installmentDate) {
		this.installmentDate = installmentDate;
	}

	public Integer getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(Integer transactionType) {
		this.transactionType = transactionType;
	}

	public Integer getTransactionMode() {
		return transactionMode;
	}

	public void setTransactionMode(Integer transactionMode) {
		this.transactionMode = transactionMode;
	}

	public Double getUnpaidFee() {
		return unpaidFee;
	}

	public void setUnpaidFee(Double unpaidFee) {
		this.unpaidFee = unpaidFee;
	}

	public Double getAdditionalFee() {
		return additionalFee;
	}

	public void setAdditionalFee(Double additionalFee) {
		this.additionalFee = additionalFee;
	}

	public Double getInterest() {
		return interest;
	}

	public void setInterest(Double interest) {
		this.interest = interest;
	}

	public Double getPrincipal() {
		return principal;
	}

	public void setPrincipal(Double principal) {
		this.principal = principal;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}

}
