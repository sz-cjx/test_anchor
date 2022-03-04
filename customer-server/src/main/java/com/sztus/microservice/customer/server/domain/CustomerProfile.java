package com.sztus.microservice.customer.server.domain;

import com.sztus.framework.component.core.annotation.Column;
import com.sztus.framework.component.core.annotation.Entity;
import com.sztus.framework.component.core.annotation.GeneratedValue;
import com.sztus.framework.component.core.annotation.Id;
import com.sztus.framework.component.core.enumerate.GenerationType;

@Entity
public class CustomerProfile {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long customerId;

    @Column
    private String username;

    @Column
    private String nickname;

    @Column
    private String introduction;

    @Column
    private String avatarUrl;

    @Column
    private Long createdAt;

    @Column
    private Long updatedAt;

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
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
}
