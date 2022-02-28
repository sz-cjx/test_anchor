package com.arbfintech.microservice.customer.object.entity;

import com.arbfintech.framework.component.core.annotation.Column;
import com.arbfintech.framework.component.core.annotation.Entity;
import com.arbfintech.framework.component.core.annotation.Id;

import java.math.BigDecimal;

@Entity
public class CustomerEmploymentData {

    @Id
    @Column
    private Long id;

    @Column
    private String employerName;

    @Column
    private String employerPhone;

    @Column
    private String employerAddress;

    @Column
    private String employerCity;

    @Column
    private String employerState;

    @Column
    private String employerZip;

    @Column
    private Integer jobType;

    @Column
    private String jobTitle;

    @Column
    private String supervisorName;

    @Column
    private String supervisorPhone;

    @Column
    private Integer incomeType;

    @Column
    private Integer payrollType;

    @Column
    private Integer payrollFrequency;

    @Column
    private Long lastPayday;

    @Column
    private Integer paydayOnHoliday;

    @Column
    private Integer paydayOnAvailable;

    @Column
    private Integer firstDayOfMonth;

    @Column
    private Integer secondDayOfMonth;

    @Column
    private Integer firstDayOfWeek;

    @Column
    private Integer secondDayOfWeek;

    @Column
    private Integer voe;

    @Column
    private BigDecimal monthlyIncome;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmployerName() {
        return employerName;
    }

    public void setEmployerName(String employerName) {
        this.employerName = employerName;
    }

    public String getEmployerPhone() {
        return employerPhone;
    }

    public void setEmployerPhone(String employerPhone) {
        this.employerPhone = employerPhone;
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

    public Integer getJobType() {
        return jobType;
    }

    public void setJobType(Integer jobType) {
        this.jobType = jobType;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
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

    public Integer getIncomeType() {
        return incomeType;
    }

    public void setIncomeType(Integer incomeType) {
        this.incomeType = incomeType;
    }

    public Integer getPayrollType() {
        return payrollType;
    }

    public void setPayrollType(Integer payrollType) {
        this.payrollType = payrollType;
    }

    public Integer getPayrollFrequency() {
        return payrollFrequency;
    }

    public void setPayrollFrequency(Integer payrollFrequency) {
        this.payrollFrequency = payrollFrequency;
    }

    public Long getLastPayday() {
        return lastPayday;
    }

    public void setLastPayday(Long lastPayday) {
        this.lastPayday = lastPayday;
    }

    public Integer getPaydayOnHoliday() {
        return paydayOnHoliday;
    }

    public void setPaydayOnHoliday(Integer paydayOnHoliday) {
        this.paydayOnHoliday = paydayOnHoliday;
    }

    public Integer getPaydayOnAvailable() {
        return paydayOnAvailable;
    }

    public void setPaydayOnAvailable(Integer paydayOnAvailable) {
        this.paydayOnAvailable = paydayOnAvailable;
    }

    public Integer getFirstDayOfMonth() {
        return firstDayOfMonth;
    }

    public void setFirstDayOfMonth(Integer firstDayOfMonth) {
        this.firstDayOfMonth = firstDayOfMonth;
    }

    public Integer getSecondDayOfMonth() {
        return secondDayOfMonth;
    }

    public void setSecondDayOfMonth(Integer secondDayOfMonth) {
        this.secondDayOfMonth = secondDayOfMonth;
    }

    public Integer getFirstDayOfWeek() {
        return firstDayOfWeek;
    }

    public void setFirstDayOfWeek(Integer firstDayOfWeek) {
        this.firstDayOfWeek = firstDayOfWeek;
    }

    public Integer getSecondDayOfWeek() {
        return secondDayOfWeek;
    }

    public void setSecondDayOfWeek(Integer secondDayOfWeek) {
        this.secondDayOfWeek = secondDayOfWeek;
    }

    public Integer getVoe() {
        return voe;
    }

    public void setVoe(Integer voe) {
        this.voe = voe;
    }

    public BigDecimal getMonthlyIncome() {
        return monthlyIncome;
    }

    public void setMonthlyIncome(BigDecimal monthlyIncome) {
        this.monthlyIncome = monthlyIncome;
    }
}
