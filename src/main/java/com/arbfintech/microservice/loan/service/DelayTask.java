package com.arbfintech.microservice.loan.service;

import com.arbfintech.framework.component.core.util.DateUtil;
import com.arbfintech.microservice.loan.client.GrabLoanFeignClient;
import com.arbfintech.microservice.loan.entity.Loan;
import com.arbfintech.microservice.loan.repository.LoanRepository;
import java.util.TimerTask;

public class DelayTask extends TimerTask {


    private Integer grabId;

    private String operatorNo;

    private String operatorName;

    private GrabLoanFeignClient grabLoanFeignClient;
    private LoanRepository loanRepository;


    public DelayTask(Integer grabId,String operatorNo,String operatorName,GrabLoanFeignClient grabLoanFeignClient,LoanRepository loanRepository){
        this.grabId = grabId;
        this.operatorNo = operatorNo;
        this.operatorName = operatorName;
        this.grabLoanFeignClient = grabLoanFeignClient;
        this.loanRepository = loanRepository;
    }

    @Override
    public void run() {

        String grabResult = grabLoanFeignClient.handleGrabLoanTimeOut(grabId);

        if (!("false").equals(grabResult) || !("error").equals(grabResult)) {

            Loan loan = loanRepository.findByContractNo(grabResult);

            if (loan != null) {
                loan.setLockedOperatorName(operatorName);
                loan.setOperatorNo(operatorNo);
                loan.setLockedOperatorNo(operatorNo);
                loan.setLockedAt(DateUtil.getCurrentTimestamp());
                loanRepository.save(loan);
            }
        }

    }
}
