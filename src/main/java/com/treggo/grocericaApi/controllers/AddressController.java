package com.treggo.grocericaApi.controllers;

import java.util.List;

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

import com.treggo.grocericaApi.entities.Address;
import com.treggo.grocericaApi.entities.Pincode;
import com.treggo.grocericaApi.entities.Users;
import com.treggo.grocericaApi.requests.NewAddressDTO;
import com.treggo.grocericaApi.requests.UpdateAddressDTO;
import com.treggo.grocericaApi.responses.GeneralResponse;
import com.treggo.grocericaApi.services.AddressService;
import com.treggo.grocericaApi.services.PincodeService;
import com.treggo.grocericaApi.services.TokenGenerator;

import io.swagger.annotations.ApiOperation;

@RestController
@CrossOrigin
@RequestMapping("/address")
public class AddressController {

	@Autowired
	private AddressService addressService;

	@Autowired
	TokenGenerator tokenService;

	@Autowired
	private PincodeService pincodeService;

	Logger logger = LoggerFactory.getLogger(AddressController.class);
	
	@ApiOperation(value = "Creates a new address for the user")
	@PostMapping("/create")
	public ResponseEntity<?> createAddress(@RequestBody NewAddressDTO req, @RequestHeader("token") String token) {
		// Validate token
		Users user = tokenService.validateToken(token);
		if (user == null) {
			return ResponseEntity.status(401).body(new GeneralResponse("Unauthorized"));
		}

		Address res = addressService.createNewAddress(req, user);
		if (res == null) {
			return ResponseEntity.status(500).body(new GeneralResponse("Failed"));
		} else {
			return ResponseEntity.ok(res);
		}
	}
	
	@ApiOperation(value = "Update existing address for the user")
	@PostMapping("/update")
	public ResponseEntity<?> updateAddress(@RequestBody UpdateAddressDTO req, @RequestHeader("token") String token) {
		// Validate token
		Users user = tokenService.validateToken(token);
		if (user == null) {
			return ResponseEntity.status(401).body(new GeneralResponse("Unauthorized"));
		}

		Address res = addressService.updateAddress(req, user);
		if (res == null) {
			return ResponseEntity.status(500).body(new GeneralResponse("Failure"));
		} else {
			return ResponseEntity.ok(res);
		}
	}

	@ApiOperation(value = "Delete existing address for the user")
	@DeleteMapping("/delete/{addressId}")
	public ResponseEntity<?> deleteAddress(@PathVariable("addressId") Long addressId,
			@RequestHeader("token") String token) {
		// Validate token
		Users user = tokenService.validateToken(token);
		if (user == null) {
			return ResponseEntity.status(401).body(new GeneralResponse("Unauthorized"));
		}

		boolean result = addressService.deleteAddress(addressId);
		if (result) {
			return ResponseEntity.ok(new GeneralResponse("Address deleted successfully"));
		} else {
			return ResponseEntity.status(500).body(new GeneralResponse("Failed"));
		}
	}

	@ApiOperation(value = "Get single address for the user")
	@GetMapping("/get/{addressId}")
	public ResponseEntity<?> getOneAddress(@PathVariable("addressId") Long addressId,
			@RequestHeader("token") String token) {

		// Validate token
		Users user = tokenService.validateToken(token);
		if (user == null) {
			return ResponseEntity.status(401).body(new GeneralResponse("Unauthorized"));
		}

		Address address = addressService.getSingleAddress(addressId);
		if (address == null) {
			return ResponseEntity.ok(new GeneralResponse("Address not found"));
		} else {
			return ResponseEntity.ok(address);
		}
	}

	@ApiOperation(value = "Get all addresses for the user")
	@GetMapping("/get")
	public ResponseEntity<?> getAllAddress(@RequestHeader("token") String token) {

		// Validate token
		Users user = tokenService.validateToken(token);
		if (user == null) {
			logger.error("Invalid session token: " + token);
			return ResponseEntity.status(401).body(new GeneralResponse("Unauthorized"));
		}

		List<Address> addresses = addressService.getAllUserAddress(user);
		return ResponseEntity.ok(addresses);
	}

