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
  private Long optInType;

  @Column
  private Long optInStatus;

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

  public Long getOptInType() {
    return optInType;
  }

  public void setOptInType(Long optInType) {
    this.optInType = optInType;
  }

  public Long getOptInStatus() {
    return optInStatus;
  }

  public void setOptInStatus(Long optInStatus) {
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
