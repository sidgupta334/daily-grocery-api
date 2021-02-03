package com.treggo.grocericaApi.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.treggo.grocericaApi.entities.Users;
import com.treggo.grocericaApi.responses.CartResponse;
import com.treggo.grocericaApi.responses.GeneralResponse;
import com.treggo.grocericaApi.services.CartService;
import com.treggo.grocericaApi.services.TokenGenerator;

import io.swagger.annotations.ApiOperation;

@RestController
@CrossOrigin
@RequestMapping("/cart")
public class CartController {

	@Autowired
	private CartService service;

	@Autowired
	private TokenGenerator tokenService;

	@ApiOperation(value = "Add a product to cart OR creates a new cart (if doesn't exists)")
	@GetMapping("/add/{productId}")
	public ResponseEntity<?> addToCart(@PathVariable("productId") Long productId,
			@RequestHeader("token") String token) {
		// Validate token
		Users user = tokenService.validateToken(token);
		if (user == null) {
			return ResponseEntity.status(401).body(new GeneralResponse("Unauthorized"));
		}

		CartResponse res = service.addToCart(productId, user);
		if (res == null) {
			return ResponseEntity.status(500).body(new GeneralResponse("failure"));
		} else {
			return ResponseEntity.ok(res);
		}
	}

	@ApiOperation(value = "Remove item from cart")
	@GetMapping("/remove/{productId}")
	public ResponseEntity<?> removeFromCart(@PathVariable("productId") Long productId,
			@RequestHeader("token") String token) {
		// Validate token
		Users user = tokenService.validateToken(token);
		if (user == null) {
			return ResponseEntity.status(401).body(new GeneralResponse("Unauthorized"));
		}

		CartResponse res = service.removeFromCart(productId, user);
		if (res == null) {
			return ResponseEntity.status(500).body(new GeneralResponse("failure"));
		} else {
			return ResponseEntity.ok(res);
		}
	}

	@ApiOperation(value = "Empty the cart of user")
	@GetMapping("/empty")
	public ResponseEntity<?> emptyCart(@RequestHeader("token") String token) {
		// Validate token
		Users user = tokenService.validateToken(token);
		if (user == null) {
			return ResponseEntity.status(401).body(new GeneralResponse("Unauthorized"));
		}

		CartResponse res = service.emptyCart(user);
		if (res == null) {
			return ResponseEntity.status(500).body(new GeneralResponse("failure"));
		} else {
			return ResponseEntity.ok(res);
		}
	}

	@ApiOperation(value = "View cart of user")
	@GetMapping("/view")
	public ResponseEntity<?> viewCart(@RequestHeader("token") String token) {
		// Validate token
		Users user = tokenService.validateToken(token);
		if (user == null) {
			CartResponse res = new CartResponse();
			res.setNetQuantity(new Long(0));
			res.setNetTotal(new Long(0));
			return ResponseEntity.ok(res);
		}

		CartResponse res = service.viewCart(user);
		if (res == null) {
			return ResponseEntity.status(500).body(new GeneralResponse("failure"));
		} else {
			return ResponseEntity.ok(res);
		}
	}

}
