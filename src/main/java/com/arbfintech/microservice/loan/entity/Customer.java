package com.arbfintech.microservice.loan.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Customer {
	
	@Id
	@Column(length = 10)
    @GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	
	@Column(length = 32)
	private String ssn;
	
	@Column(length = 32)
	private String firstName;
	
	@Column(length = 32)
	private String middleName;
	
	@Column(length = 32)
	private String lastName;
	
	@Column(length = 16)
	private String gender;
	
	@Column
    @Temporal(TemporalType.DATE)
	private Date birthday;
	
	@Column(length = 256)
	private String address;
	
	@Column(length = 64)
	private String city;
	
	@Column(length = 16)
	private String state;
	
	@Column(length = 32)
	private String zip;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getSsn() {
		return ssn;
	}

	public void setSsn(String ssn) {
		this.ssn = ssn;
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

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
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

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	@Override
	public String toString() {
		return "Customer [id=" + id + ", ssn=" + ssn + ", firstName=" + firstName + ", middleName=" + middleName
				+ ", lastName=" + lastName + ", gender=" + gender + ", birthday=" + birthday + ", address=" + address
				+ ", city=" + city + ", state=" + state + ", zip=" + zip + "]";
	}
}
