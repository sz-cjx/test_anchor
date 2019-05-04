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

    Loan findTopByCategoryEqualsAndLoanStatusInOrderByCreateTimeDesc(Integer category, List<Integer> integerList);

    Loan findTopByCategoryEqualsAndLoanStatusInAndLockedOperatorNoIsNullOrderByCreateTimeDesc(Integer category, List<Integer> integerList);

    Loan findByContractNo(String contractNo);

    Loan findById(Integer contractid);
    
    Loan findByLeadId(Integer leadId);

    List<Loan> findAllByLoanStatus(int loanStatus);

    List<Loan> findAllByLoanStatusAndCreateTimeAfter(int status, Long recentDays);

    @Query(value="SELECT * FROM loan where withdrawn_code!='' AND update_time BETWEEN ?1 AND ?2",nativeQuery = true)
    public List<Loan> countWithDrawnloans(long startRecieveTime, long endReceiveTime);

    public List<Loan> findAllByLoanStatusAndLeadIdIn(Integer status, List<Integer> leadId);

    @Query(value = "select lead_id from loan where category=?1 and update_time BETWEEN ?2 AND ?3 ",nativeQuery =true)
    public List<Integer> listLeadIdByTimeRange(Integer category,long startTime, long endTime);

    List<Loan> findAllById(List<Integer> ids);

    List<Loan> findAllByCustomerIdentifyKey(String customerIdentifyKey);
}
