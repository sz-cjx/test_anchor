package com.arbfintech.microservice.customer.object.dto;

import com.arbfintech.microservice.customer.object.entity.CustomerEmploymentData;
import com.arbfintech.microservice.customer.object.entity.CustomerProfile;
import com.arbfintech.microservice.customer.object.entity.CustomerStatementData;

import java.util.List;

public class CalculationProcessDTO {

    private CalculationResultDTO calculationResultDTO;

    private CustomerProfile customerProfile;

    private CustomerEmploymentData customerEmploymentData;

    private List<CustomerStatementData> statementList;

    public CalculationResultDTO getCalculationResultDTO() {
        return calculationResultDTO;
    }

    public void setCalculationResultDTO(CalculationResultDTO calculationResultDTO) {
        this.calculationResultDTO = calculationResultDTO;
    }

    public CustomerProfile getCustomerProfile() {
        return customerProfile;
    }

    public void setCustomerProfile(CustomerProfile customerProfile) {
        this.customerProfile = customerProfile;
    }

    public CustomerEmploymentData getCustomerEmploymentData() {
        return customerEmploymentData;
    }

    public void setCustomerEmploymentData(CustomerEmploymentData customerEmploymentData) {
        this.customerEmploymentData = customerEmploymentData;
    }

    public List<CustomerStatementData> getStatementList() {
        return statementList;
    }

    public void setStatementList(List<CustomerStatementData> statementList) {
        this.statementList = statementList;
    }
}
