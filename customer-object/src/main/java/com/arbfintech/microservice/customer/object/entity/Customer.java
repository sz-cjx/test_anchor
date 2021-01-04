package com.arbfintech.microservice.customer.object.entity;

import com.arbfintech.framework.component.core.annotation.Column;
import com.arbfintech.framework.component.core.annotation.Entity;
import com.arbfintech.framework.component.core.annotation.GeneratedValue;
import com.arbfintech.framework.component.core.annotation.Id;
import com.arbfintech.framework.component.core.enumerate.GenerationType;

/**
 * @author Fly_Roushan
 * @date 2021/1/4
 */
@Entity
public class Customer {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private String openId;

    @Column
    private String uniqueCode;

    @Column
    private Long createdAt;

    @Column
    private Long updatedAt;

    @Column
    private Integer status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getUniqueCode() {
        return uniqueCode;
    }

    public void setUniqueCode(String uniqueCode) {
        this.uniqueCode = uniqueCode;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
