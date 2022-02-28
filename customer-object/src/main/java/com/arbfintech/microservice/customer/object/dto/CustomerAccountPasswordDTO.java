package com.arbfintech.microservice.customer.object.dto;

public class CustomerAccountPasswordDTO {

    private Long id;

    private String currentLoginPassword;

    private String loginPassword;

    private String currentPaymentPassword;

    private String paymentPassword;

    private Integer changeType;

    private String verification;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCurrentLoginPassword() {
        return currentLoginPassword;
    }

    public void setCurrentLoginPassword(String currentLoginPassword) {
        this.currentLoginPassword = currentLoginPassword;
    }

    public String getLoginPassword() {
        return loginPassword;
    }

    public void setLoginPassword(String loginPassword) {
        this.loginPassword = loginPassword;
    }

    public String getCurrentPaymentPassword() {
        return currentPaymentPassword;
    }

    public void setCurrentPaymentPassword(String currentPaymentPassword) {
        this.currentPaymentPassword = currentPaymentPassword;
    }

    public String getPaymentPassword() {
        return paymentPassword;
    }

    public void setPaymentPassword(String paymentPassword) {
        this.paymentPassword = paymentPassword;
    }

    public Integer getChangeType() {
        return changeType;
    }

    public void setChangeType(Integer changeType) {
        this.changeType = changeType;
    }

    public String getVerification() {
        return verification;
    }

    public void setVerification(String verification) {
        this.verification = verification;
    }
}
