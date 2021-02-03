package com.treggo.grocericaApi.services;

import java.time.LocalDate;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.treggo.grocericaApi.entities.Address;
import com.treggo.grocericaApi.entities.AddressBckp;
import com.treggo.grocericaApi.entities.Users;
import com.treggo.grocericaApi.repositories.AddressRepository;
import com.treggo.grocericaApi.repositories.AdressBckpRepository;
import com.treggo.grocericaApi.requests.NewAddressDTO;
import com.treggo.grocericaApi.requests.UpdateAddressDTO;

@Service
public class AddressService {

	@Autowired
	private AddressRepository repo;

	@Autowired
	private AdressBckpRepository aRepo;
	
	Logger logger = LoggerFactory.getLogger(AddressService.class);

	public Address createNewAddress(NewAddressDTO ad, Users user) {
		if (ad.getAddress1() == null || ad.getPincode() == null || ad.getState() == null || ad.getMobile() == null
				|| ad.getLattitude() == null || ad.getLongitude() == null) {
			return null;
		}

		try {
			Address address = new Address();
			AddressBckp bck = new AddressBckp();
			BeanUtils.copyProperties(ad, address);
			address.setLattitude(ad.getLattitude());
			address.setLongitude(ad.getLongitude());
			bck.setLattitude(ad.getLattitude());
			bck.setLongitude(ad.getLongitude());
			address.setUser(user);
			address.setCreated_on(LocalDate.now());
			BeanUtils.copyProperties(address, bck);
			repo.save(address);
			bck.setAddressId(address.getAddressId());
			aRepo.save(bck);

			logger.info("SAVING NEW ADDRESS: " + address.getAddressId().toString());
			return address;
		} catch (Exception e) {
			logger.error("Failed to run: " + e);
			return null;
		}
	}

	public Address updateAddress(UpdateAddressDTO ad, Users user) {
		if (ad.getAddressId() == null || ad.getAddress1() == null || ad.getPincode() == null || ad.getState() == null || ad.getMobile() == null  || ad.getLattitude() == null || ad.getLongitude() == null) {
			return null;
		}
		try {
			Address address = repo.fetchByAddressId(ad.getAddressId());
			if(address == null) {
				return null;
			}
			AddressBckp ab = aRepo.fetchByAddressId(ad.getAddressId());
			BeanUtils.copyProperties(ad, address);
			BeanUtils.copyProperties(ad, ab);
			repo.save(address);
			aRepo.save(ab);
			return address;
		}
		catch(Exception e) {
			logger.error("Failed to run: " + e);
			return null;
		}
		
		
	}

	public boolean deleteAddress(Long addressId) {
		boolean result = false;
		try {
			repo.deleteById(addressId);
			result = true;
		} catch (Exception e) {
			logger.error("Failed to run: " + e);
			return false;
		}
		return result;
	}

	public List<Address> getAllUserAddress(Users user) {
		return repo.fetchByUserId(user.getUserId());
	}

	public Address getSingleAddress(Long addressId) {
		return repo.fetchByAddressId(addressId);
	}

}
