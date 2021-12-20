package com.arbfintech.microservice.customer.object.dto;

import javax.validation.constraints.NotNull;

public class CustomerDocumentDTO {

    @NotNull(message = "customerId can not be null.")
    private Long customerId;

    @NotNull(message = "documentCategory can not be null.")
    private Integer documentCategory;

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Integer getDocumentCategory() {
        return documentCategory;
    }

    public void setDocumentCategory(Integer documentCategory) {
        this.documentCategory = documentCategory;
    }
}
