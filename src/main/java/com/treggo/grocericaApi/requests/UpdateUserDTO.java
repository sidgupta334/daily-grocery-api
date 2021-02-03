package com.treggo.grocericaApi.requests;

import io.swagger.annotations.ApiModelProperty;

public class UpdateUserDTO {

	private Long userId;
	private String fullName;
	@ApiModelProperty(value = "Gender values", allowableValues = "male, female")
	private String gender;
	private String email;
	private String dob;
	private String mobile;

	public UpdateUserDTO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public UpdateUserDTO(Long userId, String fullName, String gender, String email, String dob, String mobile) {
		super();
		this.userId = userId;
		this.fullName = fullName;
		this.gender = gender;
		this.email = email;
		this.dob = dob;
		this.mobile = mobile;
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

	public String getDob() {
		return dob;
	}

	public void setDob(String dob) {
		this.dob = dob;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

}
