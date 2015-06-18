package com.flow.model.pjm;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import javax.persistence.Id;

@PersistenceCapable
public class Tutor {

	@Id
	@Persistent(valueStrategy=IdGeneratorStrategy.INCREMENT)
	public long id;
	public String firstName;
	public String lastName;
	@PrimaryKey
	public String email;
	public String password;
	public String phone;
	public String cvfile;
	public String message;
	public String contarctStatus;
	public String from;
	public String to;
	public String rate;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getCvfile() {
		return cvfile;
	}
	public void setCvfile(String cvfile) {
		this.cvfile = cvfile;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getContarctStatus() {
		return contarctStatus;
	}
	public void setContarctStatus(String contarctStatus) {
		this.contarctStatus = contarctStatus;
	}
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
	public String getRate() {
		return rate;
	}
	public void setRate(String rate) {
		this.rate = rate;
	}
	
	
	
	
}
