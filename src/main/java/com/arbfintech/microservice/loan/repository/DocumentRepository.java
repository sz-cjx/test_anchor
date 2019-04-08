package com.arbfintech.microservice.loan.repository;

import com.arbfintech.microservice.loan.entity.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * @author Richard
 */
@Repository
public interface DocumentRepository
		extends JpaRepository<Document, String>, JpaSpecificationExecutor<Document> {

	Document findByLoanId(Integer loanId);
}
