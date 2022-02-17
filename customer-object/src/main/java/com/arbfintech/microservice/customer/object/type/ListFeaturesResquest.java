package com.arbfintech.microservice.customer.object.type;

import java.util.List;

/**
 * @author Luffy
 * @description
 * @createTime 2022年02月17日 04:12:00
 */
public class ListFeaturesResquest {

    private List<Long> customerIds;

    private Long portfolioId;

    public List<Long> getCustomerIds() {
        return customerIds;
    }

    public void setCustomerIds(List<Long> customerIds) {
        this.customerIds = customerIds;
    }

    public Long getPortfolioId() {
        return portfolioId;
    }

    public void setPortfolioId(Long portfolioId) {
        this.portfolioId = portfolioId;
    }
}
