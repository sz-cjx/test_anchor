package com.arbfintech.microservice.loan.repository;

import com.arbfintech.microservice.loan.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Eenest Gou
 */
@Repository
public interface CustomerRepository extends JpaRepository<Customer, String>, JpaSpecificationExecutor<Customer> {

    List<Customer> findAllBySsnIn(List<String> ssns);
    
    Customer findById(Integer customerId);
    Customer findBySsn(String ssn);

}
