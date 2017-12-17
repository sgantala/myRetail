package com.myretail.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Component;

import com.myretail.response.CurrentPrice;
@Component
@Document(collection = "ProductInfo")
public class ProductInfo {
	@Id
	private String productId;
    public CurrentPrice current_price;
    public ProductInfo(){

    }
	public ProductInfo(String productId, CurrentPrice current_price) {
		super();
		this.productId = productId;
		this.current_price = current_price;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public CurrentPrice getCurrent_price() {
		return current_price;
	}

	public void setCurrent_price(CurrentPrice current_price) {
		this.current_price = current_price;
	}



}
