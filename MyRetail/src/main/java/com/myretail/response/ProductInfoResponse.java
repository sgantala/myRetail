package com.myretail.response;

import java.io.Serializable;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class ProductInfoResponse implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private String productId;
	private String title;
	private CurrentPrice currentPrice;
	@JsonIgnore
	private HttpStatus statusCode;

	public ProductInfoResponse() {
		super();
	}

	public ProductInfoResponse(String productId, String title, CurrentPrice currentPrice) {
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

	public HttpStatus getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(HttpStatus statusCode) {
		this.statusCode = statusCode;
	}

}
