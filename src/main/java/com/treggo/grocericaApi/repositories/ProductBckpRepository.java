package com.treggo.grocericaApi.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.treggo.grocericaApi.entities.ProductBckp;

public interface ProductBckpRepository extends JpaRepository<ProductBckp, Long> {

	@Query("from ProductBckp where category_Id = :categoryId")
	public List<ProductBckp> fetchByCategoryId(@Param("categoryId") Long categoryId);

	public ProductBckp findByProductId(Long productId);
}
