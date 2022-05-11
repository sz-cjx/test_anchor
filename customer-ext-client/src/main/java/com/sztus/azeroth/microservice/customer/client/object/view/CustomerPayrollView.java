package com.sztus.azeroth.microservice.customer.client.object.view;

import java.math.BigDecimal;

public class CustomerPayrollView {

    private Long customerId;
    private Integer payrollType;
    private Integer payrollFrequency;
    private Integer paydayOnHoliday;
    private Integer paydayOnAvailable;
    private Integer dayOfWeekPaid;
    private Integer firstDayOfMonth;
    private Integer secondDayOfMonth;
    private String lastPayday;
    private Integer incomeType;
    private BigDecimal monthlyIncome;

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

    public String getLastPayday() {
        return lastPayday;
    }

    public void setLastPayday(String lastPayday) {
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
}
