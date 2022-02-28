package com.arbfintech.microservice.customer.object.dto;

import javax.validation.constraints.NotNull;

public class CustomerOptInDTO {

    @NotNull()
    private Long customerId;

    @NotNull(message = "optInType can not be null.")
    private Integer optInType;

    @NotNull(message = "optInStatus can not be null.")
    private Integer optInStatus;

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

    public Integer getOptInStatus() {
        return optInStatus;
    }

    public void setOptInStatus(Integer optInStatus) {
        this.optInStatus = optInStatus;
    }
}
