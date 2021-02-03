package com.treggo.grocericaApi.responses;

public class AllBannersResponse {

	private Long bannerId;
	private String url;

	public AllBannersResponse() {
		super();
		// TODO Auto-generated constructor stub
	}

	public AllBannersResponse(Long bannerId, String url) {
		super();
		this.bannerId = bannerId;
		this.url = url;
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

}
