package com.arbfintech.microservice.loan.service;

import com.arbfintech.microservice.loan.entity.Bank;
import com.arbfintech.microservice.loan.repository.BankRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Wade
 */
@Service
public class BankService {

    private static final Logger logger = LoggerFactory.getLogger(BankService.class);

    @Autowired
    private BankRepository bankRepository;

    public Bank getDataByLoanId(Integer loanId){
        return bankRepository.findByLoanId(loanId);
    }

    public Bank saveData(Bank bank){
        logger.info("Save the bank Data");
        return bankRepository.save(bank);
    }
}
