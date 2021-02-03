package com.treggo.grocericaApi.requests;

public class RegisterOTPValidation {

	private Long userId;
	private String otp;

	public RegisterOTPValidation() {
		super();
		// TODO Auto-generated constructor stub
	}

	public RegisterOTPValidation(Long userId, String otp) {
		super();
		this.userId = userId;
		this.otp = otp;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getOtp() {
		return otp;
	}

	public void setOtp(String otp) {
		this.otp = otp;
	}

}
