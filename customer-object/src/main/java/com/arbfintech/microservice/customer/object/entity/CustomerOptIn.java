package com.arbfintech.microservice.customer.object.entity;

import com.arbfintech.framework.component.core.annotation.Column;
import com.arbfintech.framework.component.core.annotation.Entity;
import com.arbfintech.framework.component.core.annotation.Id;

@Entity
public class CustomerOptIn {

  @Id
  @Column
  private Long customerId;

  @Column
  private Integer optInType;

  @Column
  private Integer optInStatus;

  @Column
  private Long createdAt;

  @Column
  private Long updatedAt;

  public CustomerOptIn() {
  }

  public CustomerOptIn(Long customerId, Integer optInType, Integer optInStatus, Long createdAt, Long updatedAt) {
    this.customerId = customerId;
    this.optInType = optInType;
    this.optInStatus = optInStatus;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
  }

  public Long getCustomerId() {
    return customerId;
  }

  public void setCustomerId(Long customerId) {
    this.customerId = customerId;
  }

  public Integer getOptInType() {
    return optInType;
  }

  public void setOptInType(Integer optInType) {
    this.optInType = optInType;
  }

  public Integer getOptInStatus() {
    return optInStatus;
  }

  public void setOptInStatus(Integer optInStatus) {
    this.optInStatus = optInStatus;
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
