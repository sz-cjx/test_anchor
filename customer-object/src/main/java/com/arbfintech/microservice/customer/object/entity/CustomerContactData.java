package com.arbfintech.microservice.customer.object.entity;

import com.arbfintech.framework.component.core.annotation.Column;
import com.arbfintech.framework.component.core.annotation.Entity;
import com.arbfintech.framework.component.core.annotation.Id;

@Entity
public class CustomerContactData {

  @Id
  @Column
  private Long customerId;

  @Id
  @Column
  private Integer contactType;

  @Column
  private String value;

  @Column
  private Integer verifiedStatus;

  @Column
  private Long verifiedAt;

  public Long getCustomerId() {
    return customerId;
  }

  public void setCustomerId(Long customerId) {
    this.customerId = customerId;
  }

  public Integer getContactType() {
    return contactType;
  }

  public void setContactType(Integer contactType) {
    this.contactType = contactType;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public Integer getVerifiedStatus() {
    return verifiedStatus;
  }

  public void setVerifiedStatus(Integer verifiedStatus) {
    this.verifiedStatus = verifiedStatus;
  }

  public Long getVerifiedAt() {
    return verifiedAt;
  }

  public void setVerifiedAt(Long verifiedAt) {
    this.verifiedAt = verifiedAt;
  }
}
