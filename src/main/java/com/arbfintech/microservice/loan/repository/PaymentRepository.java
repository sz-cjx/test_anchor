package com.arbfintech.microservice.loan.repository;

import com.arbfintech.microservice.loan.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * @author Richard
 */
@Repository
public interface PaymentRepository
		extends JpaRepository<Payment, String>, JpaSpecificationExecutor<Payment> {

	Payment findByLoanId(Integer loanId);
}
