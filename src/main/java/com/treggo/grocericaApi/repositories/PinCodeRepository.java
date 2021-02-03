package com.treggo.grocericaApi.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.treggo.grocericaApi.entities.Pincode;

public interface PinCodeRepository extends JpaRepository<Pincode, Long> {

	@Query("from Pincode where pincodeId = :pincodeId")
	public Pincode fetchById(@Param("pincodeId") Long id);
	
	@Query("from Pincode where name = :name")
	public Pincode fetchByName(@Param("name") String name);
}
