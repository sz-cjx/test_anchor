package com.arbfintech.microservice.loan.entity;

import com.alibaba.fastjson.JSON;

import java.util.Date;

/**
 * @author Wade He
 */
public class LoanOverView {

	private int loanId;

	private String contractNo;

	private Long createTime;

	private Long updateTime;

	private String lockedOperatorNo;

	private String lockedOperatorName;

	private Long lockTime;

	private String firstName;

	private String lastName;

	private double requestPrincipal;

	private String state;

	public int getLoanId() {
		return loanId;
	}

	public void setLoanId(int loanId) {
		this.loanId = loanId;
	}

	public String getContractNo() {
		return contractNo;
	}

	public void setContractNo(String contractNo) {
		this.contractNo = contractNo;
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

	public Long getLockTime() {
		return lockTime;
	}

	public void setLockTime(Long lockTime) {
		this.lockTime = lockTime;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public double getRequestPrincipal() {
		return requestPrincipal;
	}

	public void setRequestPrincipal(double requestPrincipal) {
		this.requestPrincipal = requestPrincipal;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}

	
}