	@ApiOperation(value = "Create new pincode to which delivery services are offered")
	@GetMapping("/pincode/create/{name}")
	public ResponseEntity<?> createPincode(@PathVariable("name") String name, @RequestHeader("token") String token) {

		// Validate token
		Users user = tokenService.validateToken(token);
		if (user == null) {
			return ResponseEntity.status(401).body(new GeneralResponse("Unauthorized"));
		}

		Pincode pin = pincodeService.createPincode(name);
		if (pin == null) {
			return ResponseEntity.status(500).body(new GeneralResponse("Failed"));
		} else {
			return ResponseEntity.ok(pin);
		}
	}

	@ApiOperation(value = "Get all active pincodes for admin use only")
	@GetMapping("/pincode/all")
	public ResponseEntity<?> getAll(@RequestHeader("token") String token) {

		// Validate token
		Users user = tokenService.validateToken(token);
		if (user == null) {
			return ResponseEntity.status(401).body(new GeneralResponse("Unauthorized"));
		}

		List<Pincode> pincodes = pincodeService.getAllPincodes();
		if (pincodes == null) {
			return ResponseEntity.status(500).body(new GeneralResponse("Failed"));
		} else {
			return ResponseEntity.ok(pincodes);
		}
	}

	@ApiOperation(value = "Activate pincode for admin use only")
	@GetMapping("/pincode/activate/{id}")
	public ResponseEntity<?> activatePin(@PathVariable("id") Long id, @RequestHeader("token") String token) {
		// Validate token
		Users user = tokenService.validateToken(token);
		if (user == null) {
			return ResponseEntity.status(401).body(new GeneralResponse("Unauthorized"));
		}

		Pincode pin = pincodeService.activatePincode(id);
		if (pin == null) {
			return ResponseEntity.status(500).body(new GeneralResponse("Failed"));
		} else {
			return ResponseEntity.ok(pin);
		}
	}
	
	@ApiOperation(value = "Validate pincode if it is being delivered or not")
	@GetMapping("/pincode/validate/{pincode}")
	public ResponseEntity<?> activatePin(@PathVariable("pincode") String pincode, @RequestHeader("token") String token) {
		// Validate token
		Users user = tokenService.validateToken(token);
		if (user == null) {
			return ResponseEntity.status(401).body(new GeneralResponse("Unauthorized"));
		}

		boolean res = pincodeService.validatePincode(pincode);
		if (!res) {
			return ResponseEntity.status(500).body(new GeneralResponse("Invalid"));
		} else {
			return ResponseEntity.ok(new GeneralResponse("Success"));
		}
	}

	@ApiOperation(value = "De-activate pincode for admin use only")
	@GetMapping("/pincode/deactivate/{id}")
	public ResponseEntity<?> deactivatePin(@PathVariable("id") Long id, @RequestHeader("token") String token) {
		// Validate token
		Users user = tokenService.validateToken(token);
		if (user == null) {
			return ResponseEntity.status(401).body(new GeneralResponse("Unauthorized"));
		}
		
		Pincode pin = pincodeService.deactivatePincode(id);
		if(pin == null) {
			return ResponseEntity.status(500).body(new GeneralResponse("Failed"));
		}
		else {
			return ResponseEntity.ok(pin);
		}
	}
	
	@ApiOperation(value = "Delete pincode for admin use only")
	@DeleteMapping("/pincode/delete/{id}")
	public ResponseEntity<?> deletePincodeAdmin(@PathVariable("id") Long id, @RequestHeader("token") String token) {
		// Validate token
		Users user = tokenService.validateToken(token);
		if (user == null) {
			return ResponseEntity.status(401).body(new GeneralResponse("Unauthorized"));
		}
		
		boolean result = pincodeService.deletePincode(id);
		if(result) {
			return ResponseEntity.ok(new GeneralResponse("Success"));
		}
		else {
			return ResponseEntity.status(500).body(new GeneralResponse("Failed"));
		}
	}
}
