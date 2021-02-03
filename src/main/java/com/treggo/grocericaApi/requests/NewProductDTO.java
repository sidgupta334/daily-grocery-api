package com.treggo.grocericaApi.requests;

public class NewProductDTO {

	private Long productId;
	private String productName;
	private String brand;
	private String productDescription;
	private Long categoryId;
	private Long imgId;
	private int oldPrice;
	private int newPrice;
	private Long vendorId;

	public NewProductDTO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public NewProductDTO(Long productId, String productName, String brand, String productDescription, Long categoryId,
			Long imgId, int oldPrice, int newPrice, Long vendorId) {
		super();
		this.productId = productId;
		this.productName = productName;
		this.brand = brand;
		this.productDescription = productDescription;
		this.categoryId = categoryId;
		this.imgId = imgId;
		this.oldPrice = oldPrice;
		this.newPrice = newPrice;
		this.vendorId = vendorId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getProductDescription() {
		return productDescription;
	}

	public void setProductDescription(String productDescription) {
		this.productDescription = productDescription;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public Long getImgId() {
		return imgId;
	}

	public void setImgId(Long imgId) {
		this.imgId = imgId;
	}

	public int getOldPrice() {
		return oldPrice;
	}

	public void setOldPrice(int oldPrice) {
		this.oldPrice = oldPrice;
	}

	public int getNewPrice() {
		return newPrice;
	}

	public void setNewPrice(int newPrice) {
		this.newPrice = newPrice;
	}

	public Long getVendorId() {
		return vendorId;
	}

	public void setVendorId(Long vendorId) {
		this.vendorId = vendorId;
	}

}
