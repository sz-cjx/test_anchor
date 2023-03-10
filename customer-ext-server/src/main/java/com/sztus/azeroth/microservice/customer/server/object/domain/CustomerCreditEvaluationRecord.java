package com.sztus.azeroth.microservice.customer.server.object.domain;

import com.sztus.framework.component.database.annotation.Column;
import com.sztus.framework.component.database.annotation.Entity;
import com.sztus.framework.component.database.annotation.Id;

import java.math.BigDecimal;

@Entity
public class CustomerCreditEvaluationRecord {

    @Id
    @Column
    private Long id;

    @Column
    private Long customerId;

    @Column
    private Long portfolioId;

    @Column
    private Integer creditPoint;

    @Column
    private BigDecimal creditAmount;

    @Column
    private Long evaluatedAt;

    @Column
    private Long createdAt;

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

    public Long getPortfolioId() {
        return portfolioId;
    }

    public void setPortfolioId(Long portfolioId) {
        this.portfolioId = portfolioId;
    }

    public Integer getCreditPoint() {
        return creditPoint;
    }

    public void setCreditPoint(Integer creditPoint) {
        this.creditPoint = creditPoint;
    }

    public BigDecimal getCreditAmount() {
        return creditAmount;
    }

    public void setCreditAmount(BigDecimal creditAmount) {
        this.creditAmount = creditAmount;
    }

    public Long getEvaluatedAt() {
        return evaluatedAt;
    }

    public void setEvaluatedAt(Long evaluatedAt) {
        this.evaluatedAt = evaluatedAt;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }
}
