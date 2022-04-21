package com.sztus.dalaran.microservice.customer.client.object.parameter.response;

import com.sztus.dalaran.microservice.customer.client.object.view.CustomerContactDataView;

import java.util.List;

public class GetCustomerContactDataAsListResponse {

    private List<CustomerContactDataView> items;

    public List<CustomerContactDataView> getItems() {
        return items;
    }

    public void setItems(List<CustomerContactDataView> items) {
        this.items = items;
    }
}
