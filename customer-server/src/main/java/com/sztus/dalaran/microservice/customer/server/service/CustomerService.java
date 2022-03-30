package com.sztus.dalaran.microservice.customer.server.service;

import com.alibaba.fastjson.JSON;
import com.sztus.dalaran.microservice.customer.client.object.parameter.request.CreateCustomerRequest;
import com.sztus.dalaran.microservice.customer.client.object.parameter.request.GetCustomerByUniqueRequest;
import com.sztus.dalaran.microservice.customer.client.object.type.CustomerErrorCode;
import com.sztus.dalaran.microservice.customer.client.object.type.CustomerOptInType;
import com.sztus.dalaran.microservice.customer.client.object.type.CustomerOptInValue;
import com.sztus.dalaran.microservice.customer.server.business.DbKey;
import com.sztus.dalaran.microservice.customer.server.converter.CustomerConverter;
import com.sztus.dalaran.microservice.customer.server.domain.Customer;
import com.sztus.dalaran.microservice.customer.server.domain.CustomerOptInData;
import com.sztus.dalaran.microservice.customer.server.domain.CustomerPersonalData;
import com.sztus.dalaran.microservice.customer.server.repository.CustomerWriter;
import com.sztus.dalaran.microservice.customer.server.util.CustomerUtil;
import com.sztus.dalaran.microservice.customer.server.util.ResultUtil;
import com.sztus.framework.component.core.constant.CodeConst;
import com.sztus.framework.component.core.enumerate.StatusEnum;
import com.sztus.framework.component.core.type.ProcedureException;
import com.sztus.framework.component.core.util.DateUtil;
import com.sztus.framework.component.core.util.UuidUtil;
import com.sztus.framework.component.database.core.SimpleJdbcReader;
import com.sztus.framework.component.database.core.SimpleJdbcWriter;
import com.sztus.framework.component.database.type.SqlOption;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class CustomerService {

    public Customer getCustomer(String email, String openId) throws ProcedureException {
        if (StringUtils.isBlank(email) && StringUtils.isBlank(openId)) {
            throw new ProcedureException(CustomerErrorCode.PARAMETER_IS_INCOMPLETE);
        }

        SqlOption sqlOption = SqlOption.getInstance();
        if (StringUtils.isNotBlank(email)) {
            sqlOption.whereEqual(DbKey.EMAIL, email);
        }

        if (StringUtils.isNotBlank(openId)) {
            sqlOption.whereEqual(DbKey.OPEN_ID, openId);
        }

        return Optional
                .ofNullable(jdbcReader.findByOptions(Customer.class, sqlOption.toString()))
                .orElseThrow(() -> new ProcedureException(CustomerErrorCode.CUSTOMER_IS_NOT_EXISTED));
    }

    /**
     * 条件： id, openId
     * 条件无值保存，条件有值，通过条件更新
     *
     * @param customer
     * @return
     */
    public Customer saveCustomer(Customer customer) throws ProcedureException {
        Long currentTimestamp = DateUtil.getCurrentTimestamp();
        customer.setUpdatedAt(currentTimestamp);
        Long id = customer.getId();
        String openId = customer.getOpenId();

        Long result = null;
        if (Objects.isNull(id) && StringUtils.isBlank(openId)) {
            // Save
            customer.setCreatedAt(currentTimestamp);
            customer.setOpenId(UuidUtil.getUuid());
            // TODO uniqueCode is null
            result = jdbcWriter.save(customer);
        } else {
            // Update
            SqlOption sqlOption = SqlOption.getInstance();
            if (Objects.nonNull(id)) {
                sqlOption.whereEqual(DbKey.ID, id);
            }

            if (StringUtils.isNotBlank(openId)) {
                sqlOption.whereEqual(DbKey.OPEN_ID, openId);
            }
            result = jdbcWriter.save(Customer.class, JSON.toJSONString(customer), sqlOption.toString());

        }
        if (Objects.isNull(result) || result < CodeConst.DEFAULT) {
            throw new ProcedureException(CustomerErrorCode.FAIL_TO_SAVE_CUSTOMER_INFORMATION);
        }
        if (result >= CodeConst.SUCCESS && Objects.isNull(id) && StringUtils.isBlank(openId)) {
            customer.setId(result);
        }

        return customer;
    }

    public Customer getCustomerByUnique(GetCustomerByUniqueRequest request) throws ProcedureException {
        String ssn = request.getSsn();
        String routingNo = request.getRoutingNo();
        String accountNo = request.getAccountNo();
        if (StringUtils.isAnyBlank(ssn, routingNo, accountNo)) {
            throw new ProcedureException(CustomerErrorCode.PARAMETER_IS_INCOMPLETE);
        }
        SqlOption sqlOption = SqlOption.getInstance();
        sqlOption.whereEqual(DbKey.UNIQUE_CODE, CustomerUtil.generateUniqueCode(ssn, routingNo, accountNo));
        return Optional
                .ofNullable(jdbcReader.findByOptions(Customer.class, sqlOption.toString()))
                .orElseThrow(() -> new ProcedureException(CustomerErrorCode.CUSTOMER_IS_NOT_EXISTED));
    }

    @Transactional(rollbackFor = Exception.class)
    public Customer createCustomer(CreateCustomerRequest request) throws ProcedureException {
        String ssn = request.getSsn();
        String routingNo = request.getRoutingNo();
        String accountNo = request.getAccountNo();

        if (StringUtils.isAnyBlank(ssn, routingNo, accountNo)) {
            throw new ProcedureException(CustomerErrorCode.PARAMETER_IS_INCOMPLETE);
        }

        String uniqueCode = CustomerUtil.generateUniqueCode(ssn, routingNo, accountNo);
        Customer customerDb = jdbcReader.findByOptions(Customer.class,
                SqlOption.getInstance().whereEqual(DbKey.UNIQUE_CODE, uniqueCode).toString()
        );

        if (customerDb != null) {
            throw new ProcedureException(CustomerErrorCode.CUSTOMER_ALREADY_EXISTS);
        }

        Customer customer = CustomerConverter.INSTANCE.CreateCustomerRequestToCustomer(request);
        CustomerPersonalData customerPersonalData = CustomerConverter.INSTANCE.CreateCustomerRequestToCustomerProfile(request);

        long now = System.currentTimeMillis();
        customer.setUniqueCode(uniqueCode);
        customer.setOpenId(UuidUtil.getUuid());
        customer.setCreatedAt(now);
        customer.setUpdatedAt(now);
        customer.setStatus(StatusEnum.ENABLE.getValue());

        long customerId = jdbcWriter.create(Customer.class, JSON.toJSONString(customer));
        ResultUtil.checkResult(customerId, CustomerErrorCode.CUSTOMER_SAVE_FAILED);

        customer.setId(customerId);
        customerPersonalData.setCustomerId(customerId);
        customerPersonalData.setCreatedAt(now);
        customerPersonalData.setUpdatedAt(now);

        ResultUtil.checkResult(
                jdbcWriter.create(CustomerPersonalData.class, JSON.toJSONString(customerPersonalData)),
                CustomerErrorCode.CUSTOMER_PERSONAL_DATA_SAVE_FAILED
        );

        Integer defaultValue = CustomerOptInValue.IS_MARKETING.getValue() + CustomerOptInValue.IS_OPERATION.getValue();
        List<CustomerOptInData> customerOptInDataList = new ArrayList<>();
        customerOptInDataList.add(new CustomerOptInData(customerId, CustomerOptInType.EMAIL.getValue(), defaultValue, now, now));
        customerOptInDataList.add(new CustomerOptInData(customerId, CustomerOptInType.HOME_PHONE.getValue(), 0, now, now));
        customerOptInDataList.add(new CustomerOptInData(customerId, CustomerOptInType.CELL_PHONE.getValue(), 0, now, now));

        ResultUtil.checkResult(
                customerWriter.batchSave(customerOptInDataList),
                CustomerErrorCode.CUSTOMER_OPT_IN_DATA_SAVE_FAILED
        );

        return customer;
    }

    @Autowired
    private SimpleJdbcReader jdbcReader;

    @Autowired
    private SimpleJdbcWriter jdbcWriter;

    @Autowired
    private CustomerWriter customerWriter;
}
