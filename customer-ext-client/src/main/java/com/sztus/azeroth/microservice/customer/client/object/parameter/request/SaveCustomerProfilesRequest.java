package com.sztus.azeroth.microservice.customer.client.object.parameter.request;

public class SaveCustomerProfilesRequest {

    private Long customerId;
    private SaveCustomerBankAccountRequest bankAccount;
    private SaveCustomerEmploymentRequest employment;
    private SaveCustomerIdentityRequest identity;
    private SaveCustomerPayrollRequest payroll;
    private BatchSaveContactRequest contactInfo;

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public SaveCustomerBankAccountRequest getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(SaveCustomerBankAccountRequest bankAccount) {
        this.bankAccount = bankAccount;
    }

    public SaveCustomerEmploymentRequest getEmployment() {
        return employment;
    }

    public void setEmployment(SaveCustomerEmploymentRequest employment) {
        this.employment = employment;
    }

    public SaveCustomerIdentityRequest getIdentity() {
        return identity;
    }

    public void setIdentity(SaveCustomerIdentityRequest identity) {
        this.identity = identity;
    }

    public SaveCustomerPayrollRequest getPayroll() {
        return payroll;
    }

    public void setPayroll(SaveCustomerPayrollRequest payroll) {
        this.payroll = payroll;
    }

    public BatchSaveContactRequest getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(BatchSaveContactRequest contactInfo) {
        this.contactInfo = contactInfo;
    }
}
