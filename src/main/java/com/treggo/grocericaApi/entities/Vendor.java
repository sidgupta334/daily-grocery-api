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
public class Vendor {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long vendorId;

	@Column(nullable = false)
	private String vendorName;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JsonIgnore
	private Users user;

	@Column(nullable = false)
	private LocalDate created_on;

	public Vendor() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Vendor(Long vendorId, String vendorName, Users user, LocalDate created_on) {
		super();
		this.vendorId = vendorId;
		this.vendorName = vendorName;
		this.user = user;
		this.created_on = created_on;
	}

	public Long getVendorId() {
		return vendorId;
	}

	public void setVendorId(Long vendorId) {
		this.vendorId = vendorId;
	}

	public String getVendorName() {
		return vendorName;
	}

	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}

	public Users getUser() {
		return user;
	}

	public void setUser(Users user) {
		this.user = user;
	}

	public LocalDate getCreated_on() {
		return created_on;
	}

	public void setCreated_on(LocalDate created_on) {
		this.created_on = created_on;
	}

}
