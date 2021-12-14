package com.arbfintech.microservice.customer.object.entity;

import com.arbfintech.framework.component.core.annotation.Column;
import com.arbfintech.framework.component.core.annotation.Entity;
import com.arbfintech.framework.component.core.annotation.Id;

@Entity
public class CustomerDecisionLogicAuthorizationRecord {

    @Id
    @Column
    private Long id;

    @Column
    private Long customerId;

    @Column
    private Long portfolioId;

    @Column
    private String authorizationData;

    @Column
    private Long authorizationStatus;

    @Column
    private Long authorizatedAt;

    @Column
    private Long requestCode;

    @Column
    private Long requestCodeStatus;

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

    public String getAuthorizationData() {
        return authorizationData;
    }

    public void setAuthorizationData(String authorizationData) {
        this.authorizationData = authorizationData;
    }

    public Long getAuthorizationStatus() {
        return authorizationStatus;
    }

    public void setAuthorizationStatus(Long authorizationStatus) {
        this.authorizationStatus = authorizationStatus;
    }

    public Long getAuthorizatedAt() {
        return authorizatedAt;
    }

    public void setAuthorizatedAt(Long authorizatedAt) {
        this.authorizatedAt = authorizatedAt;
    }

    public Long getRequestCode() {
        return requestCode;
    }

    public void setRequestCode(Long requestCode) {
        this.requestCode = requestCode;
    }

    public Long getRequestCodeStatus() {
        return requestCodeStatus;
    }

    public void setRequestCodeStatus(Long requestCodeStatus) {
        this.requestCodeStatus = requestCodeStatus;
    }
}
