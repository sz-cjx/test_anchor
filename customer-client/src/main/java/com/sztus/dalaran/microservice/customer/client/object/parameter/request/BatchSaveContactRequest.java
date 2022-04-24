package com.sztus.dalaran.microservice.customer.client.object.parameter.request;

import com.sztus.dalaran.microservice.customer.client.object.view.CustomerContactDataView;

import java.util.List;

public class BatchSaveContactRequest {

    private List<CustomerContactDataView> list;

    public List<CustomerContactDataView> getList() {
        return list;
    }

    public void setList(List<CustomerContactDataView> list) {
        this.list = list;
    }
}
