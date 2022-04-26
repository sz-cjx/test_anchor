package com.sztus.azeroth.microservice.customer.server.service;

import com.alibaba.fastjson.JSON;
import com.sztus.azeroth.microservice.customer.client.object.parameter.enumerate.CustomerErrorCode;
import com.sztus.azeroth.microservice.customer.server.object.domain.*;
import com.sztus.azeroth.microservice.customer.server.respository.reader.CommonReader;
import com.sztus.azeroth.microservice.customer.server.respository.reader.CustomerReader;
import com.sztus.azeroth.microservice.customer.server.respository.writer.CommonWriter;
import com.sztus.azeroth.microservice.customer.server.respository.writer.CustomerWriter;
import com.sztus.azeroth.microservice.customer.server.type.constant.DbKey;
import com.sztus.azeroth.microservice.customer.server.util.CustomerCheckUtil;
import com.sztus.azeroth.microservice.customer.server.util.CustomerUtil;
import com.sztus.framework.component.core.type.ProcedureException;
import com.sztus.framework.component.core.util.DateUtil;
import com.sztus.framework.component.core.util.UuidUtil;
import com.sztus.framework.component.database.type.SqlOption;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class CustomerGeneralService {

    @Autowired
    private CustomerReader customerReader;

    @Autowired
    private CustomerWriter customerWriter;

    @Autowired
    private CommonReader commonReader;

    @Autowired
    private CommonWriter commonWriter;

    public Customer getCustomerByCustomerId(Long customerId) {
        return customerReader.findById(Customer.class, customerId, null);
    }

    public CustomerContactInfo getCustomerContactByContact(String contactInformation) {
        SqlOption sqlOption = SqlOption.getInstance();
        sqlOption.whereEqual(DbKey.VALUE, contactInformation);
        return customerReader.findByOptions(CustomerContactInfo.class, sqlOption.toString());
    }

    public Customer getCustomerByOpenId(String openId) {
        SqlOption sqlOption = SqlOption.getInstance();
        sqlOption.whereEqual(DbKey.OPEN_ID, openId);
        return customerReader.findByOptions(Customer.class, sqlOption.toString());
    }

    public Customer getCustomerByUniqueCode(String uniqueCode) {
        SqlOption sqlOption = SqlOption.getInstance();
        sqlOption.whereEqual(DbKey.UNIQUE_CODE, uniqueCode);
        return customerReader.findByOptions(Customer.class, sqlOption.toString());
    }

    public void saveCustomer(Customer customer) throws ProcedureException {
        Long id = customer.getId();
        String openId = customer.getOpenId();

        Customer customerDb = null;
        if (Objects.nonNull(id) || StringUtils.isNotBlank(openId)) {
            customerDb = customerReader.getCustomerByCondition(id, openId);
        }
        if (Objects.nonNull(customerDb)) {
            customer.setId(customerDb.getId());
            customer.setOpenId(customerDb.getOpenId());
        } else {
            // 校验username是否存在
            if (Objects.nonNull(getCustomerByUsername(customer.getUsername()))) {
                throw new ProcedureException(CustomerErrorCode.FAILURE_ADD_CUSTOMER_USERNAME_HAS_EXISTS);
            }
            customer.setOpenId(UuidUtil.getUuid());
        }

        Long result = customerWriter.save(Customer.class, JSON.toJSONString(customer));
        CustomerCheckUtil.checkSaveResult(result);
        if (!Objects.equals(result, 1L)) {
            customer.setId(result);
        }
    }

    public Customer getCustomerByUsername(String username) {
        if (StringUtils.isBlank(username)) {
            return null;
        }
        return customerReader.findByOptions(Customer.class, SqlOption.getInstance().whereEqual(DbKey.USERNAME, username).toString());
    }

    public CustomerIdentityInfo getPersonalByCustomerId(Long customerId) {
        SqlOption sqlOption = SqlOption.getInstance();
        sqlOption.whereEqual(DbKey.CUSTOMER_ID, customerId);
        return customerReader.findByOptions(CustomerIdentityInfo.class, sqlOption.toString());
    }

    public Long saveCustomerPersonal(CustomerIdentityInfo personalData) throws ProcedureException {
        Long customerId = personalData.getCustomerId();
        String ssn = personalData.getSsn();
        if (Objects.nonNull(ssn)) {
            SqlOption sqlOption = SqlOption.getInstance();
            sqlOption.whereEqual(DbKey.SSN, ssn);
            CustomerIdentityInfo dbPersonalData = customerReader.findByOptions(CustomerIdentityInfo.class, sqlOption.toString());
            if (Objects.nonNull(dbPersonalData) && !Objects.equals(customerId, dbPersonalData.getCustomerId())) {
                throw new ProcedureException(CustomerErrorCode.SSN_ALREADY_EXISTS);
            }
        }
        CustomerIdentityInfo identityInfoDb = commonReader.getEntityByCustomerId(CustomerIdentityInfo.class, customerId);
        Long currentTimestamp = DateUtil.getCurrentTimestamp();
        if (Objects.isNull(identityInfoDb)) {
            personalData.setCreatedAt(currentTimestamp);
        }

        // sync uniqueCode
        if (StringUtils.isNotBlank(ssn) && (Objects.isNull(identityInfoDb) || !Objects.equals(ssn, identityInfoDb.getSsn()))) {
            syncCustomerUniqueCode(customerId, ssn, null, null);
        }

        personalData.setUpdatedAt(currentTimestamp);
        return customerWriter.save(CustomerIdentityInfo.class, JSON.toJSONString(personalData));
    }

    public CustomerEmploymentInfo getCustomerEmploymentByCustomerId(Long customerId) {
        return commonReader.getEntityByCustomerId(CustomerEmploymentInfo.class, customerId);
    }

    public void saveCustomerEmployment(CustomerEmploymentInfo customerEmploymentInfo) throws ProcedureException {
        Long result = commonWriter.save(CustomerEmploymentInfo.class, JSON.toJSONString(customerEmploymentInfo));
        CustomerCheckUtil.checkSaveResult(result);
    }

    public CustomerPayrollInfo getCustomerPayrollByCustomerId(Long customerId) {
        return commonReader.getEntityByCustomerId(CustomerPayrollInfo.class, customerId);
    }

    public void saveCustomerPayroll(CustomerPayrollInfo payrollData) throws ProcedureException {
        Long result = commonWriter.save(payrollData);
        CustomerCheckUtil.checkSaveResult(result);
    }


    public List<CustomerBankAccount> listBankAccountByCustomerId(Long customerId) {
        SqlOption sqlOption = SqlOption.getInstance();
        sqlOption.whereEqual(DbKey.CUSTOMER_ID, customerId);
        return customerReader.findAllByOptions(CustomerBankAccount.class, sqlOption.toString());
    }

    public Long saveBankAccount(CustomerBankAccount bankAccount) throws ProcedureException {
        Long customerId = bankAccount.getCustomerId();
        CustomerBankAccount bankAccountDb = commonReader.getEntityByCustomerId(CustomerBankAccount.class, customerId);
        if (Objects.isNull(bankAccountDb)
                || (Objects.nonNull(bankAccount.getBankAccountNo()) && !Objects.equals(bankAccountDb.getBankAccountNo(), bankAccount.getBankAccountNo())
                || (Objects.nonNull(bankAccount.getBankRoutingNo()) && !Objects.equals(bankAccountDb.getBankRoutingNo(), bankAccount.getBankRoutingNo())))) {
            syncCustomerUniqueCode(customerId, null, bankAccount.getBankRoutingNo(), bankAccount.getBankRoutingNo());
        }

        Long result = customerWriter.save(CustomerBankAccount.class, JSON.toJSONString(bankAccount));
        CustomerCheckUtil.checkSaveResult(result);
        return result;
    }

    public CustomerBankAccount getBankAccountById(Long id) {
        return customerReader.findById(CustomerBankAccount.class, id, null);
    }

    public List<CustomerContactInfo> listCustomerContact(Long customerId) {
        return customerReader.findAllByOptions(CustomerContactInfo.class, SqlOption.getInstance().whereEqual(DbKey.CUSTOMER_ID, customerId).toString());
    }

    public void saveCustomerContactData(CustomerContactInfo contactData) throws ProcedureException {
        if (Objects.isNull(contactData) || Objects.isNull(contactData.getCustomerId()) || Objects.isNull(contactData.getType())) {
            throw new ProcedureException(CustomerErrorCode.PARAMETER_IS_INCOMPLETE);
        }

        SqlOption sqlOption = SqlOption.getInstance();
        Integer contactType = contactData.getType();
        Long customerId = contactData.getCustomerId();
        sqlOption.whereEqual(DbKey.CUSTOMER_ID, customerId);
        sqlOption.whereEqual(DbKey.TYPE, contactType);
        CustomerContactInfo contactDataDb = customerReader.findByOptions(CustomerContactInfo.class, sqlOption.toString());
        Long currentTimestamp = DateUtil.getCurrentTimestamp();
        if (Objects.isNull(contactDataDb)) {
            contactData.setCreatedAt(currentTimestamp);
        }
        contactData.setUpdatedAt(currentTimestamp);

        Long result = commonWriter.saveEntity(contactData);
        CustomerCheckUtil.checkSaveResult(result);
    }

    public CustomerContactInfo getCustomerContactData(Long customerId, Integer type) throws ProcedureException {
        if (Objects.isNull(customerId) || Objects.isNull(type)) {
            throw new ProcedureException(CustomerErrorCode.PARAMETER_IS_INCOMPLETE);
        }

        SqlOption option = SqlOption.getInstance();
        option.whereEqual(DbKey.CUSTOMER_ID, customerId);
        option.whereEqual(DbKey.TYPE, type);
        return commonReader.findByOptions(CustomerContactInfo.class, option.toString());
    }

    private void syncCustomerUniqueCode(Long customerId, String ssn, String routingNo, String accountNo) {
        if (StringUtils.isBlank(ssn)) {
            CustomerIdentityInfo identityInfo = commonReader.getEntityByCustomerId(CustomerIdentityInfo.class, customerId);
            if (Objects.nonNull(identityInfo)) {
                ssn = identityInfo.getSsn();
            }
        }

        if (StringUtils.isBlank(routingNo)) {
            CustomerBankAccount bankAccount = commonReader.getEntityByCustomerId(CustomerBankAccount.class, customerId);
            if (Objects.nonNull(bankAccount)) {
                routingNo = bankAccount.getBankRoutingNo();
                accountNo = bankAccount.getBankRoutingNo();
            }
        }

        String uniqueCode = CustomerUtil.generateUniqueCode(ssn, routingNo, accountNo);

        Customer customer = new Customer();
        customer.setId(customerId);
        customer.setUniqueCode(uniqueCode);
        customerWriter.save(customer);
    }

    public Customer getCustomerByCondition(String phone, String email) throws ProcedureException {

        if (StringUtils.isEmpty(phone)&&StringUtils.isEmpty(email)){
            throw new ProcedureException(CustomerErrorCode.CUSTOMER_IS_NOT_EXISTED);
        }

        SqlOption sqlOption = SqlOption.getInstance();

        if (!StringUtils.isEmpty(phone)) {
            sqlOption.whereEqual(DbKey.PHONE, phone);
        }

        if (!StringUtils.isEmpty(email)) {
            sqlOption.whereEqual(DbKey.EMAIL, email);
        }

        return commonReader.findByOptions(Customer.class, sqlOption.toString());
    }

}