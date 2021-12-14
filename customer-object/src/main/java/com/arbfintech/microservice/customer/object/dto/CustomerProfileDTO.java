package com.arbfintech.microservice.customer.object.dto;

import javax.validation.constraints.NotNull;

public class CustomerProfileDTO {

    @NotNull(message = "customerId can not be null.")
    private Long customerId;

    @NotNull(message = "profileFeature can not be null.")
    private String profileFeature;

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getProfileFeature() {
        return profileFeature;
    }

    public void setProfileFeature(String profileFeature) {
        this.profileFeature = profileFeature;
    }
}
