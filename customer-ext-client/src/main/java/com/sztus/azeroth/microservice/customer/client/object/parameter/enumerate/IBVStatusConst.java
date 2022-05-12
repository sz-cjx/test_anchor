package com.sztus.azeroth.microservice.customer.client.object.parameter.enumerate;

public interface IBVStatusConst {
    Integer OTHER_ERROR = -3;
    Integer FINANCIAL_INSTITUTION_ERROR = -2;
    Integer ACCOUNT_ERROR = -1;
    Integer NOT_STARTED = 0;
    Integer STARTED_NOT_COMPLETED = 1;
    Integer LOGIN_VERIFIED = 3;
    Integer IN_PROGRESS = 20;
    Integer SUCCESSFUL = 100;
}
