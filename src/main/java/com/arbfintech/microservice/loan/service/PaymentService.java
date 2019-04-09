package com.arbfintech.microservice.loan.service;

import com.arbfintech.microservice.loan.entity.Payment;
import com.arbfintech.microservice.loan.repository.PaymentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Wade
 */
@Service
public class PaymentService {

    private static final Logger logger = LoggerFactory.getLogger(PaymentService.class);

    @Autowired
    private PaymentRepository paymentRepository;

    public Payment getDataByLoanId(Integer loanId){
        return paymentRepository.findByLoanId(loanId);
    }

    public Payment saveData(Payment payment){
        logger.info("Save the payment Data");
        return paymentRepository.save(payment);
    }
}
