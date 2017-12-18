package com.myretail.service;

import javax.naming.ServiceUnavailableException;

import com.myretail.request.ProductInfoRequest;
import com.myretail.response.ProductInfoResponse;

public interface ProductDetailService {
	public ProductInfoResponse getProductInfoByProductId(String id) throws ServiceUnavailableException;

	public String updateProductPriceByProductId(ProductInfoRequest request, String productId) throws ServiceUnavailableException;
}
