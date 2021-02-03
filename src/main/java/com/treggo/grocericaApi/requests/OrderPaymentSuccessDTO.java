package com.treggo.grocericaApi.requests;

public class OrderPaymentSuccessDTO {

	private Long orderId;
	private String transactionId;
	private String paymentMode;

	public OrderPaymentSuccessDTO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public OrderPaymentSuccessDTO(Long orderId, String transactionId, String paymentMode) {
		super();
		this.orderId = orderId;
		this.transactionId = transactionId;
		this.paymentMode = paymentMode;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public String getPaymentMode() {
		return paymentMode;
	}

	public void setPaymentMode(String paymentMode) {
		this.paymentMode = paymentMode;
	}

}
