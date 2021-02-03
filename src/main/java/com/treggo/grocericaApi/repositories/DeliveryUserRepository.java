package com.treggo.grocericaApi.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.treggo.grocericaApi.entities.DeliveryUser;
import com.treggo.grocericaApi.enums.userType;

public interface DeliveryUserRepository extends JpaRepository<DeliveryUser, Long> {
	
	public DeliveryUser findByDeliveryUserId(Long userId);
	
	public DeliveryUser findByEmail(String email);
	public List<DeliveryUser> findByUserType(userType userType);
	public List<DeliveryUser> findByIsActive(boolean isActive);
	
	@Query("from DeliveryUser where user_id = :userId")
	public List<DeliveryUser> getUsersByVendor(@Param("userId") Long userId);
}
