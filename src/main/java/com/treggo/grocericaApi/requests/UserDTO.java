package com.treggo.grocericaApi.requests;

import io.swagger.annotations.ApiModelProperty;

public class UserDTO {

	private Long userId;
	private String fullName;
	@ApiModelProperty(value = "Gender values", allowableValues = "male, female")
	private String gender;
	private String email;
	
	@ApiModelProperty(value = "Type of user", allowableValues = "user,admin")
	private String userType;
	private String mobile;
	@ApiModelProperty(value = "Date of birth in yyyy-mm-dd format")
	private String dob;
	private String password;
	private String addressTitle;
	private String address1;
	private String address2;
	private String city;
	private String pincode;
	private String state;
	@ApiModelProperty(value = "Default will be India")
	private String country;
	

	
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

	public UserDTO(Long userId, String fullName, String gender, String email, String userType, String mobile,
			String dob, String password, String addressTitle, String address1, String address2, String city,
			String pincode, String state, String country) {
		super();
		this.userId = userId;
		this.fullName = fullName;
		this.gender = gender;
		this.email = email;
		this.userType = userType;
		this.mobile = mobile;
		this.dob = dob;
		this.password = password;
		this.addressTitle = addressTitle;
		this.address1 = address1;
		this.address2 = address2;
		this.city = city;
		this.pincode = pincode;
		this.state = state;
		this.country = country;
	}

	public UserDTO() {
		super();
		// TODO Auto-generated constructor stub
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

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
