package com.treggo.grocericaApi.entities;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.treggo.grocericaApi.enums.OrderStatus;
import com.treggo.grocericaApi.enums.PaymentMethod;

@Entity
public class Orders {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long orderId;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "user_id")
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JsonIgnore
	private Users user;

	@Column(nullable = false)
	private Long addressId;

	@Enumerated(EnumType.STRING)
	private OrderStatus orderStatus;

	@Column(nullable = false)
	private Long finalTotal;

	@Column(nullable = false)
	private Long netQuantity;

	@Column(nullable = false)
	private Long oldTotal;

	@Column
	private Long vendorId;

	@Column(columnDefinition = "TEXT", nullable = false)
	private String productIds;

	@Column(columnDefinition = "TEXT", nullable = false)
	private String productQuantities;

	@Column(nullable = false)
	private Long discountApplied;

	@Enumerated(EnumType.STRING)
	@Column
	private PaymentMethod paymentMethod;

	@Column
	private String onlinePaymentMode;

	@Column
	private String transactionId;

	@Column
	private String cancellationReason;

	@Column
	private boolean safeDelivery;

	@Column
	private LocalDate deliveryDate;

	@Column
	private Long mappedTo;

	@Column
	private String couponApplied;

	@Column(nullable = false)
	private LocalDate orderDate;

	public Orders() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Orders(Long orderId, Users user, Long addressId, OrderStatus orderStatus, Long finalTotal, Long netQuantity,
			Long oldTotal, Long vendorId, String productIds, String productQuantities, Long discountApplied,
			PaymentMethod paymentMethod, String onlinePaymentMode, String transactionId, String cancellationReason,
			boolean safeDelivery, LocalDate deliveryDate, Long mappedTo, String couponApplied, LocalDate orderDate) {
		super();
		this.orderId = orderId;
		this.user = user;
		this.addressId = addressId;
		this.orderStatus = orderStatus;
		this.finalTotal = finalTotal;
		this.netQuantity = netQuantity;
		this.oldTotal = oldTotal;
		this.vendorId = vendorId;
		this.productIds = productIds;
		this.productQuantities = productQuantities;
		this.discountApplied = discountApplied;
		this.paymentMethod = paymentMethod;
		this.onlinePaymentMode = onlinePaymentMode;
		this.transactionId = transactionId;
		this.cancellationReason = cancellationReason;
		this.safeDelivery = safeDelivery;
		this.deliveryDate = deliveryDate;
		this.mappedTo = mappedTo;
		this.couponApplied = couponApplied;
		this.orderDate = orderDate;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public Users getUser() {
		return user;
	}

	public void setUser(Users user) {
		this.user = user;
	}

	public Long getAddressId() {
		return addressId;
	}

	public void setAddressId(Long addressId) {
		this.addressId = addressId;
	}

	public OrderStatus getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(OrderStatus orderStatus) {
		this.orderStatus = orderStatus;
	}

	public Long getFinalTotal() {
		return finalTotal;
	}

	public void setFinalTotal(Long finalTotal) {
		this.finalTotal = finalTotal;
	}

	public Long getNetQuantity() {
		return netQuantity;
	}

	public void setNetQuantity(Long netQuantity) {
		this.netQuantity = netQuantity;
	}

	public Long getOldTotal() {
		return oldTotal;
	}

	public void setTotal(Long oldTotal) {
		this.oldTotal = oldTotal;
	}

	public String getProductIds() {
		return productIds;
	}

	public void setProductIds(String productIds) {
		this.productIds = productIds;
	}

	public String getProductQuantities() {
		return productQuantities;
	}

	public void setProductQuantities(String productQuantities) {
		this.productQuantities = productQuantities;
	}

	public Long getDiscountApplied() {
		return discountApplied;
	}

	public void setDiscountApplied(Long discountApplied) {
		this.discountApplied = discountApplied;
	}

	public PaymentMethod getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(PaymentMethod paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public String getOnlinePaymentMode() {
		return onlinePaymentMode;
	}

	public void setOnlinePaymentMode(String onlinePaymentMode) {
		this.onlinePaymentMode = onlinePaymentMode;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public String getCancellationReason() {
		return cancellationReason;
	}

	public void setCancellationReason(String cancellationReason) {
		this.cancellationReason = cancellationReason;
	}

	public boolean isSafeDelivery() {
		return safeDelivery;
	}

	public void setSafeDelivery(boolean safeDelivery) {
		this.safeDelivery = safeDelivery;
	}

	public LocalDate getDeliveryDate() {
		return deliveryDate;
	}

	public void setDeliveryDate(LocalDate deliveryDate) {
		this.deliveryDate = deliveryDate;
	}

	public Long getMappedTo() {
		return mappedTo;
	}

	public void setMappedTo(Long mappedTo) {
		this.mappedTo = mappedTo;
	}

	public void setOldTotal(Long oldTotal) {
		this.oldTotal = oldTotal;
	}

	public LocalDate getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(LocalDate orderDate) {
		this.orderDate = orderDate;
	}

	public Long getVendorId() {
		return vendorId;
	}

	public void setVendorId(Long vendorId) {
		this.vendorId = vendorId;
	}

	public String getCouponApplied() {
		return couponApplied;
	}

	public void setCouponApplied(String couponApplied) {
		this.couponApplied = couponApplied;
	}

}
