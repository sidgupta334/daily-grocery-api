package com.treggo.grocericaApi.requests;

import io.swagger.annotations.ApiModelProperty;

public class DeliveryUserDTO {

	private Long userId;
	private Long vendorId;
	private String fullName;
	@ApiModelProperty(value = "Gender values", allowableValues = "male, female")
	private String gender;
	private String email;
	private String mobile;
	@ApiModelProperty(value = "Date of birth in yyyy-mm-dd format")
	private String dob;
	private String password;

	public DeliveryUserDTO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public DeliveryUserDTO(Long userId, Long vendorId, String fullName, String gender, String email, String mobile,
			String dob, String password) {
		super();
		this.userId = userId;
		this.vendorId = vendorId;
		this.fullName = fullName;
		this.gender = gender;
		this.email = email;
		this.mobile = mobile;
		this.dob = dob;
		this.password = password;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getVendorId() {
		return vendorId;
	}

	public void setVendorId(Long vendorId) {
		this.vendorId = vendorId;
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
