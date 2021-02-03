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

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Category {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long categoryId;

	@Column
	private String categoryName;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "img_id")
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JsonIgnore
	private ImgMaster categoryImage;

	@Column(nullable = false)
	private LocalDate created_on;

	public Category() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Category(Long categoryId, String categoryName, ImgMaster categoryImage, LocalDate created_on) {
		super();
		this.categoryId = categoryId;
		this.categoryName = categoryName;
		this.categoryImage = categoryImage;
		this.created_on = created_on;
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

	public ImgMaster getCategoryImage() {
		return categoryImage;
	}

	public void setCategoryImage(ImgMaster categoryImage) {
		this.categoryImage = categoryImage;
	}

	public LocalDate getCreated_on() {
		return created_on;
	}

	public void setCreated_on(LocalDate created_on) {
		this.created_on = created_on;
	}

}
