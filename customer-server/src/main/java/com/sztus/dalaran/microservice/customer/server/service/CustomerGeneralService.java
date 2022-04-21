package com.sztus.dalaran.microservice.customer.server.service;

import com.alibaba.fastjson.JSON;
import com.sztus.dalaran.microservice.customer.client.object.parameter.enumerate.CustomerErrorCode;
import com.sztus.dalaran.microservice.customer.server.domain.Customer;
import com.sztus.dalaran.microservice.customer.server.domain.CustomerBankAccountData;
import com.sztus.dalaran.microservice.customer.server.domain.CustomerContactData;
import com.sztus.dalaran.microservice.customer.server.domain.CustomerEmploymentData;
import com.sztus.dalaran.microservice.customer.server.domain.CustomerPersonalData;
import com.sztus.dalaran.microservice.customer.server.respository.reader.CommonReader;
import com.sztus.dalaran.microservice.customer.server.respository.reader.CustomerReader;
import com.sztus.dalaran.microservice.customer.server.respository.writer.CommonWriter;
import com.sztus.dalaran.microservice.customer.server.respository.writer.CustomerWriter;
import com.sztus.dalaran.microservice.customer.server.util.CustomerCheckUtil;
import com.sztus.framework.component.core.type.ProcedureException;
import com.sztus.framework.component.database.constant.ConditionTypeConst;
import com.sztus.framework.component.database.type.SqlOption;
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

    public CustomerContactData getCustomerContactByContact(String contactInformation) {
        SqlOption sqlOption = SqlOption.getInstance();
        sqlOption.whereFormat(ConditionTypeConst.AND, "contact_information = '%s'", contactInformation);
        return customerReader.findByOptions(CustomerContactData.class, sqlOption.toString());
    }

    public void saveCustomer(Customer customer) throws ProcedureException {
        Long id = customer.getId();
        String openId = customer.getOpenId();

        Customer customerDb = customerReader.getCustomerByCondition(id, openId);
        if (Objects.nonNull(customerDb)) {
            customer.setId(customerDb.getId());
            customer.setOpenId(customerDb.getOpenId());
        }

        Long result = customerWriter.save(Customer.class, JSON.toJSONString(customer));
        CustomerCheckUtil.checkSaveResult(result);
        if (!Objects.equals(result, 1L)) {
            customer.setId(result);
        }
    }


    public CustomerPersonalData getPersonalByCustomerId(Long customerId) {
        SqlOption instance = SqlOption.getInstance();
        instance.whereFormat(ConditionTypeConst.AND, "customer_id= %d", customerId);
        return customerReader.findByOptions(CustomerPersonalData.class, instance.toString());
    }

    public Long saveCustomerPersonal(CustomerPersonalData personalData) throws ProcedureException {
        if (Objects.nonNull(personalData.getSsn())){
            SqlOption option = SqlOption.getInstance();
            option.whereNotEqual("customer_id",personalData.getCustomerId());
            option.whereEqual("ssn",personalData.getSsn());
            CustomerPersonalData dbPersonalData = customerReader.findByOptions(CustomerPersonalData.class, option.toString());
            if (Objects.nonNull(dbPersonalData)){
                throw new ProcedureException(CustomerErrorCode.SSN_ALREADY_EXISTS);
            }
        }

        return customerWriter.save(CustomerPersonalData.class, JSON.toJSONString(personalData));
    }

    public CustomerEmploymentData getCustomerEmploymentByCustomerId(Long customerId) {
        return commonReader.getEntityByLoanId(CustomerEmploymentData.class, customerId);
    }

    public void saveCustomerEmployment(CustomerEmploymentData customerEmploymentData) throws ProcedureException {

        Long result = commonWriter.save(CustomerEmploymentData.class, JSON.toJSONString(customerEmploymentData));
        CustomerCheckUtil.checkSaveResult(result);

    }


    public List<CustomerBankAccountData> listBankAccountByCustomerId(Long customerId){
        SqlOption instance = SqlOption.getInstance();
        instance.whereFormat(ConditionTypeConst.AND, "customer_id= %d", customerId);
        return customerReader.findAllByOptions(CustomerBankAccountData.class, instance.toString());
    }

    public Long saveBankAccount(CustomerBankAccountData bankAccountData){
        return customerWriter.save(CustomerBankAccountData.class,JSON.toJSONString(bankAccountData));
    }

    public CustomerBankAccountData getBankAccountById(Long id){
        return customerReader.findById(CustomerBankAccountData.class, id, null);
    }
}
