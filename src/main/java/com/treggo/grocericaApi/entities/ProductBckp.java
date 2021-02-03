package com.treggo.grocericaApi.entities;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class ProductBckp {

	@Id
	private Long productId;

	@Column(nullable = false)
	private String productName;

	@Column
	private String brand;

	@Column(columnDefinition = "TEXT")
	private String productDescription;

	@Column(nullable = false)
	private Long categoryId;

	@Column
	private String categoryName;

	@Column
	private int oldPrice;

	@Column(nullable = false)
	private int newPrice;

	@Column
	private Long vendorId;

	@Column
	private String vendor;

	@Column(nullable = false)
	private LocalDate created_on;

	public ProductBckp() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ProductBckp(Long productId, String productName, String brand, String productDescription, Long categoryId,
			String categoryName, int oldPrice, int newPrice, Long vendorId, String vendor, LocalDate created_on) {
		super();
		this.productId = productId;
		this.productName = productName;
		this.brand = brand;
		this.productDescription = productDescription;
		this.categoryId = categoryId;
		this.categoryName = categoryName;
		this.oldPrice = oldPrice;
		this.newPrice = newPrice;
		this.vendorId = vendorId;
		this.vendor = vendor;
		this.created_on = created_on;
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

	public LocalDate getCreated_on() {
		return created_on;
	}

	public void setCreated_on(LocalDate created_on) {
		this.created_on = created_on;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
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

}
