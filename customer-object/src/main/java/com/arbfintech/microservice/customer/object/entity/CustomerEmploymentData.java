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
    private Long jobType;

    @Column
    private String jobTitle;

    @Column
    private String supervisorName;

    @Column
    private String supervisorPhone;

    @Column
    private Long incomeType;

    @Column
    private Long payrollType;

    @Column
    private Long payrollFrequency;

    @Column
    private Long lastPayday;

    @Column
    private Long paydayOnHoliday;

    @Column
    private Long paydayOnAvailable;

    @Column
    private Long firstDayOfMonth;

    @Column
    private Long secondDayOfMonth;

    @Column
    private Long firstDayOfWeek;

    @Column
    private Long secondDayOfWeek;

    @Column
    private Long voe;

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

    public Long getJobType() {
        return jobType;
    }

    public void setJobType(Long jobType) {
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

    public Long getIncomeType() {
        return incomeType;
    }

    public void setIncomeType(Long incomeType) {
        this.incomeType = incomeType;
    }

    public Long getPayrollType() {
        return payrollType;
    }

    public void setPayrollType(Long payrollType) {
        this.payrollType = payrollType;
    }

    public Long getPayrollFrequency() {
        return payrollFrequency;
    }

    public void setPayrollFrequency(Long payrollFrequency) {
        this.payrollFrequency = payrollFrequency;
    }

    public Long getLastPayday() {
        return lastPayday;
    }

    public void setLastPayday(Long lastPayday) {
        this.lastPayday = lastPayday;
    }

    public Long getPaydayOnHoliday() {
        return paydayOnHoliday;
    }

    public void setPaydayOnHoliday(Long paydayOnHoliday) {
        this.paydayOnHoliday = paydayOnHoliday;
    }

    public Long getPaydayOnAvailable() {
        return paydayOnAvailable;
    }

    public void setPaydayOnAvailable(Long paydayOnAvailable) {
        this.paydayOnAvailable = paydayOnAvailable;
    }

    public Long getFirstDayOfMonth() {
        return firstDayOfMonth;
    }

    public void setFirstDayOfMonth(Long firstDayOfMonth) {
        this.firstDayOfMonth = firstDayOfMonth;
    }

    public Long getSecondDayOfMonth() {
        return secondDayOfMonth;
    }

    public void setSecondDayOfMonth(Long secondDayOfMonth) {
        this.secondDayOfMonth = secondDayOfMonth;
    }

    public Long getFirstDayOfWeek() {
        return firstDayOfWeek;
    }

    public void setFirstDayOfWeek(Long firstDayOfWeek) {
        this.firstDayOfWeek = firstDayOfWeek;
    }

    public Long getSecondDayOfWeek() {
        return secondDayOfWeek;
    }

    public void setSecondDayOfWeek(Long secondDayOfWeek) {
        this.secondDayOfWeek = secondDayOfWeek;
    }

    public Long getVoe() {
        return voe;
    }

    public void setVoe(Long voe) {
        this.voe = voe;
    }

    public BigDecimal getMonthlyIncome() {
        return monthlyIncome;
    }

    public void setMonthlyIncome(BigDecimal monthlyIncome) {
        this.monthlyIncome = monthlyIncome;
    }
}
