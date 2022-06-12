package com.sztus.azeroth.microservice.customer.server.object.domain;

import com.sztus.framework.component.database.annotation.Column;
import com.sztus.framework.component.database.annotation.Entity;
import com.sztus.framework.component.database.annotation.GeneratedValue;
import com.sztus.framework.component.database.annotation.Id;
import com.sztus.framework.component.database.enumerate.GenerationType;
import org.springframework.context.annotation.Primary;

@Entity
public class Customer {

  @Id
  @Column
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column
  private String openId;

  @Column
  private Integer status;

  @Column
  private Long createdAt;

  @Column
  private Long updatedAt;

  @Column
  private String uniqueCode;

  @Column
  private Integer category;

  public Integer getCategory() {
    return category;
  }

  public void setCategory(Integer category) {
    this.category = category;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getOpenId() {
    return openId;
  }

  public void setOpenId(String openId) {
    this.openId = openId;
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
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

  public String getUniqueCode() {
    return uniqueCode;
  }

  public void setUniqueCode(String uniqueCode) {
    this.uniqueCode = uniqueCode;
  }
}
