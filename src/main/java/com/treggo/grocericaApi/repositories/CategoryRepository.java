package com.treggo.grocericaApi.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.treggo.grocericaApi.entities.Category;
import com.treggo.grocericaApi.entities.Product;

public interface CategoryRepository extends JpaRepository<Category, Long> {

	public Category findByCategoryId(Long categoryId);
	
	@Query("from Category where lower(category_Name) like lower(concat('%', :categoryName, '%'))")
	public List<Product> searchCategory(@Param("categoryName") String categoryName);
}
