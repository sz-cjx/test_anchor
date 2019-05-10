package com.arbfintech.microservice.loan.entity;

import com.alibaba.fastjson.JSON;

import javax.persistence.*;

/**
 * @author Wade He
 */
@Entity
@Table(name="loan_personal")
public class Personal {

	@Id
	@Column(length = 10)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	
	@Column(length = 10)
	private Integer loanId;

	@Column(length = 32)
	private String firstName;

	@Column(length = 32)
	private String middleName;

	@Column(length = 32)
	private String lastName;

	@Column
	private String fullName;

	@Column(length = 32)
	private String ssn;

	@Column(length = 32)
	private String birthday;

	@Column(length = 32)
	private String driverLicenseNo;

	@Column
	private String address1;

	@Column
	private String address2;

	@Column
	private String address;

	@Column
	private Integer addressMonths;

	@Column
	private Integer addressYears;

	private String rentOrOwn;

	private Boolean otherOffers;

	private Boolean isCitizen;

	@Column(length = 10)
	private String zip;

	@Column(length = 32)
	private String homePhone;

	@Column(length = 32)
	private String mobilePhone;

	@Column
	private String email;

	@Column(length = 32)
	private String fax;

	@Column(length = 32)
	private Double requestPrincipal;

	@Column(length = 32)
	private String gender;

	private Boolean isMilitary;

	@Column(length = 16)
	private String language;

	@Column(length = 32)
	private String city;

	@Column(length = 10)
	private String state;

	@Column
	@Lob
	private String reference;

	@Column(length = 32)
	private String personalSessionId;

	@Column(length = 6)
	private Boolean homePhoneDoNotCall;

	@Column(length = 6)
	private Boolean homePhoneUncontactable;

	@Column(length = 6)
	private Boolean mobilePhoneDoNotCall;

	@Column(length = 6)
	private Boolean mobilePhoneUncontactable;

	@Column(length = 6)
	private Boolean mobilePhoneSmsOperational;

	@Column(length = 6)
	private Boolean mobilePhoneSmsMarketing;

	@Column(length = 6)
	private Boolean emailOperational;

	@Column(length = 6)
	private Boolean emailMarketing;


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

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getSsn() {
		return ssn;
	}

	public void setSsn(String ssn) {
		this.ssn = ssn;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getDriverLicenseNo() {
		return driverLicenseNo;
	}

	public void setDriverLicenseNo(String driverLicenseNo) {
		this.driverLicenseNo = driverLicenseNo;
	}

	public String getAddress1() {
		return address1;
	}

	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	public String getAddress2() {
		return address2;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Integer getAddressMonths() {
		return addressMonths;
	}

	public void setAddressMonths(Integer addressMonths) {
		this.addressMonths = addressMonths;
	}

	public Integer getAddressYears() {
		return addressYears;
	}

	public void setAddressYears(Integer addressYears) {
		this.addressYears = addressYears;
	}

	public String getRentOrOwn() {
		return rentOrOwn;
	}

	public void setRentOrOwn(String rentOrOwn) {
		this.rentOrOwn = rentOrOwn;
	}

	public Boolean getOtherOffers() {
		return otherOffers;
	}

	public void setOtherOffers(Boolean otherOffers) {
		this.otherOffers = otherOffers;
	}

	public Boolean getIsCitizen() {
		return isCitizen;
	}

	public void setIsCitizen(Boolean citizen) {
		isCitizen = citizen;
	}

	public Boolean getIsMilitary() {
		return isMilitary;
	}

	public void setIsMilitary(Boolean military) {
		isMilitary = military;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public String getHomePhone() {
		return homePhone;
	}

	public void setHomePhone(String homePhone) {
		this.homePhone = homePhone;
	}

	public String getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public Double getRequestPrincipal() {
		return requestPrincipal;
	}

	public void setRequestPrincipal(Double requestPrincipal) {
		this.requestPrincipal = requestPrincipal;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public String getPersonalSessionId() {
		return personalSessionId;
	}

	public void setPersonalSessionId(String personalSessionId) {
		this.personalSessionId = personalSessionId;
	}

	public Boolean getHomePhoneDoNotCall() {
		return homePhoneDoNotCall;
	}

	public void setHomePhoneDoNotCall(Boolean homePhoneDoNotCall) {
		this.homePhoneDoNotCall = homePhoneDoNotCall;
	}

	public Boolean getMobilePhoneDoNotCall() {
		return mobilePhoneDoNotCall;
	}

	public void setMobilePhoneDoNotCall(Boolean mobilePhoneDoNotCall) {
		this.mobilePhoneDoNotCall = mobilePhoneDoNotCall;
	}

	public Boolean getHomePhoneUncontactable() {
		return homePhoneUncontactable;
	}

	public void setHomePhoneUncontactable(Boolean homePhoneUncontactable) {
		this.homePhoneUncontactable = homePhoneUncontactable;
	}

	public Boolean getMobilePhoneUncontactable() {
		return mobilePhoneUncontactable;
	}

	public void setMobilePhoneUncontactable(Boolean mobilePhoneUncontactable) {
		this.mobilePhoneUncontactable = mobilePhoneUncontactable;
	}

	public Boolean getMobilePhoneSmsOperational() {
		return mobilePhoneSmsOperational;
	}

	public void setMobilePhoneSmsOperational(Boolean mobilePhoneSmsOperational) {
		this.mobilePhoneSmsOperational = mobilePhoneSmsOperational;
	}

	public Boolean getMobilePhoneSmsMarketing() {
		return mobilePhoneSmsMarketing;
	}

	public void setMobilePhoneSmsMarketing(Boolean mobilePhoneSmsMarketing) {
		this.mobilePhoneSmsMarketing = mobilePhoneSmsMarketing;
	}

	public Boolean getEmailOperational() {
		return emailOperational;
	}

	public void setEmailOperational(Boolean emailOperational) {
		this.emailOperational = emailOperational;
	}

	public Boolean getEmailMarketing() {
		return emailMarketing;
	}

	public void setEmailMarketing(Boolean emailMarketing) {
		this.emailMarketing = emailMarketing;
	}

	@Override
    public String toString() {
        return JSON.toJSONString(this);
    }

	
}
