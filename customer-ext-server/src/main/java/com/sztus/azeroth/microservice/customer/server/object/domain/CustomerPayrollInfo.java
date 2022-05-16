package com.sztus.azeroth.microservice.customer.server.object.domain;

import com.sztus.framework.component.database.annotation.Column;
import com.sztus.framework.component.database.annotation.Entity;
import com.sztus.framework.component.database.annotation.Id;

import java.math.BigDecimal;

@Entity
public class CustomerPayrollInfo {

    @Id
    @Column
    private Long customerId;

    @Column
    private Integer payrollType;

    @Column
    private Integer payrollFrequency;

    @Column
    private Integer paydayOnHoliday;

    @Column
    private Integer paydayOnAvailable;

    @Column
    private Integer dayOfWeekPaid;

    @Column
    private Integer firstDayOfMonth;

    @Column
    private Integer secondDayOfMonth;

    @Column
    private Long lastPayday;

    @Column
    private Integer incomeType;

    @Column
    private BigDecimal monthlyIncome;

    @Column
    private Long createdAt;

    @Column
    private Long updatedAt;

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
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

    public Integer getDayOfWeekPaid() {
        return dayOfWeekPaid;
    }

    public void setDayOfWeekPaid(Integer dayOfWeekPaid) {
        this.dayOfWeekPaid = dayOfWeekPaid;
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

    public Long getLastPayday() {
        return lastPayday;
    }

    public void setLastPayday(Long lastPayday) {
        this.lastPayday = lastPayday;
    }

    public Integer getIncomeType() {
        return incomeType;
    }

    public void setIncomeType(Integer incomeType) {
        this.incomeType = incomeType;
    }

    public BigDecimal getMonthlyIncome() {
        return monthlyIncome;
    }

    public void setMonthlyIncome(BigDecimal monthlyIncome) {
        this.monthlyIncome = monthlyIncome;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    public Long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Long updatedAt) {
        this.updatedAt = updatedAt;
    }
}
