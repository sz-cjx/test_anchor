package com.sztus.microservice.customer.server.domain;

import com.sztus.framework.component.core.annotation.Column;
import com.sztus.framework.component.core.annotation.Entity;
import com.sztus.framework.component.core.annotation.GeneratedValue;
import com.sztus.framework.component.core.annotation.Id;
import com.sztus.framework.component.core.enumerate.GenerationType;

@Entity
public class CustomerBankData {

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
  private Long accountType;

  @Column
  private String routingNo;

  @Column
  private String accountNo;

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

  public Long getAccountType() {
    return accountType;
  }

  public void setAccountType(Long accountType) {
    this.accountType = accountType;
  }

  public String getRoutingNo() {
    return routingNo;
  }

  public void setRoutingNo(String routingNo) {
    this.routingNo = routingNo;
  }

  public String getAccountNo() {
    return accountNo;
  }

  public void setAccountNo(String accountNo) {
    this.accountNo = accountNo;
  }
}
