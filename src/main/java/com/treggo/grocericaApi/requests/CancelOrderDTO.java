package com.treggo.grocericaApi.requests;

public class CancelOrderDTO {

	private Long orderId;
	private String cancellationReason;

	public CancelOrderDTO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public CancelOrderDTO(Long orderId, String cancellationReason) {
		super();
		this.orderId = orderId;
		this.cancellationReason = cancellationReason;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public String getCancellationReason() {
		return cancellationReason;
	}

	public void setCancellationReason(String cancellationReason) {
		this.cancellationReason = cancellationReason;
	}

}
