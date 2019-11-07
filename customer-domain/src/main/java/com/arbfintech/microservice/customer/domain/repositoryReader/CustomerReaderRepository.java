package com.arbfintech.microservice.customer.domain.repositoryReader;

import com.arbfintech.microservice.customer.domain.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerReaderRepository extends JpaRepository<Customer,Long>, JpaSpecificationExecutor<Customer> {
    Customer findByEmail(String email);

    Customer findBySsnAndEmail(String ssn, String email);

    Customer findByEmailAndId(String email, Long customerId);

}
