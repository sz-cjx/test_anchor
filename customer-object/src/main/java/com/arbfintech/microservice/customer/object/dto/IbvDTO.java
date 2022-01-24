package com.arbfintech.microservice.customer.object.dto;

public class IbvDTO {

    private Long id;

    private Long customerId;

    private String authorizationData;

    private Integer authorizationStatus;

    private Long authorizatedAt;

    private Integer requestCodeStatus;

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

    public String getAuthorizationData() {
        return authorizationData;
    }

    public void setAuthorizationData(String authorizationData) {
        this.authorizationData = authorizationData;
    }

    public Integer getAuthorizationStatus() {
        return authorizationStatus;
    }

    public void setAuthorizationStatus(Integer authorizationStatus) {
        this.authorizationStatus = authorizationStatus;
    }

    public Long getAuthorizatedAt() {
        return authorizatedAt;
    }

    public void setAuthorizatedAt(Long authorizatedAt) {
        this.authorizatedAt = authorizatedAt;
    }

    public Integer getRequestCodeStatus() {
        return requestCodeStatus;
    }

    public void setRequestCodeStatus(Integer requestCodeStatus) {
        this.requestCodeStatus = requestCodeStatus;
    }
}
