package com.myretail.controller;

import javax.naming.ServiceUnavailableException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.myretail.request.ProductInfoRequest;
import com.myretail.response.ProductInfoResponse;
import com.myretail.service.ProductDetailService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api (value="MyRetailAPI", description = "Retrieve/Modify Product Information by product id")
@RestController
public class ApiProductDetailController {
@Autowired
private ProductDetailService productDetailService;

@ApiOperation(value = "Gets the product and price information by product id")
@ApiResponses(value = {@ApiResponse(code = 200, message = "Success response"),@ApiResponse(code = 404, message = "Product not found")})
@RequestMapping(value = "/products/{id}", method = RequestMethod.GET, produces = "application/json")
public @ResponseBody ProductInfoResponse getProductInfo(@PathVariable("id") String productId) {
	ProductInfoResponse productInfoResponse = new ProductInfoResponse();
	try {
		productInfoResponse= productDetailService.getProductInfoByProductId(productId);
	} catch (ServiceUnavailableException e) {
		e.printStackTrace();
	}
	return productInfoResponse;
}

@ApiOperation(value = "Modifies the product price")
@ApiResponses(value = {@ApiResponse(code = 201, message = "Updated"),@ApiResponse(code = 400, message = "ProductId in request path and body doesn't match"),
                @ApiResponse(code = 404, message = "Product not found")})
@RequestMapping(value = "/products/{id}", method = RequestMethod.PUT,  produces = "application/json")
@ResponseStatus(HttpStatus.CREATED)
public String updateProductPrice(@RequestBody ProductInfoRequest request,@PathVariable("id") String productId) {
	String response=null;
	try {
		response= productDetailService.updateProductPriceByProductId(request,productId);
	} catch (ServiceUnavailableException e) {
		e.printStackTrace();
	}
	return response;
}
}
