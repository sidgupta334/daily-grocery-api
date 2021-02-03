package com.treggo.grocericaApi.services;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.treggo.grocericaApi.entities.Vendor;
import com.treggo.grocericaApi.repositories.VendorRepository;

@Service
public class VendorService {

	@Autowired
	private VendorRepository repo;
	
	Logger logger = LoggerFactory.getLogger(VendorService.class);
	
	public Vendor saveVendor(Vendor req) {
		if(this._validateRequest(req)) {
			try {
				repo.save(req);
				return req;
			} catch(Exception e) {
				logger.error("Unable to save vendor: " + e.getMessage());
				e.printStackTrace();
				return null;
			}
		} else {
			return null;
		}
	}
	
	public List<Vendor> getAllVendors() {
		return repo.findAll();
	}
	
	public Vendor findById(Long vendorId) {
		return repo.findByVendorId(vendorId);
	}
	
	public Vendor getVendorByUser(Long userId) {
		try {
			return repo.fetchByUserId(userId);
		} catch(Exception e) {
			logger.error("Unable to run search for vendor");
			e.printStackTrace();
			return null;
		}
	}
	
	public boolean removeVendor(Long vendorId) {
		try {
			repo.deleteById(vendorId);
			return true;
		} catch(Exception e) {
			logger.error("Unable to delete vendor with id: " + vendorId);
			e.printStackTrace();
			return false;
		}
	}
	
	private boolean _validateRequest(Vendor req) {
		if(req.getUser() == null || req.getVendorName() == null) {
			return false;
		} else {
			return true;
		}
	}
}
