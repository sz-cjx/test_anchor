package com.sztus.azeroth.microservice.customer.server.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.sztus.azeroth.microservice.customer.client.object.parameter.enumerate.CustomerErrorCode;
import com.sztus.azeroth.microservice.customer.client.object.parameter.enumerate.OptInValueConst;
import com.sztus.azeroth.microservice.customer.client.object.util.EncryptUtil;
import com.sztus.azeroth.microservice.customer.server.object.domain.*;
import com.sztus.azeroth.microservice.customer.server.respository.reader.CommonReader;
import com.sztus.azeroth.microservice.customer.server.respository.reader.CustomerReader;
import com.sztus.azeroth.microservice.customer.server.respository.writer.CommonWriter;
import com.sztus.azeroth.microservice.customer.server.respository.writer.CustomerWriter;
import com.sztus.azeroth.microservice.customer.server.type.constant.DbKey;
import com.sztus.azeroth.microservice.customer.server.type.enumeration.CustomerContactTypeEnum;
import com.sztus.azeroth.microservice.customer.server.util.CustomerCheckUtil;
import com.sztus.azeroth.microservice.customer.server.util.CustomerUtil;
import com.sztus.azeroth.microservice.customer.server.util.HttpClientUtil;
import com.sztus.framework.component.core.constant.StatusConst;
import com.sztus.framework.component.core.type.ProcedureException;
import com.sztus.framework.component.core.util.DateUtil;
import com.sztus.framework.component.database.type.SqlOption;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${spring.config.activate.on-profile}")
    private String profiles;

    private static final String BANK_INFO_URL = "https://www.routingnumbers.info/api/data.json";

    public Customer getCustomerByCustomerId(Long customerId) {
        return customerReader.findById(Customer.class, customerId, null);
    }

    public CustomerContactInfo getCustomerContactByContact(String contactInformation) {
        SqlOption sqlOption = SqlOption.getInstance();
        sqlOption.whereEqual(DbKey.VALUE, EncryptUtil.AESEncode(contactInformation));
        sqlOption.whereIN(DbKey.TYPE, Lists.newArrayList(CustomerContactTypeEnum.CELL_PHONE.getValue(), CustomerContactTypeEnum.EMAIL.getValue()));
        sqlOption.whereEqual(DbKey.VERIFIED_STATUS,StatusConst.ENABLED);
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
        }
