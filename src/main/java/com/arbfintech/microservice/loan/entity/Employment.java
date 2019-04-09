package com.arbfintech.microservice.loan.entity;

import com.alibaba.fastjson.JSON;

import javax.persistence.*;

/**
 * @author Wade He
 */
@Entity
@Table(name="loan_employment")
public class Employment {

	@Id
	@Column(length = 10)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	
	@Column(length = 10)
	private Integer loanId;

	@Column(length = 32)
	private String incomeType;

	@Column(length = 10)
	private Integer payrollType;

	@Column(length = 32)
	private String employerName;

	@Column(length = 32)
	private String employerAddress1;

	@Column(length = 32)
	private String employerAddress2;

	@Column(length = 32)
	private String employerAddress;

	@Column(length = 32)
	private String employerCity;

	@Column(length = 32)
	private String employerState;

	@Column(length = 32)
	private String employerZip;

	@Column(length = 32)
	private String employerPhone;

	@Column(length = 10)
	private String employerPhoneExt;

	@Column(length = 16)
	private Long hireDate;

	@Column(length = 10)
	private String employerType;

	@Column(length = 32)
	private String position;

	@Column(length = 10)
	private Integer payrollFrequency;

	@Column(length = 16)
	private Long lastPayDate;

	@Column(length = 16)
	private Long firstPayDate;

	@Column(length = 16)
	private Long secondPayDate;

	@Column(length = 32)
	private String workShift;

	@Column(length = 32)
	private String workStartTime;

	@Column(length = 32)
	private String workEndTime;

	@Column(length = 32)
	private String supervisorName;

	@Column(length = 32)
	private String supervisorPhone;

	@Column(length = 32)
	private String verifiedWay;

	@Column(length = 32)
	private String employmentSessionId;

	@Column(length = 32)
	private String employmentStatus;

	@Column(length = 32)
	private Boolean employerPhoneDoNotCall;

	@Column(length = 32)
	private Boolean employerPhoneUnContactable;

	@Column(length = 32)
	private Boolean supervisorPhoneDoNotCall;

	@Column(length = 32)
	private Boolean supervisorPhoneUnContactable;

	@Column(length = 6)
	private Integer paydayMoveDirection;

	@Column(length = 10)
	private String firstPayday;

	@Column(length = 10)
	private String secondPayday;

	@Column(length = 10)
	private String monthlyPayday;

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

	public String getIncomeType() {
		return incomeType;
	}

	public void setIncomeType(String incomeType) {
		this.incomeType = incomeType;
	}

	public Integer getPayrollType() {
		return payrollType;
	}

	public void setPayrollType(Integer payrollType) {
		this.payrollType = payrollType;
	}

	public String getEmployerName() {
		return employerName;
	}

	public void setEmployerName(String employerName) {
		this.employerName = employerName;
	}

	public String getEmployerAddress1() {
		return employerAddress1;
	}

	public void setEmployerAddress1(String employerAddress1) {
		this.employerAddress1 = employerAddress1;
	}

	public String getEmployerAddress2() {
		return employerAddress2;
	}

	public void setEmployerAddress2(String employerAddress2) {
		this.employerAddress2 = employerAddress2;
	}

	public String getEmployerAddress() {
		return employerAddress;
	}

	public void setEmployerAddress(String employerAddress) {
		this.employerAddress = employerAddress;
	}

	public String getEmployerCity() {
		return employerCity;
	}

	public void setEmployerCity(String employerCity) {
		this.employerCity = employerCity;
	}

	public String getEmployerState() {
		return employerState;
	}

	public void setEmployerState(String employerState) {
		this.employerState = employerState;
	}

	public String getEmployerZip() {
		return employerZip;
	}

	public void setEmployerZip(String employerZip) {
		this.employerZip = employerZip;
	}

	public String getEmployerPhone() {
		return employerPhone;
	}

	public void setEmployerPhone(String employerPhone) {
		this.employerPhone = employerPhone;
	}

	public String getEmployerPhoneExt() {
		return employerPhoneExt;
	}

	public void setEmployerPhoneExt(String employerPhoneExt) {
		this.employerPhoneExt = employerPhoneExt;
	}

	public Long getHireDate() {
		return hireDate;
	}

	public void setHireDate(Long hireDate) {
		this.hireDate = hireDate;
	}

