package com.arbfintech.microservice.customer.domain.entity;

import com.arbfintech.framework.component.core.annotation.Column;
import com.arbfintech.framework.component.core.annotation.Entity;
import com.arbfintech.framework.component.core.annotation.Id;

@Entity
public class CustomerOptInData {

    @Id
    @Column
    private Long id;

    @Column
    private Long customerId;

    @Column
    private Integer optInType;

    @Column
    private Integer optInValue;

    @Column
    private Long createdAt;

    @Column
    private Long updatedAt;

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

    public Integer getOptInType() {
        return optInType;
    }

    public void setOptInType(Integer optInType) {
        this.optInType = optInType;
    }

    public Integer getOptInValue() {
        return optInValue;
    }

    public void setOptInValue(Integer optInValue) {
        this.optInValue = optInValue;
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
