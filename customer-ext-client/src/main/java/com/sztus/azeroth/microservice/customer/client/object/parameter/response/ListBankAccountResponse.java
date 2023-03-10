package com.sztus.azeroth.microservice.customer.client.object.parameter.response;

import com.sztus.azeroth.microservice.customer.client.object.view.CustomerBankAccountDataView;

import java.util.List;

public class ListBankAccountResponse {
    Integer count;
    List<CustomerBankAccountDataView> items;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public List<CustomerBankAccountDataView> getItems() {
        return items;
    }

    public void setItems(List<CustomerBankAccountDataView> items) {
        this.items = items;
    }
}
