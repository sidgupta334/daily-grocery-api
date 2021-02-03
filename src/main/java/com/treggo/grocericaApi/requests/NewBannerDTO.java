package com.treggo.grocericaApi.requests;

public class NewBannerDTO {

	private Long bannerId;
	private Long imgId;
	private String bannerName;

	public NewBannerDTO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public NewBannerDTO(Long bannerId, Long imgId, String bannerName) {
		super();
		this.bannerId = bannerId;
		this.imgId = imgId;
		this.bannerName = bannerName;
	}

	public Long getBannerId() {
		return bannerId;
	}

	public void setBannerId(Long bannerId) {
		this.bannerId = bannerId;
	}

	public Long getImgId() {
		return imgId;
	}

	public void setImgId(Long imgId) {
		this.imgId = imgId;
	}

	public String getBannerName() {
		return bannerName;
	}

	public void setBannerName(String bannerName) {
		this.bannerName = bannerName;
	}

}
