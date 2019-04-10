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

	@Column(length = 6)
	private String verifiedBy;

	@Column(length = 16)
	private String verifiedOn;

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
	private Boolean bankNameVerified;

	@Column(length = 8)
	private Boolean bankAccountTypeVerified;

	@Column(length = 8)
	private Boolean bankRoutingNumberVerified;

	@Column(length = 8)
	private Boolean bankAccountNumberVerified;

	@Column(length = 8)
	private Boolean bankPhoneNumberVerified;

	@Column(length = 8)
	private Boolean bankAccountOwnerVerified;

	@Column(length = 8)
	private Boolean primaryNameInAccount;

	@Column(length = 8)
	private Boolean accountUnlimited;

	@Column(length = 8)
	private Boolean addressMatched;

	@Column(length = 8)
	private Boolean accountException;

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

	public String getVerifiedBy() {
		return verifiedBy;
	}

	public void setVerifiedBy(String verifiedBy) {
		this.verifiedBy = verifiedBy;
	}

	public String getVerifiedOn() {
		return verifiedOn;
	}

	public void setVerifiedOn(String verifiedOn) {
		this.verifiedOn = verifiedOn;
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
		return bankNameVerified;
	}

	public void setBankNameVerified(Boolean bankNameVerified) {
		this.bankNameVerified = bankNameVerified;
	}

	public Boolean getBankAccountTypeVerified() {
		return bankAccountTypeVerified;
	}

	public void setBankAccountTypeVerified(Boolean bankAccountTypeVerified) {
		this.bankAccountTypeVerified = bankAccountTypeVerified;
	}

	public Boolean getBankRoutingNumberVerified() {
		return bankRoutingNumberVerified;
	}

	public void setBankRoutingNumberVerified(Boolean bankRoutingNumberVerified) {
		this.bankRoutingNumberVerified = bankRoutingNumberVerified;
	}

	public Boolean getBankAccountNumberVerified() {
		return bankAccountNumberVerified;
	}

	public void setBankAccountNumberVerified(Boolean bankAccountNumberVerified) {
		this.bankAccountNumberVerified = bankAccountNumberVerified;
	}

	public Boolean getBankPhoneNumberVerified() {
		return bankPhoneNumberVerified;
	}

	public void setBankPhoneNumberVerified(Boolean bankPhoneNumberVerified) {
		this.bankPhoneNumberVerified = bankPhoneNumberVerified;
	}

	public Boolean getBankAccountOwnerVerified() {
		return bankAccountOwnerVerified;
	}

	public void setBankAccountOwnerVerified(Boolean bankAccountOwnerVerified) {
		this.bankAccountOwnerVerified = bankAccountOwnerVerified;
	}

	public Boolean getPrimaryNameInAccount() {
		return primaryNameInAccount;
	}

	public void setPrimaryNameInAccount(Boolean primaryNameInAccount) {
		this.primaryNameInAccount = primaryNameInAccount;
	}

	public Boolean getAccountUnlimited() {
		return accountUnlimited;
	}

	public void setAccountUnlimited(Boolean accountUnlimited) {
		this.accountUnlimited = accountUnlimited;
	}

	public Boolean getAddressMatched() {
		return addressMatched;
	}

	public void setAddressMatched(Boolean addressMatched) {
		this.addressMatched = addressMatched;
	}

	public Boolean getAccountException() {
		return accountException;
	}

	public void setAccountException(Boolean accountException) {
		this.accountException = accountException;
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
