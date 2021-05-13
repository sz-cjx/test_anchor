package com.arbfintech.microservice.customer.object.entity;

import com.arbfintech.framework.component.core.annotation.Column;
import com.arbfintech.framework.component.core.annotation.Entity;
import com.arbfintech.framework.component.core.annotation.Id;

/**
 * @author Fly_Roushan
 * @date 2021/1/4
 */
@Entity
public class CustomerOptInData {

    @Id
    @Column
    private Long id;

    @Id
    @Column
    private Long portfolioId;

    @Id
    @Column
    private Integer optInType;

    @Column
    private Integer optInValue;

    @Column
    private Long createdAt;

    @Column
    private Long updatedAt;

    public CustomerOptInData() {

    }

    public CustomerOptInData(Long id, Long portfolioId, Integer optInType, Integer optInValue, Long createdAt, Long updatedAt) {
        this.id = id;
        this.portfolioId = portfolioId;
        this.optInType = optInType;
        this.optInValue = optInValue;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPortfolioId() {
        return portfolioId;
    }

    public void setPortfolioId(Long portfolioId) {
        this.portfolioId = portfolioId;
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
