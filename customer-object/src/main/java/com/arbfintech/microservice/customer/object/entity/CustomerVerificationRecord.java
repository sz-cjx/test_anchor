package com.arbfintech.microservice.customer.object.entity;

import com.arbfintech.framework.component.core.annotation.Column;
import com.arbfintech.framework.component.core.annotation.Entity;
import com.arbfintech.framework.component.core.annotation.Id;

@Entity
public class CustomerVerificationRecord {

    @Id
    @Column
    private Long id;

    @Column
    private Long customerId;

    @Column
    private String veirifedValue;

    @Column
    private Long verifiedAt;

    @Column
    private Integer status;

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

    public String getVeirifedValue() {
        return veirifedValue;
    }

    public void setVeirifedValue(String veirifedValue) {
        this.veirifedValue = veirifedValue;
    }

    public Long getVerifiedAt() {
        return verifiedAt;
    }

    public void setVerifiedAt(Long verifiedAt) {
        this.verifiedAt = verifiedAt;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
