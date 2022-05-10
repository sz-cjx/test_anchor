package com.sztus.azeroth.microservice.customer.server.service;

import com.alibaba.fastjson.JSON;
import com.sztus.azeroth.microservice.customer.client.object.parameter.enumerate.CustomerErrorCode;
import com.sztus.azeroth.microservice.customer.server.object.domain.*;
import com.sztus.azeroth.microservice.customer.server.respository.reader.CommonReader;
import com.sztus.azeroth.microservice.customer.server.respository.reader.CustomerReader;
import com.sztus.azeroth.microservice.customer.server.respository.writer.CommonWriter;
import com.sztus.azeroth.microservice.customer.server.respository.writer.CustomerWriter;
import com.sztus.azeroth.microservice.customer.server.type.constant.DbKey;
import com.sztus.azeroth.microservice.customer.server.type.enumeration.CustomerContactTypeEnum;
import com.sztus.azeroth.microservice.customer.server.util.CustomerCheckUtil;
import com.sztus.azeroth.microservice.customer.server.util.CustomerUtil;
import com.sztus.framework.component.core.type.ProcedureException;
import com.sztus.framework.component.core.util.DateUtil;
import com.sztus.framework.component.core.util.EnumUtil;
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

        Long customerId = customerWriter.save(Customer.class, JSON.toJSONString(customer));
        CustomerCheckUtil.checkSaveResult(customerId);
        if (!Objects.equals(customerId, 1L)) {
            customer.setId(customerId);
        }
    }

    public Customer getCustomerByUsername(String username) {
        if (StringUtils.isBlank(username)) {
            return null;
        }
        return customerReader.findByOptions(Customer.class, SqlOption.getInstance().whereEqual(DbKey.USERNAME, username).toString());
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

    public void saveCustomerEmployment(CustomerEmploymentInfo customerEmploymentInfo) throws ProcedureException {
        Long result = commonWriter.save(CustomerEmploymentInfo.class, JSON.toJSONString(customerEmploymentInfo));
        CustomerCheckUtil.checkSaveResult(result);
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

    public List<CustomerContactInfo> listCustomerContact(Long customerId) {
        return customerReader.findAllByOptions(CustomerContactInfo.class, SqlOption.getInstance().whereEqual(DbKey.CUSTOMER_ID, customerId).toString());
    }

    public void saveCustomerContactData(CustomerContactInfo contactData) throws ProcedureException {
        if (Objects.isNull(contactData) || Objects.isNull(contactData.getCustomerId()) || Objects.isNull(contactData.getType())) {
            throw new ProcedureException(CustomerErrorCode.PARAMETER_IS_INCOMPLETE);
        }

        contactData = formatContactInfo(contactData);

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

        String value = contactData.getValue();
        if (StringUtils.isNotBlank(value)) {
            Customer customer = getCustomerByCustomerId(customerId);

            if (Objects.isNull(customer)) {
                throw new ProcedureException(CustomerErrorCode.CUSTOMER_IS_NOT_EXISTED);
            }

            CustomerContactTypeEnum customerContactTypeEnum = EnumUtil.getByValue(CustomerContactTypeEnum.class, contactType);
            switch (customerContactTypeEnum) {
                case EMAIL: {
                    customer.setEmail(value);
                    saveCustomer(customer);
                    break;
                }
                case CELL_PHONE: {
                    customer.setPhone(value);
                    saveCustomer(customer);
                    break;
                }
                default:
                    break;
            }
        }

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

        if (StringUtils.isEmpty(phone) && StringUtils.isEmpty(email)) {
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

    private CustomerContactInfo formatContactInfo(CustomerContactInfo contactData) throws ProcedureException {
        Integer type = contactData.getType();
        if (Objects.isNull(contactData.getValue())){
            return contactData;
        }
        if (CustomerContactTypeEnum.HOME_PHONE.getValue().equals(type) || CustomerContactTypeEnum.CELL_PHONE.getValue().equals(type) || CustomerContactTypeEnum.ALTERNATIVE_PHONE.getValue().equals(type)) {
            contactData.setValue(CustomerUtil.formatNumber(contactData.getValue()));
        } else if (CustomerContactTypeEnum.EMAIL.getValue().equals(type) || CustomerContactTypeEnum.ALTERNATIVE_EMAIL.getValue().equals(type)) {
            contactData.setValue(CustomerUtil.formatString(contactData.getValue()));
        } else {
            throw new ProcedureException(CustomerErrorCode.UNKNOWN_CONTACT_TYPE);
        }
        return contactData;
    }

    public <T> T getEntity(Class<T> tClass, Long id) throws ProcedureException {
        if (Objects.isNull(id)) {
            throw new ProcedureException(CustomerErrorCode.PARAMETER_IS_INCOMPLETE);
        }

        SqlOption sqlOption = SqlOption.getInstance();

        if (tClass.equals(Customer.class) ||
                tClass.equals(CustomerBankAccount.class) ||
                tClass.equals(CustomerIbvAuthorizationRecord.class)) {
            sqlOption.whereEqual(DbKey.ID, id);
        } else {
            sqlOption.whereEqual(DbKey.CUSTOMER_ID, id);
        }
        return customerReader.findByOptions(tClass, sqlOption.toString());
    }

    public void saveCreditEvaluation(CustomerCreditEvaluation creditEvaluation) throws ProcedureException {
        creditEvaluation.setUpdatedAt(DateUtil.getCurrentTimestamp());
        Long result = commonWriter.saveEntity(creditEvaluation);
        CustomerCheckUtil.checkSaveResult(result);
    }
}