	public String getEmployerType() {
		return employerType;
	}

	public void setEmployerType(String employerType) {
		this.employerType = employerType;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public Integer getPayrollFrequency() {
		return payrollFrequency;
	}

	public void setPayrollFrequency(Integer payrollFrequency) {
		this.payrollFrequency = payrollFrequency;
	}

	public Long getLastPayDate() {
		return lastPayDate;
	}

	public void setLastPayDate(Long lastPayDate) {
		this.lastPayDate = lastPayDate;
	}

	public Long getFirstPayDate() {
		return firstPayDate;
	}

	public void setFirstPayDate(Long firstPayDate) {
		this.firstPayDate = firstPayDate;
	}

	public Long getSecondPayDate() {
		return secondPayDate;
	}

	public void setSecondPayDate(Long secondPayDate) {
		this.secondPayDate = secondPayDate;
	}

	public String getWorkShift() {
		return workShift;
	}

	public void setWorkShift(String workShift) {
		this.workShift = workShift;
	}

	public String getWorkStartTime() {
		return workStartTime;
	}

	public void setWorkStartTime(String workStartTime) {
		this.workStartTime = workStartTime;
	}

	public String getWorkEndTime() {
		return workEndTime;
	}

	public void setWorkEndTime(String workEndTime) {
		this.workEndTime = workEndTime;
	}

	public String getSupervisorName() {
		return supervisorName;
	}

	public void setSupervisorName(String supervisorName) {
		this.supervisorName = supervisorName;
	}

	public String getSupervisorPhone() {
		return supervisorPhone;
	}

	public void setSupervisorPhone(String supervisorPhone) {
		this.supervisorPhone = supervisorPhone;
	}

	public String getVerifiedWay() {
		return verifiedWay;
	}

	public void setVerifiedWay(String verifiedWay) {
		this.verifiedWay = verifiedWay;
	}

	public String getEmploymentSessionId() {
		return employmentSessionId;
	}

	public void setEmploymentSessionId(String employmentSessionId) {
		this.employmentSessionId = employmentSessionId;
	}

	public String getEmploymentStatus() {
		return employmentStatus;
	}

	public void setEmploymentStatus(String employmentStatus) {
		this.employmentStatus = employmentStatus;
	}

	public Boolean getEmployerPhoneDoNotCall() {
		return employerPhoneDoNotCall;
	}

	public void setEmployerPhoneDoNotCall(Boolean employerPhoneDoNotCall) {
		this.employerPhoneDoNotCall = employerPhoneDoNotCall;
	}

	public Boolean getEmployerPhoneUnContactable() {
		return employerPhoneUnContactable;
	}

	public void setEmployerPhoneUnContactable(Boolean employerPhoneUnContactable) {
		this.employerPhoneUnContactable = employerPhoneUnContactable;
	}

	public Boolean getSupervisorPhoneDoNotCall() {
		return supervisorPhoneDoNotCall;
	}

	public void setSupervisorPhoneDoNotCall(Boolean supervisorPhoneDoNotCall) {
		this.supervisorPhoneDoNotCall = supervisorPhoneDoNotCall;
	}

	public Boolean getSupervisorPhoneUnContactable() {
		return supervisorPhoneUnContactable;
	}

	public void setSupervisorPhoneUnContactable(Boolean supervisorPhoneUnContactable) {
		this.supervisorPhoneUnContactable = supervisorPhoneUnContactable;
	}

	public Integer getPaydayMoveDirection() {
		return paydayMoveDirection;
	}

	public void setPaydayMoveDirection(Integer paydayMoveDirection) {
		this.paydayMoveDirection = paydayMoveDirection;
	}

	public String getFirstPayday() {
		return firstPayday;
	}

	public void setFirstPayday(String firstPayday) {
		this.firstPayday = firstPayday;
	}

	public String getSecondPayday() {
		return secondPayday;
	}

	public void setSecondPayday(String secondPayday) {
		this.secondPayday = secondPayday;
	}

	public String getMonthlyPayday() {
		return monthlyPayday;
	}

	public void setMonthlyPayday(String monthlyPayday) {
		this.monthlyPayday = monthlyPayday;
	}

	@Override
    public String toString() {
        return JSON.toJSONString(this);
    }

	
}
