package com.treggo.grocericaApi.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.treggo.grocericaApi.entities.Coupon;

public interface CouponRepository extends JpaRepository<Coupon, Long> {

	public Coupon findByCouponId(Long couponId);
	
	public Coupon findByCouponName(String couponName);
	
	public List<Coupon> findByVendorId(Long vendorId);
}
