package com.arbfintech.microservice.customer.object.dto;

public class CustomerEmploymentDTO {

    private Long id;

    private String employerName;

    private String jobTitle;

    private Long jobType;

    private String employerState;

    private Long paydayOnHoliday;

    private Long voe;

    private String employerPhone;

    private String lastPayday;

    private Long payrollFrequency;

    private Long firstDayOfWeek;

    private Long firstDayOfMonth;

    private Long secondDayOfMonth;

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

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public Long getJobType() {
        return jobType;
    }

    public void setJobType(Long jobType) {
        this.jobType = jobType;
    }

    public String getEmployerState() {
        return employerState;
    }

    public void setEmployerState(String employerState) {
        this.employerState = employerState;
    }

    public Long getPaydayOnHoliday() {
        return paydayOnHoliday;
    }

    public void setPaydayOnHoliday(Long paydayOnHoliday) {
        this.paydayOnHoliday = paydayOnHoliday;
    }

    public Long getVoe() {
        return voe;
    }

    public void setVoe(Long voe) {
        this.voe = voe;
    }

    public String getEmployerPhone() {
        return employerPhone;
    }

    public void setEmployerPhone(String employerPhone) {
        this.employerPhone = employerPhone;
    }

    public String getLastPayday() {
        return lastPayday;
    }

    public void setLastPayday(String lastPayday) {
        this.lastPayday = lastPayday;
    }

    public Long getPayrollFrequency() {
        return payrollFrequency;
    }

    public void setPayrollFrequency(Long payrollFrequency) {
        this.payrollFrequency = payrollFrequency;
    }

    public Long getFirstDayOfWeek() {
        return firstDayOfWeek;
    }

    public void setFirstDayOfWeek(Long firstDayOfWeek) {
        this.firstDayOfWeek = firstDayOfWeek;
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
}
