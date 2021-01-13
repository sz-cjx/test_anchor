package com.arbfintech.microservice.customer.object.type;

/**
 * @auhtor: clark
 * @date: 2021/1/13  18:56
 */
public class SearchCustomerRequest {

    private Long id;

    private String email;

    private String openId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }
}
