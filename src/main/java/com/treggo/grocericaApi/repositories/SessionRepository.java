package com.treggo.grocericaApi.repositories;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.treggo.grocericaApi.entities.Session;

public interface SessionRepository extends JpaRepository<Session, Long> {

	public Session findByToken(String token);

	public Session findByUserIdAndUserType(Long userId, String userType);

	@Transactional
	@Modifying
	@Query("delete from Session s where s.userId = :userId and s.userType = :userType")
	public int deleteSession(@Param("userId") Long userId, @Param("userType") String userType);
	
}
