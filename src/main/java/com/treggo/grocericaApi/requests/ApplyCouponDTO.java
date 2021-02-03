package com.treggo.grocericaApi.requests;

public class ApplyCouponDTO {

	private String couponName;
	private Long cartId;
	private Long userId;

	public ApplyCouponDTO() {
		super();
		// TODO Auto-generated constructor stub
	}


	public ApplyCouponDTO(String couponName, Long cartId, Long userId) {
		super();
		this.couponName = couponName;
		this.cartId = cartId;
		this.userId = userId;
	}

	public String getCouponName() {
		return couponName;
	}

	public void setCouponName(String couponName) {
		this.couponName = couponName;
	}

	public Long getCartId() {
		return cartId;
	}

	public void setCartId(Long cartId) {
		this.cartId = cartId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

}
