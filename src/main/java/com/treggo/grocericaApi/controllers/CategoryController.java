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
import com.treggo.grocericaApi.requests.NewCategoryDTO;
import com.treggo.grocericaApi.responses.CategoryResponse;
import com.treggo.grocericaApi.responses.GeneralResponse;
import com.treggo.grocericaApi.services.CategoryService;
import com.treggo.grocericaApi.services.TokenGenerator;

import io.swagger.annotations.ApiOperation;

@RestController
@CrossOrigin
@RequestMapping("/category")
public class CategoryController {

	@Autowired
	private CategoryService categoryService;

	@Autowired
	private TokenGenerator tokenService;

	@ApiOperation(value = "Creates a new Category")
	@PostMapping("/create")
	public ResponseEntity<?> createCategory(@RequestBody NewCategoryDTO req, @RequestHeader("token") String token) {
		// Validate token
		Users user = tokenService.validateToken(token);
		if (user == null) {
			return ResponseEntity.status(401).body(new GeneralResponse("Unauthorized"));
		}

		CategoryResponse res = categoryService.createCategory(req);
		if (res == null) {
			return ResponseEntity.status(500).body(new GeneralResponse("failure"));
		} else {
			return ResponseEntity.ok(res);
		}
	}

	@ApiOperation(value = "Get all the Categories")
	@GetMapping("/getAll")
	public ResponseEntity<?> getAllCategories() {
		return ResponseEntity.ok(categoryService.findAllCategories());
	}

	@ApiOperation(value = "Delete particular Category")
	@DeleteMapping("/remove/{categoryId}")
	public ResponseEntity<?> removeCategory(@PathVariable("categoryId") Long categoryId,
			@RequestHeader("token") String token) {
		// Validate token
		Users user = tokenService.validateToken(token);
		if (user == null) {
			return ResponseEntity.status(401).body(new GeneralResponse("Unauthorized"));
		}

		boolean result = categoryService.deleteCategory(categoryId);
		if (result) {
			return ResponseEntity.ok(new GeneralResponse("Success"));
		} else {
			return ResponseEntity.status(500).body(new GeneralResponse("Failure"));
		}
	}

	@ApiOperation(value = "Get single Category")
	@GetMapping("/get/{categoryId}")
	public ResponseEntity<?> getCategory(@PathVariable("categoryId") Long categoryId) {
		return ResponseEntity.ok(categoryService.getSingleCategory(categoryId));
	}
}
