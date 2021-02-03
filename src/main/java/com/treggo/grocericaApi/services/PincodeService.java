package com.treggo.grocericaApi.services;

import java.time.LocalDate;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.treggo.grocericaApi.entities.Pincode;
import com.treggo.grocericaApi.repositories.PinCodeRepository;

@Service
public class PincodeService {

	@Autowired
	private PinCodeRepository repo;
	
	Logger logger = LoggerFactory.getLogger(PincodeService.class);
	
	public Pincode createPincode(String name) {
		Pincode pin = new Pincode();
		if(name == null) {
			return null;
		}
		if(repo.fetchByName(name) != null) {
			return null;
		}
		pin.setName(name);
		pin.setActive(true);
		pin.setCreated_on(LocalDate.now());
		
		try {
			repo.save(pin);
			return pin;
		} catch(Exception e) {
			logger.error("Failed to run: " + e);
			return null;
		}
	}
	
	public boolean deletePincode(Long id) {
		try {
			repo.deleteById(id);
			return true;
		} catch(Exception e) {
			return false;
		}
	}
	
	public Pincode activatePincode(Long id) {
		try {
			Pincode pin = repo.fetchById(id);
			pin.setActive(true);
			repo.save(pin);
			return pin;
		} catch(Exception e) {
			return null;
		}
	}
	
	public Pincode deactivatePincode(Long id) {
		try {
			Pincode pin = repo.fetchById(id);
			pin.setActive(false);
			repo.save(pin);
			return pin;
		} catch(Exception e) {
			return null;
		}
	}
	
	public List<Pincode> getAllPincodes() {
		try {
			return repo.findAll();
		} catch(Exception e) {
			return null;
		}
		
	}
	
	public boolean validatePincode(String pincode) {
		try {
			Pincode pin = repo.fetchByName(pincode);
			if(pin == null) {
				return false;
			} else {
				return true;
			}
		}
		catch(Exception e) {
			logger.error("Failed to run: " + e);
			return false;
		}
	}
}
