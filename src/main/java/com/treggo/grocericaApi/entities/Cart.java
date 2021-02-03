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
public class Cart {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long cartId;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "user_id")
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JsonIgnore
	private Users user;

	@Column(nullable = false)
	private Long netTotal;

	@Column(nullable = false)
	private Long netQuantity;

	@Column(columnDefinition = "TEXT")
	private String productIds;

	@Column(columnDefinition = "TEXT")
	private String productQuantities;

	@Column(nullable = false)
	private LocalDate created_on;

	public Cart() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getProductQuantities() {
		return productQuantities;
	}

	public void setProductQuantities(String productQuantities) {
		this.productQuantities = productQuantities;
	}

	public Cart(Long cartId, Users user, Long netTotal, Long netQuantity, String productIds, String productQuantities,
			LocalDate created_on) {
		super();
		this.cartId = cartId;
		this.user = user;
		this.netTotal = netTotal;
		this.netQuantity = netQuantity;
		this.productIds = productIds;
		this.productQuantities = productQuantities;
		this.created_on = created_on;
	}

	public Long getCartId() {
		return cartId;
	}

	public void setCartId(Long cartId) {
		this.cartId = cartId;
	}

	public Users getUser() {
		return user;
	}

	public void setUser(Users user) {
		this.user = user;
	}

	public Long getNetTotal() {
		return netTotal;
	}

	public void setNetTotal(Long netTotal) {
		this.netTotal = netTotal;
	}

	public Long getNetQuantity() {
		return netQuantity;
	}

	public void setNetQuantity(Long netQuantity) {
		this.netQuantity = netQuantity;
	}

	public String getProductIds() {
		return productIds;
	}

	public void setProductIds(String productIds) {
		this.productIds = productIds;
	}

	public LocalDate getCreated_on() {
		return created_on;
	}

	public void setCreated_on(LocalDate created_on) {
		this.created_on = created_on;
	}

}
