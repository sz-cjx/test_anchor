package com.arbfintech.microservice.customer.object.enumerate;

import com.arbfintech.framework.component.core.base.BaseEnum;

public enum CustomerDocumentTypeEnum implements BaseEnum {

    IDENTIFY_DOCUMENT(1, "Identify document"),
    BANK_DOCUMENT(2, "Bank document"),
    OTHERS_DOCUMENT(3, "Others Loan document"),
    LOAN_AGREEMENT(4, "Loan Agreement"),
    CERTIFICATE_OF_EVIDENCE(5, "Certificate of Evidence"),
    BANKRUPTCY(6, "Bankruptcy"),
    CCCS(7,"CCCS Attachments"),
    PAPER_CHECK(8, "Paper Check"),
    EMAIL_ATTACHMENTS(9, "Email Attachments"),
    POA(10, "POA"),
    ATTY_REP_EMAIL(11, "Atty Rep Email"),
    MONEY_ORDER_ATTACHMENTS(12, "Money Order Attachments"),
    ONLINE_VERIFICATION_OF_EMPLOYMENT(13, "Online Verification of Employment"),
    SCRA(14, "SCRA"),
    SSN(100, "SSN"),
    DRIVER_LICENSE(101, "Driver License"),
    GRADUATION_CERTIFICATE(102, "Graduation Certificate")
    ;

    CustomerDocumentTypeEnum(Integer value, String text) {
        this.value = value;
        this.text = text;
    }

    private Integer value;
    private String text;

    @Override
    public Integer getValue() {
        return value;
    }

    @Override
    public String getText() {
        return text;
    }

}