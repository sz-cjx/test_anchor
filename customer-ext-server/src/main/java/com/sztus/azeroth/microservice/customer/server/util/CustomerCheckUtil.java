package com.sztus.azeroth.microservice.customer.server.util;

import com.sztus.azeroth.microservice.customer.client.object.parameter.enumerate.CustomerErrorCode;
import com.sztus.framework.component.core.type.ProcedureException;

import java.util.Objects;

public class CustomerCheckUtil {

    public static void checkSaveResult(Long result) throws ProcedureException {
        if (Objects.isNull(result) || result < 1) {
            throw new ProcedureException(CustomerErrorCode.FAILURE_TO_SAVE_DATA);
        }
    }

}
