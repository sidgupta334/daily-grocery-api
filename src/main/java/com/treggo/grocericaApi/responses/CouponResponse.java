package com.treggo.grocericaApi.responses;

public class CouponResponse {

	private String couponName;
	private Long oldPrice;
	private Long updatedPrice;

	public CouponResponse() {
		super();
		// TODO Auto-generated constructor stub
	}

	public CouponResponse(String couponName, Long oldPrice, Long updatedPrice) {
		super();
		this.couponName = couponName;
		this.oldPrice = oldPrice;
		this.updatedPrice = updatedPrice;
	}

	public String getCouponName() {
		return couponName;
	}

	public void setCouponName(String couponName) {
		this.couponName = couponName;
	}

	public Long getOldPrice() {
		return oldPrice;
	}

	public void setOldPrice(Long oldPrice) {
		this.oldPrice = oldPrice;
	}

	public Long getUpdatedPrice() {
		return updatedPrice;
	}

	public void setUpdatedPrice(Long updatedPrice) {
		this.updatedPrice = updatedPrice;
	}

}
