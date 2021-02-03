package com.treggo.grocericaApi.requests;

public class NewCategoryDTO {

	private Long categoryId;
	private String categoryName;
	private Long imgId;

	public NewCategoryDTO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public NewCategoryDTO(Long categoryId, String categoryName, Long imgId) {
		super();
		this.categoryId = categoryId;
		this.categoryName = categoryName;
		this.imgId = imgId;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public Long getImgId() {
		return imgId;
	}

	public void setImgId(Long imgId) {
		this.imgId = imgId;
	}

}
