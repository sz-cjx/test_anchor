package com.arbfintech.microservice.loan.repository;

import com.arbfintech.microservice.loan.entity.Bank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Richard
 */
@Repository
public interface BankRepository
		extends JpaRepository<Bank, String>, JpaSpecificationExecutor<Bank> {

	Bank findByLoanId(Integer loanId);

	@Query("select loanId from Bank where bankAccountNo like ?1")
	List<Integer> findAllLoanIdsByAccountNumber(String bankAccountNo);
}
