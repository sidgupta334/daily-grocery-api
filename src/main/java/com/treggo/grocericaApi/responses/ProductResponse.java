package com.treggo.grocericaApi.responses;

import java.time.LocalDate;

public class ProductResponse {

	private Long productId;
	private String productName;
	private String brand;
	private String productDescription;
	private Long categoryId;
	private String categoryName;
	private String url;
	private int oldPrice;
	private int discountPercentage;
	private int newPrice;
	private boolean isAvailable;
	private Long vendorId;
	private String vendor;
	private LocalDate created_on;

	public ProductResponse() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ProductResponse(Long productId, String productName, String brand, String productDescription, Long categoryId,
			String categoryName, String url, int oldPrice, int discountPercentage, int newPrice, boolean isAvailable,
			Long vendorId, String vendor, LocalDate created_on) {
		super();
		this.productId = productId;
		this.productName = productName;
		this.brand = brand;
		this.productDescription = productDescription;
		this.categoryId = categoryId;
		this.categoryName = categoryName;
		this.url = url;
		this.oldPrice = oldPrice;
		this.discountPercentage = discountPercentage;
		this.newPrice = newPrice;
		this.isAvailable = isAvailable;
		this.vendorId = vendorId;
		this.vendor = vendor;
		this.created_on = created_on;
	}

	public Long getVendorId() {
		return vendorId;
	}

	public void setVendorId(Long vendorId) {
		this.vendorId = vendorId;
	}

	public String getVendor() {
		return vendor;
	}

	public void setVendor(String vendor) {
		this.vendor = vendor;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
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

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getOldPrice() {
		return oldPrice;
	}

	public void setOldPrice(int oldPrice) {
		this.oldPrice = oldPrice;
	}

	public int getDiscountPercentage() {
		return discountPercentage;
	}

	public void setDiscountPercentage(int discountPercentage) {
		this.discountPercentage = discountPercentage;
	}

	public int getNewPrice() {
		return newPrice;
	}

	public void setNewPrice(int newPrice) {
		this.newPrice = newPrice;
	}

	public boolean isAvailable() {
		return isAvailable;
	}

	public void setAvailable(boolean isAvailable) {
		this.isAvailable = isAvailable;
	}

	public LocalDate getCreated_on() {
		return created_on;
	}

	public void setCreated_on(LocalDate created_on) {
		this.created_on = created_on;
	}

}
