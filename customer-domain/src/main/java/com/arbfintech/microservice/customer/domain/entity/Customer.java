package com.arbfintech.microservice.customer.domain.entity;

import com.alibaba.fastjson.JSONObject;
import com.arbfintech.framework.component.core.annotation.Column;
import com.arbfintech.framework.component.core.annotation.Entity;
import com.arbfintech.framework.component.core.annotation.Id;

@Entity
public class Customer {
    @Id
    @Column
    private Long id;

    @Column
    private String ssn;

    @Column
    private String email;

    @Column
    private String homePhone;

    @Column
    private String cellPhone;

    @Column
    private Long flags;

    @Column
    private Long updateTime;

    @Column
    private Long createTime;

    @Column
    private Integer status;

    @Column(length = 32)
    private String firstName;

    @Column(length = 32)
    private String middleName;

    @Column(length = 32)
    private String lastName;

    @Column(length = 128)
    private String password;

    @Column(length = 16)
    private String salt;

    @Column
    private Integer emailMarketingStatus;

    @Column
    private Integer emailOperationStatus;

    @Column
    private Integer smsMarketingStatus;

    @Column
    private Integer smsOperationStatus;

    @Column
    private Integer isSignUp;

    @Column
    private String bankRoutingNo;

    @Column
    private String bankAccountNo;

    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSsn() {
        return ssn;
    }

    public void setSsn(String ssn) {
        this.ssn = ssn;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHomePhone() {
        return homePhone;
    }

    public void setHomePhone(String homePhone) {
        this.homePhone = homePhone;
    }

    public String getCellPhone() {
        return cellPhone;
    }

    public void setCellPhone(String cellPhone) {
        this.cellPhone = cellPhone;
    }

    public Long getFlags() {
        return flags;
    }

    public void setFlags(Long flags) {
        this.flags = flags;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public Integer getEmailMarketingStatus() {
        return emailMarketingStatus;
    }

    public void setEmailMarketingStatus(Integer emailMarketingStatus) {
        this.emailMarketingStatus = emailMarketingStatus;
    }

    public Integer getEmailOperationStatus() {
        return emailOperationStatus;
    }

    public void setEmailOperationStatus(Integer emailOperationStatus) {
        this.emailOperationStatus = emailOperationStatus;
    }

    public Integer getSmsMarketingStatus() {
        return smsMarketingStatus;
    }

    public void setSmsMarketingStatus(Integer smsMarketingStatus) {
        this.smsMarketingStatus = smsMarketingStatus;
    }

    public Integer getSmsOperationStatus() {
        return smsOperationStatus;
    }

    public void setSmsOperationStatus(Integer smsOperationStatus) {
        this.smsOperationStatus = smsOperationStatus;
    }

    public Integer getIsSignUp() {
        return isSignUp;
    }

    public void setIsSignUp(Integer isSignUp) {
        this.isSignUp = isSignUp;
    }

    public String getBankRoutingNo() {
        return bankRoutingNo;
    }

    public void setBankRoutingNo(String bankRoutingNo) {
        this.bankRoutingNo = bankRoutingNo;
    }

    public String getBankAccountNo() {
        return bankAccountNo;
    }

    public void setBankAccountNo(String bankAccountNo) {
        this.bankAccountNo = bankAccountNo;
    }

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }
}
