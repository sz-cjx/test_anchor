package com.sztus.dalaran.microservice.customer.client.object.parameter.response;

import com.sztus.dalaran.microservice.customer.client.object.view.CustomerOptInDataView;

import java.util.List;

public class GetCustomerOptInDataAsListResponse {

    private List<CustomerOptInDataView> items;

    public List<CustomerOptInDataView> getItems() {
        return items;
    }

    public void setItems(List<CustomerOptInDataView> items) {
        this.items = items;
    }
}
