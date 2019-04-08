package com.arbfintech.microservice.loan.entity;

import com.alibaba.fastjson.JSON;

import javax.persistence.*;

/**
 * @author Wade He
 */
@Entity
@Table(name="loan_bank")
public class Bank {

	@Id
	@Column(length = 10)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	
	@Column(length = 10)
	private Integer loanId;

	@Column(length = 32)
	private String bankName;

	@Column(length = 32)
	private String bankPhone;

	@Column(length = 32)
	private String bankAccountType;

	@Column(length = 32)
	private String bankRoutingNo;

	@Column(length = 32)
	private String bankAccountNo;

	@Column(length = 32)
	private String bankAccountOwner;

	@Column(length = 32)
	private String bankerName;

	@Column(length = 32)
	private Double bankAvailableBalance;

	@Column(length = 10)
	private String bankSessionId;

	@Column(length = 8)
	private Boolean isBankNameVerified;

	@Column(length = 8)
	private Boolean isBankAccountTypeVerified;

	@Column(length = 8)
	private Boolean isBankRoutingNumberVerified;

	@Column(length = 8)
	private Boolean isBankAccountNumberVerified;

	@Column(length = 8)
	private Boolean isBankPhoneNumberVerified;

	@Column(length = 8)
	private Boolean isBankAccountOwnerVerified;

	@Column(length = 8)
	private Boolean isPrimaryNameInAccount;

	@Column(length = 8)
	private Boolean isAccountUnlimited;

	@Column(length = 8)
	private Boolean isAddressMatched;

	@Column(length = 8)
	private Boolean isAccountException;

	@Column(length = 10)
	private String recentRequestCode;

	@Column(length = 32)
	private String recentRequestCodeStatus;

	@Column(length = 32)
	private String newRequestCode;

	@Column(length = 32)
	private Double newRequestCodeStatus;

	@Column(length = 32)
	private String bankException;

	@Column(length = 32)
	private Double amountOfOpenLoans;

	@Column(length = 32)
	private Integer numberOfOpenLoans;

	@Column
	@Lob
	private String bankDeposits;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getLoanId() {
		return loanId;
	}

