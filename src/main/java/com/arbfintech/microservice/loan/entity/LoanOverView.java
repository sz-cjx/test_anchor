package com.arbfintech.microservice.loan.entity;

import com.alibaba.fastjson.JSON;

import java.util.Date;

/**
 * @author Wade He
 */
public class LoanOverView {

	private int loanId;

	private String contractNo;

	private Date createTime;

	private Date updateTime;

	private String lockedOperatorNo;

	private String lockedOperatorName;

	private Date lockTime;

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

	public Date getLockTime() {
		return lockTime;
	}

	public void setLockTime(Date lockTime) {
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
