package com.treggo.grocericaApi.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.treggo.grocericaApi.entities.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

	public Product findByProductId(Long productId);
	
	@Query("from Product where category_Id = :categoryId")
	public List<Product> fetchByCategoryId(@Param("categoryId") Long categoryId);
	
	@Query("from Product where lower(product_Name) like lower(concat('%', :productName, '%')) OR lower(brand) like lower(concat('%', :productName, '%')) OR lower(product_Description) like lower(concat('%', :productName, '%'))")
	public List<Product> searchProducts(@Param("productName") String productName);
	
	@Query("from Product where vendor_id = :vendorId") 
	public List<Product> fetchByVendorId(@Param("vendorId") Long vendorId);
	
}
