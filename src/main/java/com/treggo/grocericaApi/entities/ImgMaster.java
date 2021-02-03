package com.treggo.grocericaApi.entities;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "IMG_MASTER")
public class ImgMaster {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long imgId;

	@Column(nullable = false)
	private String imgPath;

	@Column(nullable = false)
	private String fileExtension;

	@Column(nullable = false, columnDefinition = "TEXT")
	private String imgData;

	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "image")
	private BannerMaster bannerMaster;

	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "categoryImage")
	private Category category;

	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "productImage")
	private Product product;

	public ImgMaster() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ImgMaster(Long imgId, String imgPath, String fileExtension, String imgData, BannerMaster bannerMaster,
			Category category, Product product) {
		super();
		this.imgId = imgId;
		this.imgPath = imgPath;
		this.fileExtension = fileExtension;
		this.imgData = imgData;
		this.bannerMaster = bannerMaster;
		this.category = category;
		this.product = product;
	}

	public Long getImgId() {
		return imgId;
	}

	public void setImgId(Long imgId) {
		this.imgId = imgId;
	}

	public String getImgPath() {
		return imgPath;
	}

	public void setImgPath(String imgPath) {
		this.imgPath = imgPath;
	}

	public String getFileExtension() {
		return fileExtension;
	}

	public void setFileExtension(String fileExtension) {
		this.fileExtension = fileExtension;
	}

	public String getImgData() {
		return imgData;
	}

	public void setImgData(String imgData) {
		this.imgData = imgData;
	}

	public BannerMaster getBannerMaster() {
		return bannerMaster;
	}

	public void setBannerMaster(BannerMaster bannerMaster) {
		this.bannerMaster = bannerMaster;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

}
