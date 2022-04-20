package com.sztus.dalaran.microservice.customer.server.domain;

import com.sztus.framework.component.database.annotation.Column;
import com.sztus.framework.component.database.annotation.Entity;
import com.sztus.framework.component.database.annotation.GeneratedValue;
import com.sztus.framework.component.database.annotation.Id;
import com.sztus.framework.component.database.enumerate.GenerationType;

@Entity
public class CustomerContactData {

  @Id
  @Column
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column
  private Long customerId;

  @Column
  private Integer contactType;

  @Column
  private String contactInformation;

  @Column
  private Integer verifiedStatus;

  @Column
  private Long verifiedAt;

  @Column
  private Integer primary;

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

  public Integer getContactType() {
    return contactType;
  }

  public void setContactType(Integer contactType) {
    this.contactType = contactType;
  }

  public String getContactInformation() {
    return contactInformation;
  }

  public void setContactInformation(String contactInformation) {
    this.contactInformation = contactInformation;
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

  public Integer getPrimary() {
    return primary;
  }

  public void setPrimary(Integer primary) {
    this.primary = primary;
  }
}
