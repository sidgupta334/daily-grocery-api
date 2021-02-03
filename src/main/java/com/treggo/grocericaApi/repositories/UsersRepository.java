package com.treggo.grocericaApi.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.treggo.grocericaApi.entities.Users;
import com.treggo.grocericaApi.enums.userType;

public interface UsersRepository extends JpaRepository<Users, Long> {
	
	@Query("from Users where userId = :userId")
	public Users fetchByUserId(@Param("userId") Long id);
	
	public Users findByEmail(String email);
	public List<Users> findByUserType(userType userType);
	public List<Users> findByIsActive(boolean isActive);
	
	@Query("from Users where user_type = :userType1 OR user_type = :userType2")
	public List<Users> fetchByAdmin(@Param("userType1") String userType1, @Param("userType2") String userType2);
}
