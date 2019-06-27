package com.arbfintech.microservice.loan.service;

import com.arbfintech.framework.component.core.util.DateUtil;
import com.arbfintech.microservice.loan.client.GrabLoanFeignClient;
import com.arbfintech.microservice.loan.entity.Loan;
import com.arbfintech.microservice.loan.repository.LoanRepository;
import java.util.TimerTask;
import java.util.logging.Logger;

public class DelayTask extends TimerTask {


    private Integer grabId;
    private Integer levelFlag;
    private String operatorNo;


    private String operatorName;

    private GrabLoanFeignClient grabLoanFeignClient;
    private LoanRepository loanRepository;


    public DelayTask(Integer grabId,Integer levelFlag,String operatorNo,String operatorName,GrabLoanFeignClient grabLoanFeignClient,LoanRepository loanRepository){
        this.grabId = grabId;
        this.levelFlag = levelFlag;
        this.operatorNo = operatorNo;
        this.operatorName = operatorName;
        this.grabLoanFeignClient = grabLoanFeignClient;
        this.loanRepository = loanRepository;
    }

    @Override
    public void run() {

        String grabResult = grabLoanFeignClient.handleGrabLoanTimeOut(grabId,levelFlag);

        if (!("false").equals(grabResult) || !("error").equals(grabResult)) {

            Loan loan = loanRepository.findByContractNo(grabResult);

            if (levelFlag==1) {
                if (loan != null) {
                    loan.setLockedOperatorName(operatorName);
                    loan.setOperatorNo(operatorNo);
                    loan.setLockedOperatorNo(operatorNo);
                    loan.setLockedAt(DateUtil.getCurrentTimestamp());
                    loanRepository.save(loan);
                }
            }else {
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
}
