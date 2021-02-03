package com.treggo.grocericaApi.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.treggo.grocericaApi.entities.Vendor;

public interface VendorRepository extends JpaRepository<Vendor, Long> {

	public Vendor findByVendorId(Long vendorId);
	
	@Query("from Vendor where user_id = :userId")
	public Vendor fetchByUserId(@Param("userId") Long userId);
}
