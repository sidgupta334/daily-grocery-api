package com.treggo.grocericaApi.entities;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.treggo.grocericaApi.enums.userType;

@Entity
public class DeliveryUser {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long deliveryUserId;

	@Column(nullable = false)
	private String fullName;

	@Column(nullable = false)
	private String gender;

	@Column(nullable = false)
	private String email;

	@Enumerated(EnumType.STRING)
	private userType userType;

	@Column(nullable = false)
	private String mobile;

	@Column(nullable = false)
	private String dob;

	@Column(nullable = false)
	private String password;

	@Column(nullable = false)
	private boolean isActive;

	@Column(nullable = false)
	private LocalDate created_on;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "user_id")
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JsonIgnore
	private Users user;

	public DeliveryUser() {
		super();
		// TODO Auto-generated constructor stub
	}

	public DeliveryUser(Long deliveryUserId, String fullName, String gender, String email,
			com.treggo.grocericaApi.enums.userType userType, String mobile, String dob, String password,
			boolean isActive, LocalDate created_on, Users user) {
		super();
		this.deliveryUserId = deliveryUserId;
		this.fullName = fullName;
		this.gender = gender;
		this.email = email;
		this.userType = userType;
		this.mobile = mobile;
		this.dob = dob;
		this.password = password;
		this.isActive = isActive;
		this.created_on = created_on;
		this.user = user;
	}

	public Long getDeliveryUserId() {
		return deliveryUserId;
	}

	public void setDeliveryUserId(Long deliveryUserId) {
		this.deliveryUserId = deliveryUserId;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public userType getUserType() {
		return userType;
	}

	public void setUserType(userType userType) {
		this.userType = userType;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getDob() {
		return dob;
	}

	public void setDob(String dob) {
		this.dob = dob;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public LocalDate getCreated_on() {
		return created_on;
	}

	public void setCreated_on(LocalDate created_on) {
		this.created_on = created_on;
	}

	public Users getUser() {
		return user;
	}

	public void setUser(Users user) {
		this.user = user;
	}

}
