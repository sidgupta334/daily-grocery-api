package com.treggo.grocericaApi.requests;

public class FeedbackDTO {

	private Long orderId;
	private Long deliveryFeedback;
	private Long itemsFeedback;
	private String feedback;

	public FeedbackDTO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public FeedbackDTO(Long orderId, Long deliveryFeedback, Long itemsFeedback, String feedback) {
		super();
		this.orderId = orderId;
		this.deliveryFeedback = deliveryFeedback;
		this.itemsFeedback = itemsFeedback;
		this.feedback = feedback;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public Long getDeliveryFeedback() {
		return deliveryFeedback;
	}

	public void setDeliveryFeedback(Long deliveryFeedback) {
		this.deliveryFeedback = deliveryFeedback;
	}

	public Long getItemsFeedback() {
		return itemsFeedback;
	}

	public void setItemsFeedback(Long itemsFeedback) {
		this.itemsFeedback = itemsFeedback;
	}

	public String getFeedback() {
		return feedback;
	}

	public void setFeedback(String feedback) {
		this.feedback = feedback;
	}

}
