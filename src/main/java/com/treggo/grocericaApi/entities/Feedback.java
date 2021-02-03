package com.treggo.grocericaApi.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Feedback {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long feedbackId;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "order_id")
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JsonIgnore
	private Orders order;

	@Column(nullable = false)
	private Long deliveryFeedback;

	@Column(nullable = false)
	private Long itemsFeedback;

	@Column(nullable = false)
	private String feedback;

	public Feedback(Long feedbackId, Orders order, Long deliveryFeedback, Long itemsFeedback, String feedback) {
		super();
		this.feedbackId = feedbackId;
		this.order = order;
		this.deliveryFeedback = deliveryFeedback;
		this.itemsFeedback = itemsFeedback;
		this.feedback = feedback;
	}

	public Feedback() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Long getFeedbackId() {
		return feedbackId;
	}

	public void setFeedbackId(Long feedbackId) {
		this.feedbackId = feedbackId;
	}

	public Orders getOrder() {
		return order;
	}

	public void setOrder(Orders order) {
		this.order = order;
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
