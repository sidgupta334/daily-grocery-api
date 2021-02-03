package com.treggo.grocericaApi.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.treggo.grocericaApi.entities.Cart;

public interface CartRepository extends JpaRepository<Cart, Long> {
	
	public Cart findByCartId(Long cartId);
	
	@Query("from Cart where user_Id = :userId")
	public Cart fetchByUserId(@Param("userId") Long userId);
	
}
