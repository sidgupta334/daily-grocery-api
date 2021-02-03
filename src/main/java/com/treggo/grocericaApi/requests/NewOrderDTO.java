package com.treggo.grocericaApi.requests;

import org.springframework.stereotype.Service;
import com.treggo.grocericaApi.enums.PaymentMethod;

import io.swagger.annotations.ApiModelProperty;

@Service
public class NewOrderDTO {

	private Long addressId;
	@ApiModelProperty(value = "Total after applying coupon code (If selected) OR same as cart Total")
	private Long finalTotal;
	@ApiModelProperty(value = "Mode of Payment", allowableValues = "COD, ONLINE")
	private PaymentMethod paymentMethod;
	private boolean safeDelivery;
	private String couponApplied;

	public NewOrderDTO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public NewOrderDTO(Long addressId, Long finalTotal, PaymentMethod paymentMethod, boolean safeDelivery,
			String couponApplied) {
		super();
		this.addressId = addressId;
		this.finalTotal = finalTotal;
		this.paymentMethod = paymentMethod;
		this.safeDelivery = safeDelivery;
		this.couponApplied = couponApplied;
	}

	public Long getAddressId() {
		return addressId;
	}

	public void setAddressId(Long addressId) {
		this.addressId = addressId;
	}

	public Long getFinalTotal() {
		return finalTotal;
	}

	public void setFinalTotal(Long finalTotal) {
		this.finalTotal = finalTotal;
	}

	public PaymentMethod getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(PaymentMethod paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public boolean isSafeDelivery() {
		return safeDelivery;
	}

	public void setSafeDelivery(boolean safeDelivery) {
		this.safeDelivery = safeDelivery;
	}

	public String getCouponApplied() {
		return couponApplied;
	}

	public void setCouponApplied(String couponApplied) {
		this.couponApplied = couponApplied;
	}

}
