package com.treggo.grocericaApi.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.treggo.grocericaApi.entities.Coupon;
import com.treggo.grocericaApi.entities.Users;
import com.treggo.grocericaApi.requests.ApplyCouponDTO;
import com.treggo.grocericaApi.responses.CouponResponse;
import com.treggo.grocericaApi.responses.GeneralResponse;
import com.treggo.grocericaApi.services.CouponService;
import com.treggo.grocericaApi.services.TokenGenerator;

import io.swagger.annotations.ApiOperation;

@RestController
@CrossOrigin
@RequestMapping("/apply")
public class CouponController {

	@Autowired
	private CouponService couponService;

	@Autowired
	private TokenGenerator tokenService;

	@ApiOperation(value = "Creates a new Coupon")
	@PostMapping("/create")
	public ResponseEntity<?> createCoupon(@RequestBody Coupon req, @RequestHeader("token") String token) {
		// Validate token
		Users user = tokenService.validateToken(token);
		if (user == null) {
			return ResponseEntity.status(401).body(new GeneralResponse("Unauthorized"));
		}

		Coupon res = couponService.createCoupon(req);
		if (res == null) {
			return ResponseEntity.status(500).body(new GeneralResponse("failure"));
		} else {
			return ResponseEntity.ok(res);
		}
	}

	@ApiOperation(value = "Get all the Coupons")
	@GetMapping("/getAll")
	public ResponseEntity<?> getAll(@RequestHeader("token") String token) {
		// Validate token
		Users user = tokenService.validateToken(token);
		if (user == null) {
			return ResponseEntity.status(401).body(new GeneralResponse("Unauthorized"));
		}

		return ResponseEntity.ok(couponService.getAllCoupons(user));
	}

	@ApiOperation(value = "Delete particular Coupon")
	@DeleteMapping("/remove/{couponId}")
	public ResponseEntity<?> removeCoupon(@PathVariable("couponId") Long couponId,
			@RequestHeader("token") String token) {
		// Validate token
		Users user = tokenService.validateToken(token);
		if (user == null) {
			return ResponseEntity.status(401).body(new GeneralResponse("Unauthorized"));
		}

		boolean result = couponService.deleteCoupon(couponId);
		if (result) {
			return ResponseEntity.ok(new GeneralResponse("Success"));
		} else {
			return ResponseEntity.status(500).body(new GeneralResponse("Failure"));
		}

	}

	@ApiOperation(value = "Apply discount on amount")
	@PostMapping("/apply")
	public ResponseEntity<?> applyCoupon(@RequestBody ApplyCouponDTO req, @RequestHeader("token") String token) {
		// Validate token
		Users user = tokenService.validateToken(token);
		if (user == null) {
			return ResponseEntity.status(401).body(new GeneralResponse("Unauthorized"));
		}

		CouponResponse res = couponService.applyCoupon(req);
		if (res == null) {
			return ResponseEntity.status(500).body(new GeneralResponse("Failure"));
		} else {
			return ResponseEntity.ok(res);
		}
	}
}
