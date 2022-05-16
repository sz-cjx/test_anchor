package com.sztus.azeroth.microservice.customer.server.object.domain;

import com.sztus.framework.component.database.annotation.Column;
import com.sztus.framework.component.database.annotation.Entity;
import com.sztus.framework.component.database.annotation.GeneratedValue;
import com.sztus.framework.component.database.annotation.Id;
import com.sztus.framework.component.database.enumerate.GenerationType;

@Entity
public class CustomerBankAccount {

  @Id
  @Column
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column
  private Long customerId;

  @Column
  private String bankName;

  @Column
  private String bankPhone;

  @Column
  private Integer bankAccountType;

  @Column
  private String bankRoutingNo;

  @Column
  private String bankAccountNo;

  @Column
  private Long createdAt;

  @Column
  private Long updatedAt;

  @Column
  private String note;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getCustomerId() {
    return customerId;
  }

  public void setCustomerId(Long customerId) {
    this.customerId = customerId;
  }

  public String getBankName() {
    return bankName;
  }

  public void setBankName(String bankName) {
    this.bankName = bankName;
  }

  public String getBankPhone() {
    return bankPhone;
  }

  public void setBankPhone(String bankPhone) {
    this.bankPhone = bankPhone;
  }

  public Integer getBankAccountType() {
    return bankAccountType;
  }

  public void setBankAccountType(Integer bankAccountType) {
    this.bankAccountType = bankAccountType;
  }

  public String getBankRoutingNo() {
    return bankRoutingNo;
  }

  public void setBankRoutingNo(String bankRoutingNo) {
    this.bankRoutingNo = bankRoutingNo;
  }

  public String getBankAccountNo() {
    return bankAccountNo;
  }

  public void setBankAccountNo(String bankAccountNo) {
    this.bankAccountNo = bankAccountNo;
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

  public String getNote() {
    return note;
  }

  public void setNote(String note) {
    this.note = note;
  }
}
