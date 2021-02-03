package com.treggo.grocericaApi.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.treggo.grocericaApi.entities.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {

	public Category findByCategoryId(Long categoryId);
}
