package com.treggo.grocericaApi.requests;

public class SocialLoginDTO {

	private String userId;
	private String email;
	private String fullName;

	public SocialLoginDTO(String userId, String email, String fullName) {
		super();
		this.userId = userId;
		this.email = email;
		this.fullName = fullName;
	}

	public SocialLoginDTO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

}
