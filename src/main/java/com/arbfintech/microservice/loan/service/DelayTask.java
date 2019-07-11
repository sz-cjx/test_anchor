package com.arbfintech.microservice.loan.service;

import com.arbfintech.framework.component.core.util.DateUtil;
import com.arbfintech.microservice.loan.client.GrabLoanFeignClient;
import com.arbfintech.microservice.loan.entity.Loan;
import com.arbfintech.microservice.loan.repository.LoanRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.TimerTask;
import java.util.logging.Logger;

public class DelayTask extends TimerTask {


    private Integer grabId;
    private Integer levelFlag;
    private String operatorNo;
    private String contractNo;

    private String operatorName;

    private GrabLoanFeignClient grabLoanFeignClient;
    private LoanRepository loanRepository;
    @Autowired LoanService loanService;


    public DelayTask(Integer grabId, Integer levelFlag, String contractNo, String operatorNo, String operatorName, GrabLoanFeignClient grabLoanFeignClient, LoanRepository loanRepository) {
        this.grabId = grabId;
        this.levelFlag = levelFlag;
        this.operatorNo = operatorNo;
        this.operatorName = operatorName;
        this.grabLoanFeignClient = grabLoanFeignClient;
        this.loanRepository = loanRepository;
        this.contractNo = contractNo;
    }

    @Override
    public void run() {

        String grabResult = grabLoanFeignClient.handleGrabLoanTimeOut(grabId, levelFlag);

        if (!("false").equals(grabResult) || !("error").equals(grabResult)) {

            Loan loan = loanRepository.findByContractNo(grabResult);

            if (levelFlag == 1) {
                if (loan != null) {
                    loanService.lockLoan(contractNo, operatorNo, operatorName);
                }
            } else {
                if (loan != null) {
                    loanService.lockLoan(contractNo, operatorNo, operatorName);
                }
            }
        }

    }
}
