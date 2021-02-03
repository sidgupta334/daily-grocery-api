package com.treggo.grocericaApi.responses;

import java.time.LocalDate;

import org.springframework.stereotype.Component;

import com.treggo.grocericaApi.enums.userType;

@Component
public class UserResponse {

	private Long userId;
	private String fullName;
	private String gender;
	private String email;
	private userType userType;
	private String mobile;
	private String dob;
	private String addressTitle;
	private String address1;
	private String address2;
	private String city;
	private String pincode;
	private String state;
	private String country;
	private LocalDate created_on;
	private boolean isValid;
	private String message;
	private Long vendorId;
	private String vendorName;

	public UserResponse() {
		super();
		// TODO Auto-generated constructor stub
	}

	public UserResponse(Long userId, String fullName, String gender, String email,
			com.treggo.grocericaApi.enums.userType userType, String mobile, String dob, String addressTitle,
			String address1, String address2, String city, String pincode, String state, String country,
			LocalDate created_on, boolean isValid, String message, Long vendorId, String vendorName) {
		super();
		this.userId = userId;
		this.fullName = fullName;
		this.gender = gender;
		this.email = email;
		this.userType = userType;
		this.mobile = mobile;
		this.dob = dob;
		this.addressTitle = addressTitle;
		this.address1 = address1;
		this.address2 = address2;
		this.city = city;
		this.pincode = pincode;
		this.state = state;
		this.country = country;
		this.created_on = created_on;
		this.isValid = isValid;
		this.message = message;
		this.vendorId = vendorId;
		this.vendorName = vendorName;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public boolean isValid() {
		return isValid;
	}

	public void setValid(boolean isValid) {
		this.isValid = isValid;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public userType getUserType() {
		return userType;
	}

	public void setUserType(userType userType) {
		this.userType = userType;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getDob() {
		return dob;
	}

	public void setDob(String dob) {
		this.dob = dob;
	}

	public LocalDate getCreated_on() {
		return created_on;
	}

	public void setCreated_on(LocalDate created_on) {
		this.created_on = created_on;
	}

	public String getAddressTitle() {
		return addressTitle;
	}

	public void setAddressTitle(String addressTitle) {
		this.addressTitle = addressTitle;
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

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getPincode() {
		return pincode;
	}

	public void setPincode(String pincode) {
		this.pincode = pincode;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public Long getVendorId() {
		return vendorId;
	}

	public void setVendorId(Long vendorId) {
		this.vendorId = vendorId;
	}

	public String getVendorName() {
		return vendorName;
	}

	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}

}