	public void setLoanId(Integer loanId) {
		this.loanId = loanId;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getBankPhone() {
		return bankPhone;
	}

	public void setBankPhone(String bankPhone) {
		this.bankPhone = bankPhone;
	}

	public String getBankAccountType() {
		return bankAccountType;
	}

	public void setBankAccountType(String bankAccountType) {
		this.bankAccountType = bankAccountType;
	}

	public String getBankRoutingNo() {
		return bankRoutingNo;
	}

	public void setBankRoutingNo(String bankRoutingNo) {
		this.bankRoutingNo = bankRoutingNo;
	}

	public String getBankAccountNo() {
		return bankAccountNo;
	}

	public void setBankAccountNo(String bankAccountNo) {
		this.bankAccountNo = bankAccountNo;
	}

	public String getBankAccountOwner() {
		return bankAccountOwner;
	}

	public void setBankAccountOwner(String bankAccountOwner) {
		this.bankAccountOwner = bankAccountOwner;
	}

	public String getBankerName() {
		return bankerName;
	}

	public void setBankerName(String bankerName) {
		this.bankerName = bankerName;
	}

	public Double getBankAvailableBalance() {
		return bankAvailableBalance;
	}

	public void setBankAvailableBalance(Double bankAvailableBalance) {
		this.bankAvailableBalance = bankAvailableBalance;
	}

	public String getBankSessionId() {
		return bankSessionId;
	}

	public void setBankSessionId(String bankSessionId) {
		this.bankSessionId = bankSessionId;
	}

	public Boolean getBankNameVerified() {
		return isBankNameVerified;
	}

	public void setBankNameVerified(Boolean bankNameVerified) {
		isBankNameVerified = bankNameVerified;
	}

	public Boolean getBankAccountTypeVerified() {
		return isBankAccountTypeVerified;
	}

	public void setBankAccountTypeVerified(Boolean bankAccountTypeVerified) {
		isBankAccountTypeVerified = bankAccountTypeVerified;
	}

	public Boolean getBankRoutingNumberVerified() {
		return isBankRoutingNumberVerified;
	}

	public void setBankRoutingNumberVerified(Boolean bankRoutingNumberVerified) {
		isBankRoutingNumberVerified = bankRoutingNumberVerified;
	}

	public Boolean getBankAccountNumberVerified() {
		return isBankAccountNumberVerified;
	}

	public void setBankAccountNumberVerified(Boolean bankAccountNumberVerified) {
		isBankAccountNumberVerified = bankAccountNumberVerified;
	}

	public Boolean getBankPhoneNumberVerified() {
		return isBankPhoneNumberVerified;
	}

	public void setBankPhoneNumberVerified(Boolean bankPhoneNumberVerified) {
		isBankPhoneNumberVerified = bankPhoneNumberVerified;
	}

	public Boolean getBankAccountOwnerVerified() {
		return isBankAccountOwnerVerified;
	}

	public void setBankAccountOwnerVerified(Boolean bankAccountOwnerVerified) {
		isBankAccountOwnerVerified = bankAccountOwnerVerified;
	}

	public Boolean getPrimaryNameInAccount() {
		return isPrimaryNameInAccount;
	}

	public void setPrimaryNameInAccount(Boolean primaryNameInAccount) {
		isPrimaryNameInAccount = primaryNameInAccount;
	}

	public Boolean getAccountUnlimited() {
		return isAccountUnlimited;
	}

	public void setAccountUnlimited(Boolean accountUnlimited) {
		isAccountUnlimited = accountUnlimited;
	}

	public Boolean getAddressMatched() {
		return isAddressMatched;
	}

	public void setAddressMatched(Boolean addressMatched) {
		isAddressMatched = addressMatched;
	}

	public Boolean getAccountException() {
		return isAccountException;
	}

	public void setAccountException(Boolean accountException) {
		isAccountException = accountException;
	}

	public String getRecentRequestCode() {
		return recentRequestCode;
	}

	public void setRecentRequestCode(String recentRequestCode) {
		this.recentRequestCode = recentRequestCode;
	}

	public String getRecentRequestCodeStatus() {
		return recentRequestCodeStatus;
	}

	public void setRecentRequestCodeStatus(String recentRequestCodeStatus) {
		this.recentRequestCodeStatus = recentRequestCodeStatus;
	}

	public String getNewRequestCode() {
		return newRequestCode;
	}

	public void setNewRequestCode(String newRequestCode) {
		this.newRequestCode = newRequestCode;
	}

	public Double getNewRequestCodeStatus() {
		return newRequestCodeStatus;
	}

	public void setNewRequestCodeStatus(Double newRequestCodeStatus) {
		this.newRequestCodeStatus = newRequestCodeStatus;
	}

	public String getBankException() {
		return bankException;
	}

	public void setBankException(String bankException) {
		this.bankException = bankException;
	}

	public Double getAmountOfOpenLoans() {
		return amountOfOpenLoans;
	}

	public void setAmountOfOpenLoans(Double amountOfOpenLoans) {
		this.amountOfOpenLoans = amountOfOpenLoans;
	}

	public Integer getNumberOfOpenLoans() {
		return numberOfOpenLoans;
	}

	public void setNumberOfOpenLoans(Integer numberOfOpenLoans) {
		this.numberOfOpenLoans = numberOfOpenLoans;
	}

	public String getBankDeposits() {
		return bankDeposits;
	}

	public void setBankDeposits(String bankDeposits) {
		this.bankDeposits = bankDeposits;
	}

	@Override
    public String toString() {
        return JSON.toJSONString(this);
    }

	
}
