package com.sztus.azeroth.microservice.customer.server.object.domain;

import com.sztus.framework.component.database.annotation.Column;
import com.sztus.framework.component.database.annotation.Entity;
import com.sztus.framework.component.database.annotation.GeneratedValue;
import com.sztus.framework.component.database.annotation.Id;
import com.sztus.framework.component.database.enumerate.GenerationType;

@Entity
public class CustomerIbvAuthorizationRecord {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private Long customerId;

    @Column
    private Long ibvProviderId;

    @Column
    private Long portfolioId;

    @Column
    private String requestData;

    @Column
    private String requestCode;

    @Column
    private Integer requestStatus;

    @Column
    private Long authorizedAt;

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

    public Long getIbvProviderId() {
        return ibvProviderId;
    }

    public void setIbvProviderId(Long ibvProviderId) {
        this.ibvProviderId = ibvProviderId;
    }

    public Long getPortfolioId() {
        return portfolioId;
    }

    public void setPortfolioId(Long portfolioId) {
        this.portfolioId = portfolioId;
    }

    public String getRequestData() {
        return requestData;
    }

    public void setRequestData(String requestData) {
        this.requestData = requestData;
    }

    public String getRequestCode() {
        return requestCode;
    }

    public void setRequestCode(String requestCode) {
        this.requestCode = requestCode;
    }

    public Integer getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(Integer requestStatus) {
        this.requestStatus = requestStatus;
    }

    public Long getAuthorizedAt() {
        return authorizedAt;
    }

    public void setAuthorizedAt(Long authorizedAt) {
        this.authorizedAt = authorizedAt;
    }
}
