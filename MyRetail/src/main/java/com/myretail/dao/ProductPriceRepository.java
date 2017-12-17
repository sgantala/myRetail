package com.myretail.dao;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.myretail.model.ProductInfo;
public interface ProductPriceRepository extends MongoRepository<ProductInfo, String> {
	public ProductInfo findByProductId(String productId);
}
