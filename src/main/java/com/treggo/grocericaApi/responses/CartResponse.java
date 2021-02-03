package com.treggo.grocericaApi.responses;

import java.util.List;

public class CartResponse {

	private Long cartId;
	private Long netTotal;
	private Long netQuantity;
	private List<ProductCartResponse> products;

	public CartResponse() {
		super();
		// TODO Auto-generated constructor stub
	}

	public CartResponse(Long cartId, Long netTotal, Long netQuantity, List<ProductCartResponse> products) {
		super();
		this.cartId = cartId;
		this.netTotal = netTotal;
		this.netQuantity = netQuantity;
		this.products = products;
	}

	public Long getCartId() {
		return cartId;
	}

	public void setCartId(Long cartId) {
		this.cartId = cartId;
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

	public List<ProductCartResponse> getProducts() {
		return products;
	}

	public void setProducts(List<ProductCartResponse> products) {
		this.products = products;
	}

}
