package com.arbfintech.microservice.customer.restapi.util;

import com.arbfintech.framework.component.core.type.ProcedureException;
import com.arbfintech.microservice.customer.object.enumerate.CustomerErrorCode;

/**
 * @author Fly_Roushan
 * @date 2020/12/27
 */
public class ResultUtil {

    public static void checkResult(Long row, CustomerErrorCode errorCode) throws ProcedureException {
        if (row < 1) {
            throw new ProcedureException(errorCode);
        }
    }
}