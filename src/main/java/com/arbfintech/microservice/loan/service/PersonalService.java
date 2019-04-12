package com.arbfintech.microservice.loan.service;

import com.arbfintech.microservice.loan.entity.Personal;
import com.arbfintech.microservice.loan.repository.PersonalRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Wade
 */
@Service
public class PersonalService {

    private static final Logger logger = LoggerFactory.getLogger(PersonalService.class);

    @Autowired
    private PersonalRepository personalRepository;

    public Personal getDataByLoanId(Integer loanId){
        return personalRepository.findByLoanId(loanId);
    }

    public Personal savePersonal(Personal personal){
        logger.info("Save the personal Data");
        return personalRepository.save(personal);
    }
}
