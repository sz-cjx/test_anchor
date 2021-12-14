package com.arbfintech.microservice.customer.object.entity;

import com.arbfintech.framework.component.core.annotation.Column;
import com.arbfintech.framework.component.core.annotation.Entity;
import com.arbfintech.framework.component.core.annotation.Id;

@Entity
public class CustomerOperationLog {

  @Id
  @Column
  private Long id;

  @Column
  private Long customerId;

  @Column
  private Long logType;

  @Column
  private String logData;

  @Column
  private String original;

  @Column
  private String current;

  @Column
  private Long operatedAt;

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

  public Long getLogType() {
    return logType;
  }

  public void setLogType(Long logType) {
    this.logType = logType;
  }

  public String getLogData() {
    return logData;
  }

  public void setLogData(String logData) {
    this.logData = logData;
  }

  public String getOriginal() {
    return original;
  }

  public void setOriginal(String original) {
    this.original = original;
  }

  public String getCurrent() {
    return current;
  }

  public void setCurrent(String current) {
    this.current = current;
  }

  public Long getOperatedAt() {
    return operatedAt;
  }

  public void setOperatedAt(Long operatedAt) {
    this.operatedAt = operatedAt;
  }
}
