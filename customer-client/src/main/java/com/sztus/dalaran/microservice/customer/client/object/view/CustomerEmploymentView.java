package com.sztus.dalaran.microservice.customer.client.object.view;

public class CustomerEmploymentView {

    private Long customerId;

    private String employerName;

    private String employerEmail;

    private String employerPhone;

    private String employerFax;

    private Integer employerCity;

    private String employerState;

    private String employerZip;

    private String employerAddress;

    private Integer jobType;

    private String jobTitle;

    private String supervisorName;

    private String supervisorPhone;

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getEmployerName() {
        return employerName;
    }

    public void setEmployerName(String employerName) {
        this.employerName = employerName;
    }

    public String getEmployerEmail() {
        return employerEmail;
    }

    public void setEmployerEmail(String employerEmail) {
        this.employerEmail = employerEmail;
    }

    public String getEmployerPhone() {
        return employerPhone;
    }

    public void setEmployerPhone(String employerPhone) {
        this.employerPhone = employerPhone;
    }

    public String getEmployerFax() {
        return employerFax;
    }

    public void setEmployerFax(String employerFax) {
        this.employerFax = employerFax;
    }

    public Integer getEmployerCity() {
        return employerCity;
    }

    public void setEmployerCity(Integer employerCity) {
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

    public String getEmployerAddress() {
        return employerAddress;
    }

    public void setEmployerAddress(String employerAddress) {
        this.employerAddress = employerAddress;
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
}
