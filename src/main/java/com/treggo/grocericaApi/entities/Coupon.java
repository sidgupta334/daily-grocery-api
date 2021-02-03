package com.treggo.grocericaApi.entities;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Coupon {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long couponId;

	@Column(nullable = false)
	private String couponName;

	@Column(nullable = true)
	private Long percentageDiscount;

	@Column(nullable = false)
	private Long maxDiscount;

	@Column
	private Long vendorId;

	@Column(nullable = false)
	private LocalDate created_on;

	public Coupon() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Coupon(Long couponId, String couponName, Long percentageDiscount, Long maxDiscount, Long vendorId,
			LocalDate created_on) {
		super();
		this.couponId = couponId;
		this.couponName = couponName;
		this.percentageDiscount = percentageDiscount;
		this.maxDiscount = maxDiscount;
		this.vendorId = vendorId;
		this.created_on = created_on;
	}

	public Long getCouponId() {
		return couponId;
	}

	public void setCouponId(Long couponId) {
		this.couponId = couponId;
	}

	public String getCouponName() {
		return couponName;
	}

	public void setCouponName(String couponName) {
		this.couponName = couponName;
	}

	public Long getPercentageDiscount() {
		return percentageDiscount;
	}

	public void setPercentageDiscount(Long percentageDiscount) {
		this.percentageDiscount = percentageDiscount;
	}

	public Long getMaxDiscount() {
		return maxDiscount;
	}

	public void setMaxDiscount(Long maxDiscount) {
		this.maxDiscount = maxDiscount;
	}

	public LocalDate getCreated_on() {
		return created_on;
	}

	public void setCreated_on(LocalDate created_on) {
		this.created_on = created_on;
	}

	public Long getVendorId() {
		return vendorId;
	}

	public void setVendorId(Long vendorId) {
		this.vendorId = vendorId;
	}

}
