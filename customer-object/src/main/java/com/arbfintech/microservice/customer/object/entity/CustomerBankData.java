package com.arbfintech.microservice.customer.object.entity;

import com.arbfintech.framework.component.core.annotation.Column;
import com.arbfintech.framework.component.core.annotation.Entity;
import com.arbfintech.framework.component.core.annotation.Id;

@Entity
public class CustomerBankData {

  @Id
  @Column
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
