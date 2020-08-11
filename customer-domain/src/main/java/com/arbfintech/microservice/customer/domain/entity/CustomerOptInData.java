package com.arbfintech.microservice.customer.domain.entity;

import com.arbfintech.framework.component.core.annotation.Column;
import com.arbfintech.framework.component.core.annotation.Entity;
import com.arbfintech.framework.component.core.annotation.Id;

@Entity
public class CustomerOptInData {

    @Id
    @Column
    private Long customerId;

    @Column
    private Integer emailOptIn;

    @Column
    private Integer homePhoneOptIn;

    @Column
    private Integer cellPhoneOptIn;

    @Column
    private Long updatedAt;


    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Integer getEmailOptIn() {
        return emailOptIn;
    }

    public void setEmailOptIn(Integer emailOptIn) {
        this.emailOptIn = emailOptIn;
    }

    public Integer getHomePhoneOptIn() {
        return homePhoneOptIn;
    }

    public void setHomePhoneOptIn(Integer homePhoneOptIn) {
        this.homePhoneOptIn = homePhoneOptIn;
    }

    public Integer getCellPhoneOptIn() {
        return cellPhoneOptIn;
    }

    public void setCellPhoneOptIn(Integer cellPhoneOptIn) {
        this.cellPhoneOptIn = cellPhoneOptIn;
    }

    public Long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Long updatedAt) {
        this.updatedAt = updatedAt;
    }
}
