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

import com.treggo.grocericaApi.entities.Users;
import com.treggo.grocericaApi.requests.NewProductDTO;
import com.treggo.grocericaApi.responses.GeneralResponse;
import com.treggo.grocericaApi.responses.ProductResponse;
import com.treggo.grocericaApi.services.ProductService;
import com.treggo.grocericaApi.services.TokenGenerator;

import io.swagger.annotations.ApiOperation;

@RestController
@CrossOrigin
@RequestMapping("/product")
public class ProductController {

	@Autowired
	private ProductService productService;

	@Autowired
	private TokenGenerator tokenService;

	@ApiOperation(value = "Creates a new Product")
	@PostMapping("/create")
	public ResponseEntity<?> createProduct(@RequestBody NewProductDTO req, @RequestHeader("token") String token) {
		// Validate token
		Users user = tokenService.validateToken(token);
		if (user == null) {
			return ResponseEntity.status(401).body(new GeneralResponse("Unauthorized"));
		}

		ProductResponse res = productService.createProduct(req);
		if (res == null) {
			return ResponseEntity.status(500).body(new GeneralResponse("failure"));
		} else {
			return ResponseEntity.ok(res);
		}
	}

	@ApiOperation(value = "Get all the Products")
	@GetMapping("/getAll")
	public ResponseEntity<?> getAllProducts() {

		return ResponseEntity.ok(productService.getAllProducts());
	}

	@ApiOperation(value = "Delete particular Product")
	@DeleteMapping("/remove/{productId}")
	public ResponseEntity<?> removeProduct(@PathVariable("productId") Long productId,
			@RequestHeader("token") String token) {
		// Validate token
		Users user = tokenService.validateToken(token);
		if (user == null) {
			return ResponseEntity.status(401).body(new GeneralResponse("Unauthorized"));
		}

		boolean result = productService.deleteProduct(productId);
		if (result) {
			return ResponseEntity.ok(new GeneralResponse("Success"));
		} else {
			return ResponseEntity.status(500).body(new GeneralResponse("Failure"));
		}
	}

	@ApiOperation(value = "Get single Product")
	@GetMapping("/get/{productId}")
	public ResponseEntity<?> getProduct(@PathVariable("productId") Long productId) {
	
		return ResponseEntity.ok(productService.getSingleProduct(productId));
	}

	@ApiOperation(value = "Get Products by Category ID")
	@GetMapping("/getByCategory/{categoryId}")
	public ResponseEntity<?> getProductbyCategory(@PathVariable("categoryId") Long categoryId) {
		return ResponseEntity.ok(productService.getProductsByCategory(categoryId));
	}

	@ApiOperation(value = "Search Products by Name")
	@GetMapping("/search/{productName}")
	public ResponseEntity<?> searchProduct(@PathVariable("productName") String productName) {
		return ResponseEntity.ok(productService.seachProducts(productName));
	}

	@ApiOperation(value = "Make product available")
	@GetMapping("/available/{productId}")
	public ResponseEntity<?> availableProduct(@PathVariable("productId") Long productId,
			@RequestHeader("token") String token) {
		// Validate token
		Users user = tokenService.validateToken(token);
		if (user == null) {
			return ResponseEntity.status(401).body(new GeneralResponse("Unauthorized"));
		}

		boolean res = productService.setAvailability(productId, true);
		if (res) {
			return ResponseEntity.ok(new GeneralResponse("success"));
		} else {
			return ResponseEntity.status(500).body(new GeneralResponse("failure"));
		}
	}
	
	@ApiOperation(value = "Make product unavailable")
	@GetMapping("/unavailable/{productId}")
	public ResponseEntity<?> unavailableProduct(@PathVariable("productId") Long productId,
			@RequestHeader("token") String token) {
		// Validate token
		Users user = tokenService.validateToken(token);
		if (user == null) {
			return ResponseEntity.status(401).body(new GeneralResponse("Unauthorized"));
		}

		boolean res = productService.setAvailability(productId, false);
		if (res) {
			return ResponseEntity.ok(new GeneralResponse("success"));
		} else {
			return ResponseEntity.status(500).body(new GeneralResponse("failure"));
		}
	}

}
