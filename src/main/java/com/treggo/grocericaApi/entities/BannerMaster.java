package com.treggo.grocericaApi.entities;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "BANNER_MASTER")
public class BannerMaster {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long bannerId;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "img_id")
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JsonIgnore
	private ImgMaster image;
	
	@Column
	private String bannerName;


	@Column(nullable = false)
	private LocalDate created_on;


	public BannerMaster() {
		super();
		// TODO Auto-generated constructor stub
	}


	public Long getBannerId() {
		return bannerId;
	}


	public void setBannerId(Long bannerId) {
		this.bannerId = bannerId;
	}


	public ImgMaster getImage() {
		return image;
	}


	public void setImage(ImgMaster image) {
		this.image = image;
	}


	public String getBannerName() {
		return bannerName;
	}


	public void setBannerName(String bannerName) {
		this.bannerName = bannerName;
	}


	public LocalDate getCreated_on() {
		return created_on;
	}


	public void setCreated_on(LocalDate created_on) {
		this.created_on = created_on;
	}


	public BannerMaster(Long bannerId, ImgMaster image, String bannerName, LocalDate created_on) {
		super();
		this.bannerId = bannerId;
		this.image = image;
		this.bannerName = bannerName;
		this.created_on = created_on;
	}
	
	
	
}
