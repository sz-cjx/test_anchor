package com.arbfintech.microservice.loan.repository;

import com.arbfintech.microservice.loan.entity.Employment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * @author Richard
 */
@Repository
public interface EmploymentRepository
		extends JpaRepository<Employment, String>, JpaSpecificationExecutor<Employment> {

	Employment findByLoanId(Integer loanId);
}
