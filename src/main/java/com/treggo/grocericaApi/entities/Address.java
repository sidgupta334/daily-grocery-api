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

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Address {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long addressId;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "user_id")
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JsonIgnore
	private Users user;

	@Column
	private String title;

	@Column(nullable = false)
	private String address1;

	@Column
	private String address2;

	@Column
	private String city;

	@Column(nullable = false)
	private String pincode;

	@Column(nullable = false)
	private String state;

	@Column
	private String country;

	@Column
	private String mobile;

	@Column
	private String lattitude;

	@Column
	private String longitude;

	@Column(nullable = false)
	private LocalDate created_on;

	public Address() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Long getAddressId() {
		return addressId;
	}

	public Address(Long addressId, Users user, String title, String address1, String address2, String city,
			String pincode, String state, String country, String mobile, String lattitude, String longitude,
			LocalDate created_on) {
		super();
		this.addressId = addressId;
		this.user = user;
		this.title = title;
		this.address1 = address1;
		this.address2 = address2;
		this.city = city;
		this.pincode = pincode;
		this.state = state;
		this.country = country;
		this.mobile = mobile;
		this.lattitude = lattitude;
		this.longitude = longitude;
		this.created_on = created_on;
	}

	public void setAddressId(Long addressId) {
		this.addressId = addressId;
	}

	public Users getUser() {
		return user;
	}

	public void setUser(Users user) {
		this.user = user;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAddress1() {
		return address1;
	}

	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	public String getAddress2() {
		return address2;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getPincode() {
		return pincode;
	}

	public void setPincode(String pincode) {
		this.pincode = pincode;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public LocalDate getCreated_on() {
		return created_on;
	}

	public void setCreated_on(LocalDate created_on) {
		this.created_on = created_on;
	}

	public String getLattitude() {
		return lattitude;
	}

	public void setLattitude(String lattitude) {
		this.lattitude = lattitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

}
