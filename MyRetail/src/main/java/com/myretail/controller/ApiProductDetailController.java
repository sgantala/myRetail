package com.myretail.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.myretail.request.ProductInfoRequest;
import com.myretail.response.ProductInfoResponse;
import com.myretail.service.ProductDetailService;

@RestController
public class ApiProductDetailController {
@Autowired
private ProductDetailService productDetailService;

@RequestMapping(value = "/products/{id}", method = RequestMethod.GET, produces = "application/json")
@ResponseBody
public ProductInfoResponse getProductInfo(@PathVariable("id") String productId) {
	return productDetailService.getProductInfoByProductId(productId);
}

@RequestMapping(value = "/products/{id}", method = RequestMethod.PUT,  produces = "application/json")
@ResponseBody
public String updateProductPrice(@RequestBody ProductInfoRequest request,@PathVariable("id") String productId) {
	return productDetailService.updateProductPriceByProductId(request,productId);
}
}
