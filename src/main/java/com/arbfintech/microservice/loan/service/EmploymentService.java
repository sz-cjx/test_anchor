package com.arbfintech.microservice.loan.service;

import com.arbfintech.microservice.loan.entity.Employment;
import com.arbfintech.microservice.loan.repository.EmploymentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Wade
 */
@Service
public class EmploymentService {

    private static final Logger logger = LoggerFactory.getLogger(EmploymentService.class);

    @Autowired
    private EmploymentRepository employmentRepository;

    public Employment getDataByLoanId(Integer loanId){
        return employmentRepository.findByLoanId(loanId);
    }

    public Employment saveData(Employment employment){
        logger.info("Save the employment Data");
        return employmentRepository.save(employment);
    }
}
