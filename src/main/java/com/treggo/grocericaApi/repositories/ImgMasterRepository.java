package com.treggo.grocericaApi.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.treggo.grocericaApi.entities.ImgMaster;

public interface ImgMasterRepository extends JpaRepository<ImgMaster, Long> {

	public ImgMaster findByImgId(Long imgId); 
}
