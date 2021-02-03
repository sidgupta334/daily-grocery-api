package com.treggo.grocericaApi.responses;

import java.time.LocalDate;
import java.util.List;

import com.treggo.grocericaApi.enums.OrderStatus;
import com.treggo.grocericaApi.enums.PaymentMethod;

public class OrderResponse {

	private Long orderId;
	private Long userId;
	private Long vendorId;
	private String fullName;
	private String email;
	private OrderStatus orderStatus;
	private Long finalTotal;
	private Long netQuantity;
	private Long oldTotal;
	private Long discountApplied;
	private PaymentMethod paymentMethod;
	private String onlinePaymentMode;
	private String transactionId;
	private String cancellationReason;
	private boolean safeDelivery;
	private LocalDate deliveryDate;
	private LocalDate orderDate;
	private String address1;
	private String address2;
	private String city;
	private String pincode;
	private String state;
	private String country;
	private String mobile;
	private String message;
	private Long mappedTo;
	private String lattitude;
	private String longitude;
	private String mapUrl;
	private List<ProductCartResponse> products;

	public OrderResponse() {
		super();
		// TODO Auto-generated constructor stub
	}

	public OrderResponse(Long orderId, Long userId, Long vendorId, String fullName, String email,
			OrderStatus orderStatus, Long finalTotal, Long netQuantity, Long oldTotal, Long discountApplied,
			PaymentMethod paymentMethod, String onlinePaymentMode, String transactionId, String cancellationReason,
			boolean safeDelivery, LocalDate deliveryDate, LocalDate orderDate, String address1, String address2,
			String city, String pincode, String state, String country, String mobile, String message, Long mappedTo,
			String lattitude, String longitude, String mapUrl, List<ProductCartResponse> products) {
		super();
		this.orderId = orderId;
		this.userId = userId;
		this.vendorId = vendorId;
		this.fullName = fullName;
		this.email = email;
		this.orderStatus = orderStatus;
		this.finalTotal = finalTotal;
		this.netQuantity = netQuantity;
		this.oldTotal = oldTotal;
		this.discountApplied = discountApplied;
		this.paymentMethod = paymentMethod;
		this.onlinePaymentMode = onlinePaymentMode;
		this.transactionId = transactionId;
		this.cancellationReason = cancellationReason;
		this.safeDelivery = safeDelivery;
		this.deliveryDate = deliveryDate;
		this.orderDate = orderDate;
		this.address1 = address1;
		this.address2 = address2;
		this.city = city;
		this.pincode = pincode;
		this.state = state;
		this.country = country;
		this.mobile = mobile;
		this.message = message;
		this.mappedTo = mappedTo;
		this.lattitude = lattitude;
		this.longitude = longitude;
		this.mapUrl = mapUrl;
		this.products = products;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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

	public void setOldTotal(Long oldTotal) {
		this.oldTotal = oldTotal;
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

	public LocalDate getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(LocalDate orderDate) {
		this.orderDate = orderDate;
	}

	public String getAddress1() {
		return address1;
	}

	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	public String getAddress2() {
		return address2;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getPincode() {
		return pincode;
	}

	public void setPincode(String pincode) {
		this.pincode = pincode;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public List<ProductCartResponse> getProducts() {
		return products;
	}

	public void setProducts(List<ProductCartResponse> products) {
		this.products = products;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getLattitude() {
		return lattitude;
	}

	public void setLattitude(String lattitude) {
		this.lattitude = lattitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public Long getMappedTo() {
		return mappedTo;
	}

	public void setMappedTo(Long mappedTo) {
		this.mappedTo = mappedTo;
	}

	public String getMapUrl() {
		return mapUrl;
	}

	public void setMapUrl(String mapUrl) {
		this.mapUrl = mapUrl;
	}

	public Long getVendorId() {
		return vendorId;
	}

	public void setVendorId(Long vendorId) {
		this.vendorId = vendorId;
	}

}
