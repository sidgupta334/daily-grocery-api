package com.treggo.grocericaApi.responses;

public class DashboardResponse {

	private Long orderCount;
	private Long amount;

	public DashboardResponse() {
		super();
		// TODO Auto-generated constructor stub
	}

	public DashboardResponse(Long orderCount, Long amount) {
		super();
		this.orderCount = orderCount;
		this.amount = amount;
	}

	public Long getOrderCount() {
		return orderCount;
	}

	public void setOrderCount(Long orderCount) {
		this.orderCount = orderCount;
	}

	public Long getAmount() {
		return amount;
	}

	public void setAmount(Long amount) {
		this.amount = amount;
	}

}
