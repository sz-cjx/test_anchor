package com.arbfintech.microservice.customer.object.dto;

import com.alibaba.fastjson.JSONObject;

import javax.validation.constraints.NotNull;

public class CustomerProfileDTO {

    @NotNull(message = "customerId can not be null.")
    private Long customerId;

    @NotNull(message = "profileFeature can not be null.")
    private String profileFeature;

    private JSONObject data;

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

    public JSONObject getData() {
        return data;
    }

    public void setData(JSONObject data) {
        this.data = data;
    }
}
