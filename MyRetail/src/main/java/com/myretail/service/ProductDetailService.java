package com.myretail.service;


import com.myretail.request.ProductInfoRequest;
import com.myretail.response.ProductInfoResponse;

public interface ProductDetailService {
	public ProductInfoResponse getProductInfoByProductId(String id) ;

	public String updateProductPriceByProductId(ProductInfoRequest request, String productId);
}
