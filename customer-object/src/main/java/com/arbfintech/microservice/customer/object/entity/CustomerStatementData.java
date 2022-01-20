package com.arbfintech.microservice.customer.object.entity;

import com.arbfintech.framework.component.core.annotation.Column;
import com.arbfintech.framework.component.core.annotation.Entity;
import com.arbfintech.framework.component.core.annotation.Id;

import java.math.BigDecimal;

@Entity
public class CustomerStatementData {

  @Id
  @Column
  private Long id;

  @Column
  private Long customerId;

  @Column
  private Long payrollDate;

  @Column
  private Integer payrollType;

  @Column
  private BigDecimal amount;

  @Column
  private BigDecimal balance;


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

  public Long getPayrollDate() {
    return payrollDate;
  }

  public void setPayrollDate(Long payrollDate) {
    this.payrollDate = payrollDate;
  }

  public Integer getPayrollType() {
    return payrollType;
  }

  public void setPayrollType(Integer payrollType) {
    this.payrollType = payrollType;
  }

  public java.math.BigDecimal getAmount() {
    return amount;
  }

  public void setAmount(java.math.BigDecimal amount) {
    this.amount = amount;
  }

  public java.math.BigDecimal getBalance() {
    return balance;
  }

  public void setBalance(java.math.BigDecimal balance) {
    this.balance = balance;
  }
}
