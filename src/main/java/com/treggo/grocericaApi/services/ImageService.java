package com.treggo.grocericaApi.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import com.treggo.grocericaApi.entities.ImgMaster;
import com.treggo.grocericaApi.repositories.ImgMasterRepository;

@Component
@CacheConfig(cacheNames={"images"})
public class ImageService {

	@Autowired
	private ImgMasterRepository imgRepo;
	
	Logger logger = LoggerFactory.getLogger(ImageService.class);
	
	@Cacheable(value="images")
	public ImgMaster getImage(Long imgId) {
		return imgRepo.findByImgId(imgId);
	}
	
	@CacheEvict(value = "images", key = "#img.imgId")
	public boolean deleteImage(ImgMaster img) throws Exception {
		try {
			imgRepo.delete(img);
		} catch(Exception e) {
			return false;
		}
		return true;
	}
	
	
	public void saveImage(ImgMaster img) {
		try {
			imgRepo.save(img);
		}
		catch(Exception e) {
			logger.error("Failed to run: " + e);
		}
	}
}
