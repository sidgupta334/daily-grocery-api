package com.treggo.grocericaApi.responses;

public class BannerResponse {

	private Long bannerId;
	private String url;
	private String bannerName;

	public BannerResponse() {
		super();
		// TODO Auto-generated constructor stub
	}

	public BannerResponse(Long bannerId, String url, String bannerName) {
		super();
		this.bannerId = bannerId;
		this.url = url;
		this.bannerName = bannerName;
	}

	public Long getBannerId() {
		return bannerId;
	}

	public void setBannerId(Long bannerId) {
		this.bannerId = bannerId;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getBannerName() {
		return bannerName;
	}

	public void setBannerName(String bannerName) {
		this.bannerName = bannerName;
	}

}
