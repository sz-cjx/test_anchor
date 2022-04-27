package com.sztus.azeroth.microservice.customer.client.object.parameter.response;

import com.sztus.azeroth.microservice.customer.client.object.view.IbvAuthorizationView;

import java.util.List;

public class ListIbvAuthorizationResponse {
    Integer count;
    List<IbvAuthorizationView> items;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public List<IbvAuthorizationView> getItems() {
        return items;
    }

    public void setItems(List<IbvAuthorizationView> items) {
        this.items = items;
    }
}
