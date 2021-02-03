package com.treggo.grocericaApi.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.treggo.grocericaApi.entities.BannerMaster;

public interface BannerRepository extends JpaRepository<BannerMaster, Long> {

	public BannerMaster findByBannerId(Long bannerId);
	
}
