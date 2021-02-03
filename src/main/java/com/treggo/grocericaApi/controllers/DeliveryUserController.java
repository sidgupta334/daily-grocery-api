package com.treggo.grocericaApi.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import com.treggo.grocericaApi.entities.DeliveryUser;
import com.treggo.grocericaApi.entities.Users;
import com.treggo.grocericaApi.enums.userType;
import com.treggo.grocericaApi.requests.DeliveryUserDTO;
import com.treggo.grocericaApi.requests.LoginDTO;
import com.treggo.grocericaApi.requests.OtpValidationDTO;
import com.treggo.grocericaApi.requests.UpdateUserDTO;
import com.treggo.grocericaApi.requests.updatePasswordDTO;
import com.treggo.grocericaApi.responses.GeneralResponse;
import com.treggo.grocericaApi.responses.LoginResponse;
import com.treggo.grocericaApi.responses.UserResponse;
import com.treggo.grocericaApi.services.DeliveryUserService;
import com.treggo.grocericaApi.services.TokenGenerator;

import io.swagger.annotations.ApiOperation;

@RestController
@CrossOrigin
@RequestMapping("/delivery/users")
public class DeliveryUserController {

	@Autowired
	private DeliveryUserService userService;

	@Autowired
	TokenGenerator tokenService;

	Logger logger = LoggerFactory.getLogger(DeliveryUserController.class);

	@ApiOperation(value = "Creates a new delivery partner of the system")
	@PostMapping("/create")
	public ResponseEntity<?> createUser(@RequestBody DeliveryUserDTO req) {
		UserResponse res = userService.createNewUser(req);
		if (res.isValid()) {
			return ResponseEntity.ok().body(res);
		} else {
			return ResponseEntity.status(500).body(res);
		}
	}

	@ApiOperation(value = "Get all the delivery partners of the system")
	@GetMapping("/all")
	public ResponseEntity<?> getAllUsers(@RequestHeader("token") String token) {
		// Validate token
		Users user = tokenService.validateToken(token);
		if (user != null && user.getUserType().equals(userType.ADMIN)) {
			return ResponseEntity.ok(userService.getAllUsers());
		} else {
			return ResponseEntity.status(401).body(new GeneralResponse("Unauthorized"));
		}
	}
	
	@ApiOperation(value = "Get all the delivery partners of a vendor")
	@GetMapping("/vendor")
	public ResponseEntity<?> getAllUsersByVendor(@RequestHeader("token") String token) {
		// Validate token
		Users user = tokenService.validateToken(token);
		if (user != null && user.getUserType().equals(userType.VENDOR)) {
			return ResponseEntity.ok(userService.getAllUsersByVendor(user.getUserId()));
		} else {
			return ResponseEntity.status(401).body(new GeneralResponse("Unauthorized"));
		}
	}

	@ApiOperation(value = "Login as an existing user")
	@PostMapping("/login")
	public ResponseEntity<?> loginUser(@RequestBody LoginDTO req) {

		LoginResponse login = userService.loginUser(req);
		if (login == null) {
			logger.warn("Unauthorized access request for user: " + req.getEmail());
			return ResponseEntity.status(400).body(new GeneralResponse("Login failed"));
		} else if (login.getMessage().equals("PENDING")) {
			logger.warn("User is not verified: " + req.getEmail());
			return ResponseEntity.status(500).body(login);
		} else {
			logger.info("User Logged in successfully: " + login.getUserId() + " : " + login.getFullName());
			return ResponseEntity.ok(login);
		}
	}

	@ApiOperation(value = "Update existing user")
	@PostMapping("/update")
	public ResponseEntity<?> updateExistingUser(@RequestHeader("token") String token, @RequestBody UpdateUserDTO dto) {
		// Validate token
		DeliveryUser user = tokenService.validateDeliveryToken(token);
		if (user == null) {
			return ResponseEntity.status(401).body(new GeneralResponse("Unauthorized"));
		}

		DeliveryUser res = userService.updateUser(dto);
		if (res == null) {
			return ResponseEntity.status(401).body(new GeneralResponse("Failed to update user"));
		} else {
			res.setPassword(null);
			return ResponseEntity.ok(res);
		}
	}

	@ApiOperation(value = "Update User password")
	@PostMapping("/updatePassword")
	public ResponseEntity<?> updatePassword(@RequestBody updatePasswordDTO req) {

		String result = userService.updatePassword(req);
		if (result == "INVALID") {
			return ResponseEntity.status(400).body(new GeneralResponse("Invalid Request"));
		} else if (result == "FAILURE") {
			return ResponseEntity.status(500).body(new GeneralResponse("Failure"));
		} else {
			return ResponseEntity.ok(new GeneralResponse("Password updated successfully"));
		}
	}

	@ApiOperation(value = "Logout user session")
	@GetMapping("/logout")
	public ResponseEntity<?> logoutUser(@RequestHeader("token") String token) {
		// Validate token
		DeliveryUser user = tokenService.validateDeliveryToken(token);
		if (user == null) {
			return ResponseEntity.status(401).body(new GeneralResponse("Unauthorized"));
		}

		if (userService.logoutUser(user)) {
			return ResponseEntity.ok(new GeneralResponse("User logged out"));
		} else {
			return ResponseEntity.status(500).body(new GeneralResponse("Unable to logout user"));
		}
	}
	
	@ApiOperation(value = "Get user details")
	@GetMapping("/me")
	public ResponseEntity<?> getMyDetails(@RequestHeader("token") String token) {
		// Validate token
		DeliveryUser user = tokenService.validateDeliveryToken(token);
		if (user == null) {
			return ResponseEntity.status(401).body(new GeneralResponse("Unauthorized"));
		}

		user.setPassword(null);
		return ResponseEntity.ok(user);
	}

	@ApiOperation(value = "Recover account by email ID")
	@GetMapping("/forgotPassword/{email}")
	public ResponseEntity<?> forgotPassword(@PathVariable("email") String email) {
		String result = userService.forgotPassword(email);
		if (result == "ERROR") {
			return ResponseEntity.status(500).body(new GeneralResponse("Something went wrong"));
		} else if (result == "NOT FOUND") {
			return ResponseEntity.status(400).body(new GeneralResponse("User not found"));
		} else if (result == "FAILURE") {
			return ResponseEntity.status(500).body(new GeneralResponse("Unable to send mail"));
		} else {
			return ResponseEntity.ok(new GeneralResponse("OTP sent successfully"));
		}
	}

	@ApiOperation(value = "Validate OTP for account recovery")
	@PostMapping("/validateOTP")
	public ResponseEntity<?> validateOTP(@RequestBody OtpValidationDTO req) {

		GeneralResponse res = userService.validateOTP(req);
		if (res.getMessage().equals("SUCCESS")) {
			return ResponseEntity.ok(new GeneralResponse("Password updated successfully"));
		} else {
			return ResponseEntity.status(500).body(res);
		}
	}

	@ApiOperation(value = "Delete existing user")
	@DeleteMapping("/delete/{userId}")
	public ResponseEntity<?> deleteUser(@PathVariable("userId") Long userId, @RequestHeader("token") String token) {

		// Validate token
		Users user = tokenService.validateToken(token);
		if (user == null) {
			return ResponseEntity.status(401).body(new GeneralResponse("Unauthorized"));
		}

		boolean res = userService.deleteUser(userId);
		if (!res) {
			return ResponseEntity.status(500).body(new GeneralResponse("Failure"));
		} else {
			return ResponseEntity.ok(new GeneralResponse("User deleted successfully"));
		}
	}
}
