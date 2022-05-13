package com.sztus.azeroth.microservice.customer.server.service;

import com.alibaba.fastjson.JSON;
import com.sztus.azeroth.microservice.customer.client.object.parameter.enumerate.IBVStatusConst;
import com.sztus.azeroth.microservice.customer.server.object.domain.CustomerIbvAuthorizationRecord;
import com.sztus.azeroth.microservice.customer.server.respository.reader.CommonReader;
import com.sztus.azeroth.microservice.customer.server.respository.writer.CommonWriter;
import com.sztus.azeroth.microservice.customer.server.type.constant.DbKey;
import com.sztus.azeroth.microservice.customer.server.util.CustomerCheckUtil;
import com.sztus.framework.component.core.constant.StatusConst;
import com.sztus.framework.component.core.type.ProcedureException;
import com.sztus.framework.component.core.util.DateUtil;
import com.sztus.framework.component.database.type.SqlOption;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class IbvService {

    @Autowired
    private CommonWriter commonWriter;

    @Autowired
    private CommonReader commonReader;

    public Long saveIbvAuthorization(CustomerIbvAuthorizationRecord ibvAuthorizationRecord) throws ProcedureException {
        if (Objects.isNull(ibvAuthorizationRecord.getAuthorizedAt())
                && (Objects.equals(IBVStatusConst.LOGIN_VERIFIED, ibvAuthorizationRecord.getRequestStatus())
                || Objects.equals(IBVStatusConst.SUCCESSFUL, ibvAuthorizationRecord.getRequestStatus()))) {
            ibvAuthorizationRecord.setAuthorizedAt(DateUtil.getCurrentTimestamp());
        }
        Long result = commonWriter.save(CustomerIbvAuthorizationRecord.class, JSON.toJSONString(ibvAuthorizationRecord));
        CustomerCheckUtil.checkSaveResult(result);
        return result;
    }

    public List<CustomerIbvAuthorizationRecord> listIbvAuthorization(Long customerId, Long portfolioId) {
        SqlOption sqlOption = SqlOption.getInstance();
        sqlOption.whereEqual(DbKey.CUSTOMER_ID, customerId);
        if (Objects.nonNull(portfolioId)) {
            sqlOption.whereEqual(DbKey.PORTFOLIO_ID, portfolioId);
        }
        sqlOption.order("authorized_at DESC");
        return commonReader.findAllByOptions(CustomerIbvAuthorizationRecord.class, sqlOption.toString());
    }

}
