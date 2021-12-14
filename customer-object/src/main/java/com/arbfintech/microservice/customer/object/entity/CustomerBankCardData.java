package com.arbfintech.microservice.customer.object.entity;

import com.arbfintech.framework.component.core.annotation.Column;
import com.arbfintech.framework.component.core.annotation.Entity;
import com.arbfintech.framework.component.core.annotation.Id;

@Entity
public class CustomerBankCardData {

  @Id
  @Column
  private Long id;

  @Column
  private Long customerId;

  @Column
  private String cardNo;

  @Column
  private String note;

  @Column
  private Long addedAt;

  @Column
  private Long expiredAt;

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

  public String getCardNo() {
    return cardNo;
  }

  public void setCardNo(String cardNo) {
    this.cardNo = cardNo;
  }

  public String getNote() {
    return note;
  }

  public void setNote(String note) {
    this.note = note;
  }

  public Long getAddedAt() {
    return addedAt;
  }

  public void setAddedAt(Long addedAt) {
    this.addedAt = addedAt;
  }

  public Long getExpiredAt() {
    return expiredAt;
  }

  public void setExpiredAt(Long expiredAt) {
    this.expiredAt = expiredAt;
  }
}
