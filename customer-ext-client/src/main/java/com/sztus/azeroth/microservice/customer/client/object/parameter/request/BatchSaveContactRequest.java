package com.sztus.azeroth.microservice.customer.client.object.parameter.request;

import com.sztus.azeroth.microservice.customer.client.object.view.CustomerContactInfoView;

import java.util.List;

public class BatchSaveContactRequest {

    private List<CustomerContactInfoView> list;

    private Boolean isVerified;

    public List<CustomerContactInfoView> getList() {
        return list;
    }

    public void setList(List<CustomerContactInfoView> list) {
        this.list = list;
    }

    public Boolean getIsVerified() {
        return isVerified;
    }

    public void setIsVerified(Boolean verified) {
        isVerified = verified;
    }
}
