package com.sztus.azeroth.microservice.customer.server.object.domain;

import com.sztus.framework.component.database.annotation.Column;
import com.sztus.framework.component.database.annotation.Entity;
import com.sztus.framework.component.database.annotation.Id;

@Entity
public class CustomerEmploymentInfo {

  @Id
  @Column
  private Long customerId;

  @Column
  private String employerName;

  @Column
  private String employerEmail;

  @Column
  private String employerPhone;

  @Column
  private String employerFax;

  @Column
  private String employerCity;

  @Column
  private String employerState;

  @Column
  private String employerZip;

  @Column
  private String employerAddress;

  @Column
  private Integer jobType;

  @Column
  private String jobTitle;

  @Column
  private String supervisorName;

  @Column
  private String supervisorPhone;

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
