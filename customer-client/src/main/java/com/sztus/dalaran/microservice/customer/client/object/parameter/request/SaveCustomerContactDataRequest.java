package com.sztus.dalaran.microservice.customer.client.object.parameter.request;

import com.sztus.dalaran.microservice.customer.client.object.view.CustomerContactDataView;

import java.util.List;

public class SaveCustomerContactDataRequest {

    private List<CustomerContactDataView> contactDataList;

    public List<CustomerContactDataView> getContactDataList() {
        return contactDataList;
    }

    public void setContactDataList(List<CustomerContactDataView> contactDataList) {
        this.contactDataList = contactDataList;
    }
}