//        else {
//            // 校验username是否存在
//            CustomerAccount customerAccount = commonReader.getEntityByCustomerId(CustomerAccount.class, id);
//            if (Objects.nonNull(getCustomerByUsername(customerAccount.getUsername()))) {
//                throw new ProcedureException(CustomerErrorCode.FAILURE_ADD_CUSTOMER_USERNAME_HAS_EXISTS);
//            }
//            customer.setOpenId(UuidUtil.getUuid());
//        }

        Long customerId = customerWriter.save(Customer.class, JSON.toJSONString(customer));
        CustomerCheckUtil.checkSaveResult(customerId);
        if (!Objects.equals(customerId, 1L)) {
            customer.setId(customerId);
        }
    }

    public CustomerAccount getCustomerByUsername(String username) {
        if (StringUtils.isBlank(username)) {
            return null;
        }
        return customerReader.findByOptions(CustomerAccount.class, SqlOption.getInstance().whereEqual(DbKey.USERNAME, username).toString());
    }

    public Long saveCustomerPersonal(CustomerIdentityInfo personalData) throws ProcedureException {
        Long customerId = personalData.getCustomerId();
        String ssn = personalData.getSsn();
        SqlOption sqlOption = SqlOption.getInstance();
        sqlOption.whereEqual(DbKey.CUSTOMER_ID, customerId);
        CustomerIdentityInfo identityInfoDb = commonReader.getEntityWithDecrypt(CustomerIdentityInfo.class, sqlOption);
        Long currentTimestamp = DateUtil.getCurrentTimestamp();
        if (Objects.isNull(identityInfoDb)) {
            personalData.setCreatedAt(currentTimestamp);
        }

        // sync uniqueCode
        if (StringUtils.isNotBlank(ssn) && (Objects.isNull(identityInfoDb) || !Objects.equals(ssn, identityInfoDb.getSsn()))) {
            syncCustomerUniqueCode(customerId, ssn, null, null);
        }

        personalData.setUpdatedAt(currentTimestamp);
        return commonWriter.saveEncodeEntity(CustomerIdentityInfo.class, JSON.toJSONString(personalData));
    }

    public void saveCustomerEmployment(CustomerEmploymentInfo customerEmploymentInfo) throws ProcedureException {
        Long customerId = customerEmploymentInfo.getCustomerId();
        CustomerEmploymentInfo employmentInfoDb = commonReader.getEntityByCustomerId(CustomerEmploymentInfo.class, customerId);
        Long currentTimestamp = DateUtil.getCurrentTimestamp();
        if (Objects.isNull(employmentInfoDb)) {
            customerEmploymentInfo.setCreatedAt(currentTimestamp);
        }
        customerEmploymentInfo.setUpdatedAt(currentTimestamp);
        Long result = commonWriter.save(CustomerEmploymentInfo.class, JSON.toJSONString(customerEmploymentInfo));
        CustomerCheckUtil.checkSaveResult(result);
    }

    public void saveCustomerAccount(CustomerAccount customerAccount) throws ProcedureException {
        Long customerId = customerAccount.getCustomerId();
        CustomerAccount customerAccountDb = commonReader.getEntityByCustomerId(CustomerAccount.class, customerId);
        Long currentTimestamp = DateUtil.getCurrentTimestamp();
        if (Objects.isNull(customerAccountDb)) {
            customerAccount.setCreatedAt(currentTimestamp);
        }
        customerAccount.setUpdatedAt(currentTimestamp);
        Long result = commonWriter.save(CustomerAccount.class, JSON.toJSONString(customerAccount));
        CustomerCheckUtil.checkSaveResult(result);
    }

    public void saveCustomerPayroll(CustomerPayrollInfo payrollData) throws ProcedureException {
        Long customerId = payrollData.getCustomerId();
        CustomerPayrollInfo payrollDataDb = commonReader.getEntityByCustomerId(CustomerPayrollInfo.class, customerId);
        Long currentTimestamp = DateUtil.getCurrentTimestamp();
        if (Objects.isNull(payrollDataDb)) {
            payrollData.setCreatedAt(currentTimestamp);
        }
        payrollData.setUpdatedAt(currentTimestamp);
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
        SqlOption sqlOption = SqlOption.getInstance();
        sqlOption.whereEqual(DbKey.CUSTOMER_ID, customerId);
        sqlOption.whereEqual(DbKey.PRECEDENCE, 1);
        CustomerBankAccount bankAccountDb = customerReader.findByOptions(CustomerBankAccount.class, sqlOption.toString());

        String routingNo = bankAccount.getBankRoutingNo();
        Long currentTimestamp = DateUtil.getCurrentTimestamp();

        if (StringUtils.isNotBlank(routingNo)) {
            String request = BANK_INFO_URL + "?rn=" + routingNo;
            String response = HttpClientUtil.getRequest(null, request, null, false);
            JSONObject bankInfoJSon = JSON.parseObject(response);
            bankAccount.setBankName(bankInfoJSon.getString("customer_name"));

            String bankPhone = bankAccount.getBankPhone();
            if (StringUtils.isBlank(bankPhone)) {
                String telephone = bankInfoJSon.getString("telephone");
                if (StringUtils.isNotBlank(telephone)) {
                    bankAccount.setBankPhone(telephone.replaceAll("[^\\d]", ""));
                }
            }

            if (bankInfoJSon.getInteger("code") != 200 && !profiles.contains("release")) {
                bankAccount.setBankName("FIRST CITIZENS BANK");
                bankAccount.setBankPhone("8883234732");
            }

        }

        Long result;

        if (Objects.isNull(bankAccountDb)) {
            syncCustomerUniqueCode(customerId, null, bankAccount.getBankRoutingNo(), bankAccount.getBankAccountNo());
            bankAccount.setCreatedAt(currentTimestamp);
            bankAccount.setPrecedence(true);
            bankAccount.setBankAccountNo(EncryptUtil.AESEncode(bankAccount.getBankAccountNo()));
            result = customerWriter.saveEntity(bankAccount);
        } else {
            String bankAccountNo = EncryptUtil.AESDecode(bankAccountDb.getBankAccountNo());
            if (!Objects.equals(bankAccountNo, bankAccount.getBankAccountNo())
                    || !Objects.equals(bankAccountDb.getBankRoutingNo(), bankAccount.getBankRoutingNo())
            ) {
                syncCustomerUniqueCode(customerId, null, bankAccount.getBankRoutingNo(), bankAccount.getBankAccountNo());
            }
            bankAccountDb.setBankPhone(bankAccount.getBankPhone());
            bankAccountDb.setUpdatedAt(currentTimestamp);
            bankAccountDb.setBankName(bankAccount.getBankName());
            bankAccountDb.setBankAccountType(bankAccount.getBankAccountType());
            bankAccountDb.setBankAccountNo(EncryptUtil.AESEncode(bankAccount.getBankAccountNo()));
            bankAccountDb.setBankRoutingNo(bankAccount.getBankRoutingNo());
            bankAccountDb.setNote(bankAccount.getNote());
            result = customerWriter.saveEntity(bankAccountDb);
        }
        CustomerCheckUtil.checkSaveResult(result);
        return result;
    }

    public List<CustomerContactInfo> listCustomerContact(Long customerId) {
        return customerReader.findAllByOptions(CustomerContactInfo.class, SqlOption.getInstance().whereEqual(DbKey.CUSTOMER_ID, customerId).toString());
    }

    public void saveCustomerContactData(CustomerContactInfo contactData, Boolean isVerified) throws ProcedureException {
        if (Objects.isNull(contactData) || Objects.isNull(contactData.getCustomerId()) || Objects.isNull(contactData.getType())) {
            throw new ProcedureException(CustomerErrorCode.PARAMETER_IS_INCOMPLETE);
        }
        contactData = formatContactInfo(contactData);

        Integer contactType = contactData.getType();
        String contactInformation = contactData.getValue();
        if ((Objects.equals(contactType, CustomerContactTypeEnum.EMAIL.getValue()) ||
                Objects.equals(contactType, CustomerContactTypeEnum.ALTERNATIVE_EMAIL.getValue())) && StringUtils.isNotBlank(contactInformation)) {
            contactInformation = contactInformation.toLowerCase();
        }
        String value = EncryptUtil.AESEncode(contactInformation);
        isVerified = Objects.nonNull(isVerified) ? isVerified : false;
        Long customerId = contactData.getCustomerId();
        if (isVerified) {
            boolean isNotUnique = checkContactData(contactType, value, customerId);
            if (isNotUnique) {
                throw new ProcedureException(CustomerErrorCode.CONTACT_INFORMATION_USED);
            }
        }

        SqlOption sqlOption = SqlOption.getInstance();
        sqlOption.whereEqual(DbKey.CUSTOMER_ID, customerId);
        sqlOption.whereEqual(DbKey.TYPE, contactType);
        CustomerContactInfo contactDataDb = customerReader.findByOptions(CustomerContactInfo.class, sqlOption.toString());
        Long currentTimestamp = DateUtil.getCurrentTimestamp();
        if (Objects.isNull(contactDataDb)) {
            contactData.setOptionCombination(OptInValueConst.IS_MARKETING | OptInValueConst.IS_OPERATION);
            contactData.setCreatedAt(currentTimestamp);
        }
        contactData.setUpdatedAt(currentTimestamp);
        contactData.setValue(value);
        if (isVerified) {
            contactData.setVerifiedStatus(StatusConst.ENABLED);
        } else {
            if (Objects.isNull(contactDataDb) || Objects.isNull(contactDataDb.getVerifiedStatus())){
                contactData.setVerifiedStatus(StatusConst.DISABLED);
            }
        }
        Long result = commonWriter.saveEntity(contactData);
        CustomerCheckUtil.checkSaveResult(result);
    }

    private boolean checkContactData(Integer contactType, String contactInfo, Long customerId) {
        SqlOption sqlOption = SqlOption.getInstance();
        if (CustomerContactTypeEnum.CELL_PHONE.getValue().equals(contactType) || CustomerContactTypeEnum.EMAIL.getValue().equals(contactType)) {
            sqlOption.whereEqual(DbKey.VALUE, contactInfo);
            sqlOption.whereNotEqual(DbKey.CUSTOMER_ID, customerId);
            sqlOption.whereEqual(DbKey.VERIFIED_STATUS, StatusConst.ENABLED);
            CustomerContactInfo contactInfoDb = commonReader.findByOptions(CustomerContactInfo.class, sqlOption.toString());
            return Objects.nonNull(contactInfoDb);
        } else {
            return false;
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
                ssn = EncryptUtil.AESDecode(identityInfo.getSsn());
            }
        }

        if (StringUtils.isBlank(routingNo)) {
            CustomerBankAccount bankAccount = commonReader.getEntityByCustomerId(CustomerBankAccount.class, customerId);
            if (Objects.nonNull(bankAccount)) {
                routingNo = bankAccount.getBankRoutingNo();
                accountNo = EncryptUtil.AESDecode(bankAccount.getBankAccountNo());
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
        if (Objects.isNull(contactData.getValue())) {
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

    public CustomerBankAccount getBankByPrecedence(Long customerId) {
        SqlOption sqlOption = SqlOption.getInstance();
        sqlOption.whereEqual(DbKey.CUSTOMER_ID, customerId).whereEqual(DbKey.PRECEDENCE, 1);
        return customerReader.findByOptions(CustomerBankAccount.class, sqlOption.toString());
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

    public CustomerIdentityInfo getCustomerPersonalData(Long customerId) {
        SqlOption sqlOption = SqlOption.getInstance();
        sqlOption.whereEqual(DbKey.CUSTOMER_ID, customerId);
        return commonReader.getEntityWithDecrypt(CustomerIdentityInfo.class, sqlOption);
    }

    public CustomerBankAccount getCustomerBankAccountByCustomerId(Long customerId) {
        SqlOption sqlOption = SqlOption.getInstance();
        sqlOption.whereEqual(DbKey.CUSTOMER_ID, customerId);
        return commonReader.getEntityWithDecrypt(CustomerBankAccount.class, sqlOption);
    }

    public List<CustomerContactInfo> listCustomerContact(String email) {
        SqlOption sqlOption = SqlOption.getInstance();
        sqlOption.whereEqual(DbKey.VALUE, EncryptUtil.AESEncode(email));
        return commonReader.findAllByOptions(CustomerContactInfo.class, sqlOption.toString());
    }

    public void deleteCustomerInformation(List<Long> customerIds) {
        if (profiles.contains("release")) {
            return;
        }
        commonWriter.deleteByIdList(Customer.class, customerIds);

        commonWriter.deleteByIdList(CustomerAccount.class, customerIds);

        commonWriter.deleteByIdList(CustomerBankAccount.class, customerIds);

        commonWriter.deleteByIdList(CustomerContactInfo.class, customerIds);

        commonWriter.deleteByIdList(CustomerCreditEvaluation.class, customerIds);

        commonWriter.deleteByIdList(CustomerEmploymentInfo.class, customerIds);

        commonWriter.deleteByIdList(CustomerIbvAuthorizationRecord.class, customerIds);

        commonWriter.deleteByIdList(CustomerIdentityInfo.class, customerIds);

        commonWriter.deleteByIdList(CustomerPayrollInfo.class, customerIds);
    }
}
