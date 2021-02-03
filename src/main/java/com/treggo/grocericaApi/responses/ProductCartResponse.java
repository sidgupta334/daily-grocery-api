package com.treggo.grocericaApi.responses;

public class ProductCartResponse {

	private Long productId;
	private String productName;
	private String brand;
	private String productDescription;
	private String url;
	private Long categoryId;
	private String categoryName;
	private Long quantity;
	private int oldPrice;
	private int newPrice;
	private int discountPercentage;

	public ProductCartResponse() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ProductCartResponse(Long productId, String productName, String brand, String productDescription, String url,
			Long categoryId, String categoryName, Long quantity, int oldPrice, int newPrice, int discountPercentage) {
		super();
		this.productId = productId;
		this.productName = productName;
		this.brand = brand;
		this.productDescription = productDescription;
		this.url = url;
		this.categoryId = categoryId;
		this.categoryName = categoryName;
		this.quantity = quantity;
		this.oldPrice = oldPrice;
		this.newPrice = newPrice;
		this.discountPercentage = discountPercentage;
	}

	public int getDiscountPercentage() {
		return discountPercentage;
	}

	public void setDiscountPercentage(int discountPercentage) {
		this.discountPercentage = discountPercentage;
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

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
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

	public Long getQuantity() {
		return quantity;
	}

	public void setQuantity(Long quantity) {
		this.quantity = quantity;
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

}
