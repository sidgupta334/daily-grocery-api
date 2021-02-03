package com.treggo.grocericaApi.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.treggo.grocericaApi.entities.AddressBckp;

public interface AdressBckpRepository extends JpaRepository<AddressBckp, Long> {

	@Query("from AddressBckp where addressId = :addressId")
	public AddressBckp fetchByAddressId(@Param("addressId") Long addressId);
	
	@Query("from AddressBckp where pincode = :pincode")
	public List<AddressBckp> fetchByPinCode(@Param("pincode") String pincode);
	
}
