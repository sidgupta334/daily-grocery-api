package com.treggo.grocericaApi.entities;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Pincode {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long pincodeId;

	@Column(nullable = false)
	private boolean isActive;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	private LocalDate created_on;

	public Pincode() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Pincode(Long pincodeId, boolean isActive, String name, LocalDate created_on) {
		super();
		this.pincodeId = pincodeId;
		this.isActive = isActive;
		this.name = name;
		this.created_on = created_on;
	}

	public Long getPincodeId() {
		return pincodeId;
	}

	public void setPincodeId(Long pincodeId) {
		this.pincodeId = pincodeId;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public LocalDate getCreated_on() {
		return created_on;
	}

	public void setCreated_on(LocalDate created_on) {
		this.created_on = created_on;
	}

}
