package com.treggo.grocericaApi.responses;

public class CategoryResponse {

	private Long categoryId;
	private String url;
	private String categoryName;

	public CategoryResponse() {
		super();
		// TODO Auto-generated constructor stub
	}

	public CategoryResponse(Long categoryId, String url, String categoryName) {
		super();
		this.categoryId = categoryId;
		this.url = url;
		this.categoryName = categoryName;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

}
