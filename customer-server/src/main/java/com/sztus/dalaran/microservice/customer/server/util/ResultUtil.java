package com.sztus.dalaran.microservice.customer.server.util;


import com.sztus.dalaran.microservice.customer.client.object.type.CustomerErrorCode;
import com.sztus.framework.component.core.type.ProcedureException;

/**
 * @author Fly_Roushan
 * @date 2020/12/27
 */
public class ResultUtil {

    public static void checkResult(Long id, CustomerErrorCode errorCode) throws ProcedureException {
        if (id < 1) {
            throw new ProcedureException(errorCode);
        }
    }

    public static void checkResult(Integer row, CustomerErrorCode errorCode) throws ProcedureException {
        if (row < 1) {
            throw new ProcedureException(errorCode);
        }
    }
}
