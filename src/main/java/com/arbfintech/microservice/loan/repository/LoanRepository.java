package com.arbfintech.microservice.loan.repository;

import com.arbfintech.microservice.loan.entity.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * @author Wade He
 */
@Repository
public interface LoanRepository extends JpaRepository<Loan, String>, JpaSpecificationExecutor<Loan> {

    List<Loan> findAllByContractNoIn(String[] contactNoArray);

    Loan findTopByCategoryEqualsAndStatusInOrderByCreateTimeDesc(Integer category, List<Integer> integerList);

    Loan findTopByCategoryEqualsAndStatusInAndLockedOperatorNoIsNullOrderByCreateTimeDesc(Integer category, List<Integer> integerList);

    Loan findByContractNo(String contractNo);

    Loan findById(Integer contractid);
    
    Loan findByLeadId(Integer leadId);

    List<Loan> findAllByStatus(int loanStatus);

    List<Loan> findAllByStatusAndCreateTimeAfter(int status, Date recentDays);

    @Query(value="SELECT * FROM loan where withdrawn_code!='' AND update_time BETWEEN ?1 AND ?2",nativeQuery = true)
    public List<Loan> countWithDrawnloans(long startRecieveTime, long endReceiveTime);

//    @Query(value = "SELECT * FROM loan where status=?1 and lead_id in ?2", nativeQuery = true)
//    public List<Loan> getLoanByStatusAndLeadId(Integer status, List<Integer> leadId);

    public List<Loan> findAllByStatusAndLeadIdIn(Integer status, List<Integer> leadId);
}
