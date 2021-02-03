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
import com.treggo.grocericaApi.requests.NewBannerDTO;
import com.treggo.grocericaApi.responses.BannerResponse;
import com.treggo.grocericaApi.responses.GeneralResponse;
import com.treggo.grocericaApi.services.BannerService;
import com.treggo.grocericaApi.services.TokenGenerator;

import io.swagger.annotations.ApiOperation;

@RestController
@CrossOrigin
@RequestMapping("/banners")
public class BannerController {

	@Autowired
	private BannerService bannerService;

	@Autowired
	private TokenGenerator tokenService;

	@ApiOperation(value = "Creates a new advertisement banner")
	@PostMapping("/create")
	public ResponseEntity<?> createBanner(@RequestBody NewBannerDTO req, @RequestHeader("token") String token) {

		// Validate token
		Users user = tokenService.validateToken(token);
		if (user == null) {
			return ResponseEntity.status(401).body(new GeneralResponse("Unauthorized"));
		}

		BannerResponse res = bannerService.createBanner(req);
		if (res == null) {
			return ResponseEntity.status(500).body(new GeneralResponse("failure"));
		} else {
			return ResponseEntity.ok(res);
		}
	}

	@ApiOperation(value = "Get all the advertisement banners")
	@GetMapping("/getAll")
	public ResponseEntity<?> getAllBanners() {
		return ResponseEntity.ok(bannerService.findAllBanners());
	}

	@ApiOperation(value = "Delete advertisement banners")
	@DeleteMapping("/remove/{bannerId}")
	public ResponseEntity<?> removeBanner(@PathVariable("bannerId") Long bannerId,
			@RequestHeader("token") String token) {
		// Validate token
		Users user = tokenService.validateToken(token);
		if (user == null) {
			return ResponseEntity.status(401).body(new GeneralResponse("Unauthorized"));
		}
		boolean result = bannerService.deleteBanner(bannerId);
		if (result) {
			return ResponseEntity.ok(new GeneralResponse("Success"));
		} else {
			return ResponseEntity.status(500).body(new GeneralResponse("Failure"));
		}
	}

	@ApiOperation(value = "Get single banner")
	@GetMapping("/get/{bannerId}")
	public ResponseEntity<?> getBanner(@PathVariable("bannerId") Long bannerId, @RequestHeader("token") String token) {
		// Validate token
		Users user = tokenService.validateToken(token);
		if (user == null) {
			return ResponseEntity.status(401).body(new GeneralResponse("Unauthorized"));
		}

		return ResponseEntity.ok(bannerService.findByBannerId(bannerId));
	}

}
