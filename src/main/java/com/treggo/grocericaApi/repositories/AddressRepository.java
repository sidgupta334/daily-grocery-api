package com.treggo.grocericaApi.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.treggo.grocericaApi.entities.Address;

public interface AddressRepository extends JpaRepository<Address, Long> {

	@Query("from Address where addressId = :addressId")
	public Address fetchByAddressId(@Param("addressId") Long addressId);

	@Query("from Address where user_Id = :userId")
	public List<Address> fetchByUserId(@Param("userId") Long userId);
}
