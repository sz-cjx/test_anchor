package com.arbfintech.microservice.customer.object.entity;

import com.arbfintech.framework.component.core.annotation.Column;
import com.arbfintech.framework.component.core.annotation.Entity;
import com.arbfintech.framework.component.core.annotation.Id;
import java.math.BigDecimal;

@Entity
public class CustomerCreditData {

  @Id
  @Column
  private Long id;

  @Column
  private Long customerId;

  @Column
  private Integer creditPoint;

  @Column
  private BigDecimal maximumCreditAmount;

  @Column
  private Long updateAt;

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

  public Integer getCreditPoint() {
    return creditPoint;
  }

  public void setCreditPoint(Integer creditPoint) {
    this.creditPoint = creditPoint;
  }

  public BigDecimal getMaximumCreditAmount() {
    return maximumCreditAmount;
  }

  public void setMaximumCreditAmount(BigDecimal maximumCreditAmount) {
    this.maximumCreditAmount = maximumCreditAmount;
  }

  public Long getUpdateAt() {
    return updateAt;
  }

  public void setUpdateAt(Long updateAt) {
    this.updateAt = updateAt;
  }
}
