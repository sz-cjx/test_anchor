package com.sztus.azeroth.microservice.customer.client.object.parameter.request;

import com.sztus.azeroth.microservice.customer.client.object.view.CustomerContactInfoView;

public class SaveCustomerContactInfoRequest extends CustomerContactInfoView {
    private Boolean isVerified;

    public Boolean getIsVerified() {
        return isVerified;
    }

    public void setIsVerified(Boolean verified) {
        isVerified = verified;
    }
}
