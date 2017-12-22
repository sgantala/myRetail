package com.myretail.request;

import java.io.Serializable;

import com.myretail.response.CurrentPrice;

public class ProductInfoRequest implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private String productId;
	private String title;
	private CurrentPrice currentPrice;

	public ProductInfoRequest() {
		super();
	}

	public ProductInfoRequest(String productId, String title, CurrentPrice currentPrice) {
		super();
		this.productId = productId;
		this.title = title;
		this.currentPrice = currentPrice;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public CurrentPrice getCurrentPrice() {
		return currentPrice;
	}

	public void setCurrentPrice(CurrentPrice currentPrice) {
		this.currentPrice = currentPrice;
	}
}
