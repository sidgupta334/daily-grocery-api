package com.treggo.grocericaApi.entities;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Product {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long productId;

	@Column(nullable = false)
	private String productName;

	@Column
	private String brand;

	@Column(columnDefinition = "TEXT")
	private String productDescription;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "category_id")
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JsonIgnore
	private Category category;

	@Column
	private Long vendorId;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "img_id")
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JsonIgnore
	private ImgMaster productImage;

	@Column
	private int oldPrice;

	@Column(nullable = false)
	private int newPrice;

	@Column(nullable = false)
	private LocalDate created_on;

	@Column
	private boolean isAvailable;

	public Product() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Product(Long productId, String productName, String brand, String productDescription, Category category,
			Long vendorId, ImgMaster productImage, int oldPrice, int newPrice, LocalDate created_on,
			boolean isAvailable) {
		super();
		this.productId = productId;
		this.productName = productName;
		this.brand = brand;
		this.productDescription = productDescription;
		this.category = category;
		this.vendorId = vendorId;
		this.productImage = productImage;
		this.oldPrice = oldPrice;
		this.newPrice = newPrice;
		this.created_on = created_on;
		this.isAvailable = isAvailable;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public ImgMaster getProductImage() {
		return productImage;
	}

	public void setProductImage(ImgMaster productImage) {
		this.productImage = productImage;
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

	public boolean isAvailable() {
		return isAvailable;
	}

	public void setAvailable(boolean isAvailable) {
		this.isAvailable = isAvailable;
	}

	public Long getVendorId() {
		return vendorId;
	}

	public void setVendorId(Long vendorId) {
		this.vendorId = vendorId;
	}

}
