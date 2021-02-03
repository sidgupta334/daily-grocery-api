package com.treggo.grocericaApi.services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.treggo.grocericaApi.entities.BannerMaster;
import com.treggo.grocericaApi.entities.ImgMaster;
import com.treggo.grocericaApi.repositories.BannerRepository;
import com.treggo.grocericaApi.requests.NewBannerDTO;
import com.treggo.grocericaApi.responses.BannerResponse;

@Service
public class BannerService {

	@Autowired
	private BannerRepository repo;

	@Autowired
	private ImageService imgService;

	@Value("${app.server}")
	private String serverAddress;
	
	Logger logger = LoggerFactory.getLogger(BannerService.class);


	public BannerResponse createBanner(NewBannerDTO req) {
		
		ImgMaster img = imgService.getImage(req.getImgId());
		if (img == null) {
			return null;
		}

		BannerMaster banner = new BannerMaster();
		BeanUtils.copyProperties(req, banner);
		banner.setImage(img);
		banner.setCreated_on(LocalDate.now());

		try {
			repo.save(banner);
			return new BannerResponse(banner.getBannerId(),
					serverAddress + "images/download/" + banner.getImage().getImgId(), banner.getBannerName());
		} catch (Exception e) {
			logger.error("Failed to run: " + e);
			return null;
		}
	}

	public boolean deleteBanner(Long bannerId) {
		try {
			BannerMaster banner = repo.findByBannerId(bannerId);
			imgService.deleteImage(banner.getImage());
			repo.delete(banner);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public BannerResponse findByBannerId(Long bannerId) {
		BannerMaster banner = repo.findByBannerId(bannerId);
		return new BannerResponse(banner.getBannerId(),
				serverAddress + "images/download/" + banner.getImage().getImgId(), banner.getBannerName());
	}

	public List<BannerResponse> findAllBanners() {
		List<BannerResponse> res = new ArrayList<>();
		try {
			List<BannerMaster> banners = repo.findAll();
			for (BannerMaster banner : banners) {
				BannerResponse r = new BannerResponse();
				r.setBannerId(banner.getBannerId());
				r.setBannerName(banner.getBannerName());
				r.setUrl(serverAddress + "images/download/" + banner.getImage().getImgId());
				res.add(r);
			}
			return res;
		} catch (Exception e) {
			return null;
		}
	}
}
