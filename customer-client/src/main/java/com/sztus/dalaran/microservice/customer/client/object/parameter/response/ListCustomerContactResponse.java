package com.sztus.dalaran.microservice.customer.client.object.parameter.response;

import com.sztus.dalaran.microservice.customer.client.object.view.CustomerContactInfoView;

import java.util.List;

public class ListCustomerContactResponse {

    private List<CustomerContactInfoView> list;

    public List<CustomerContactInfoView> getList() {
        return list;
    }

    public void setList(List<CustomerContactInfoView> list) {
        this.list = list;
    }
}
