package com.arbfintech.microservice.loan.entity;

import com.alibaba.fastjson.JSON;

import java.io.Serializable;

public class LoanProperty implements Serializable{

	private static final long serialVersionUID = 1L;

	private Integer loanId;
	private String sectionName;
	private String fieldName;
	private String fieldValue;
    private String originFieldValue;
	private String fieldKey;

	public Integer getLoanId() {
		return loanId;
	}

	public void setLoanId(Integer loanId) {
		this.loanId = loanId;
	}

	public String getSectionName() {
		return sectionName;
	}

	public void setSectionName(String sectionName) {
		this.sectionName = sectionName;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getFieldValue() {
		return fieldValue;
	}

	public void setFieldValue(String fieldValue) {
		this.fieldValue = fieldValue;
	}

	public String getOriginFieldValue() {
		return originFieldValue;
	}

	public void setOriginFieldValue(String originFieldValue) {
		this.originFieldValue = originFieldValue;
	}

	public String getFieldKey() {
		return fieldKey;
	}

	public void setFieldKey(String fieldKey) {
		this.fieldKey = fieldKey;
	}

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}
}
