package com.sztus.azeroth.microservice.customer.server.object.domain;

import com.sztus.framework.component.database.annotation.Column;
import com.sztus.framework.component.database.annotation.Entity;
import com.sztus.framework.component.database.annotation.Id;

@Entity
public class CustomerContactInfo {

  @Id
  @Column
  private Long customerId;

  @Id
  @Column
  private Integer type;

  @Column
  private String value;

  @Column
  private Integer verifiedStatus;

  @Column
  private Long verifiedAt;

  @Column
  private Integer optionCombination;

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

  public Integer getType() {
    return type;
  }

  public void setType(Integer type) {
    this.type = type;
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

  public Integer getOptionCombination() {
    return optionCombination;
  }

  public void setOptionCombination(Integer optionCombination) {
    this.optionCombination = optionCombination;
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
