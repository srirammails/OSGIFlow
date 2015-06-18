package com.flow.model.pjm;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import javax.persistence.Id;

@PersistenceCapable
public class Registration {

	@Id
	@Persistent(valueStrategy=IdGeneratorStrategy.INCREMENT)
	public long id;
	public String firstName;
	public String lastName;
	@PrimaryKey
	public String email;
	public String password;
	public String phone;
	public String courseDelivery;
	public String location;
	public String course;
	public String courseTerm;
	public String addr_line_1;
	public String addr_line_2;
	public String addr_line_3;
	public String county;
	public String bookingStatus;
	public String paymentStatus;
	public String amountPaid;
	
	
	
	
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
	public String getCourseDelivery() {
		return courseDelivery;
	}
	public void setCourseDelivery(String courseDelivery) {
		this.courseDelivery = courseDelivery;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getCourse() {
		return course;
	}
	public void setCourse(String course) {
		this.course = course;
	}
	public String getCourseTerm() {
		return courseTerm;
	}
	public void setCourseTerm(String courseTerm) {
		this.courseTerm = courseTerm;
	}
	public String getAddr_line_1() {
		return addr_line_1;
	}
	public void setAddr_line_1(String addr_line_1) {
		this.addr_line_1 = addr_line_1;
	}
	public String getAddr_line_2() {
		return addr_line_2;
	}
	public void setAddr_line_2(String addr_line_2) {
		this.addr_line_2 = addr_line_2;
	}
	public String getAddr_line_3() {
		return addr_line_3;
	}
	public void setAddr_line_3(String addr_line_3) {
		this.addr_line_3 = addr_line_3;
	}
	public String getCounty() {
		return county;
	}
	public void setCounty(String county) {
		this.county = county;
	}
	public String getBookingStatus() {
		return bookingStatus;
	}
	public void setBookingStatus(String bookingStatus) {
		this.bookingStatus = bookingStatus;
	}
	public String getPaymentStatus() {
		return paymentStatus;
	}
	public void setPaymentStatus(String paymentStatus) {
		this.paymentStatus = paymentStatus;
	}
	public String getAmountPaid() {
		return amountPaid;
	}
	public void setAmountPaid(String amountPaid) {
		this.amountPaid = amountPaid;
	}
	
	
	
}
