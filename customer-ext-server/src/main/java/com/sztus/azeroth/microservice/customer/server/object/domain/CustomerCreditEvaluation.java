package com.sztus.azeroth.microservice.customer.server.object.domain;

import java.math.BigDecimal;

import com.sztus.framework.component.database.annotation.Column;
import com.sztus.framework.component.database.annotation.Entity;
import com.sztus.framework.component.database.annotation.Id;

@Entity
public class CustomerCreditEvaluation {

    @Id
    @Column
    private Long customerId;

    @Column
    private Integer creditPoint;

    @Column
    private BigDecimal maximumCreditAmount;

    @Column
    private Long updatedAt;

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

    public Long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Long updatedAt) {
        this.updatedAt = updatedAt;
    }
}
