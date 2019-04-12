package com.arbfintech.microservice.loan.repository;

import com.arbfintech.microservice.loan.entity.Personal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Richard
 */
@Repository
public interface PersonalRepository
		extends JpaRepository<Personal, String>, JpaSpecificationExecutor<Personal> {

	Personal findByLoanId(Integer loanId);

	@Query("select loanId from Personal where fullName like ?1")
	List<Integer> findAllLoanIdsByFullName(String fullName);

	@Query("select loanId from Personal where ssn like ?1")
	List<Integer> findAllLoanIdsBySsn(String ssn);

	@Query("select loanId from Personal where email like ?1")
	List<Integer> findAllLoanIdsByEmail(String email);

	@Query("select loanId from Personal where homePhone like ?1")
	List<Integer> findAllLoanIdsByHomePhone(String homePhone);

	@Query("select loanId from Personal where mobilePhone like ?1")
	List<Integer> findAllLoanIdsByMobilePhone(String mobilePhone);
}
