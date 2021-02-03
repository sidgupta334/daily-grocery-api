package com.treggo.grocericaApi.responses;

import com.treggo.grocericaApi.enums.userType;

public class LoginResponse {

	Long userId;
	String token;
	String message;
	String fullName;
	String email;
	userType userType;
	String mobile;
	String dob;

	public LoginResponse() {
		super();
		// TODO Auto-generated constructor stub
	}

	public LoginResponse(Long userId, String token, String message, String fullName, String email,
			com.treggo.grocericaApi.enums.userType userType, String mobile, String dob) {
		super();
		this.userId = userId;
		this.token = token;
		this.message = message;
		this.fullName = fullName;
		this.email = email;
		this.userType = userType;
		this.mobile = mobile;
		this.dob = dob;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
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

}
