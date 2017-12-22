package com.myretail.controller;


import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.naming.ServiceUnavailableException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myretail.exception.ProductMisMatchException;
import com.myretail.exception.ProductNotFoundException;
import com.myretail.request.ProductInfoRequest;
import com.myretail.response.CurrentPrice;
import com.myretail.response.ProductInfoResponse;
import com.myretail.service.ProductDetailService;

@RunWith(SpringJUnit4ClassRunner.class)
@WebMvcTest(ApiProductDetailController.class)
public class ApiProductDetailControllerTest{
	@Configuration
	static class ProductDetailControllerTestConfiguration {

	@Bean
	public ProductDetailService productDetailService() {
		return Mockito.mock(ProductDetailService.class);
	}

	@Bean
	public ApiProductDetailController productDetailController() {
		return new ApiProductDetailController();
	}
	}

	@Autowired
	private ApiProductDetailController productDetailController;

	@Autowired
	private ProductDetailService productDetailServiceMock;

	@Before
	public void setUp(){
		Mockito.reset(this.productDetailServiceMock);
	}

	public void setupForGetProductInfoWhenProductIdExists() throws ServiceUnavailableException {
		CurrentPrice currentPrice = new CurrentPrice("12.59","USD");
		ProductInfoResponse productInfoResponse = new ProductInfoResponse("123","test",currentPrice);
		Mockito.when(this.productDetailServiceMock.getProductInfoByProductId(anyString())).thenReturn(productInfoResponse);
	}

	public void setupForGetProductInfoWhenProductIdNotExists() throws ServiceUnavailableException {
		Mockito.when(this.productDetailServiceMock.getProductInfoByProductId(anyString())).thenThrow(new ProductNotFoundException());
	}


	public static String asJsonString(final Object obj) {
	    try {
	        return new ObjectMapper().writeValueAsString(obj);
	    } catch (Exception e) {
	        throw new RuntimeException(e);
	    }
	}

	@Test
	public void testGetProductInfoWhenProductIdExists() throws Exception {
		setupForGetProductInfoWhenProductIdExists();
		MockMvc mockMvc= MockMvcBuilders.standaloneSetup(this.productDetailController).build();
		mockMvc.perform(get("/products/{id}","123")).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("title").value("test"))
				.andExpect(jsonPath("productId").value("123"))
				.andExpect(jsonPath("currentPrice").isNotEmpty());
	}

	@Test
	public void testGetProductInfoWhenProductIdNotExist() throws Exception {
		setupForGetProductInfoWhenProductIdNotExists();
		MockMvc mockMvc= MockMvcBuilders.standaloneSetup(this.productDetailController).build();
		mockMvc.perform(get("/products/{id}","456")).andExpect(status().isNotFound());
	}

	@Test
	public void testUpdateProductPriceWhenProductExistAndProductIdsMatch() throws Exception{
		CurrentPrice currentPrice = new CurrentPrice("12.59","USD");
		ProductInfoRequest request = new ProductInfoRequest("567","test",currentPrice);
		Mockito.when(this.productDetailServiceMock.updateProductPriceByProductId(anyObject(), anyString())).thenReturn("Success");
		MockMvc mockMvc= MockMvcBuilders.standaloneSetup(this.productDetailController).build();
		mockMvc.perform(put("/products/{id}", "567")
				.contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(request)))
				.andExpect(status().isCreated());
		verify(this.productDetailServiceMock,times(1)).updateProductPriceByProductId(anyObject(), anyString());
		verifyNoMoreInteractions(this.productDetailServiceMock);
	}

	@Test
	public void testUpdateProductPriceWhenProductIdMisMatched() throws Exception{
		CurrentPrice currentPrice = new CurrentPrice("123.59","AUD");
		ProductInfoRequest productInfoRequest= new ProductInfoRequest("567","Test",currentPrice);
		Mockito.when(this.productDetailServiceMock.updateProductPriceByProductId(anyObject(), anyString())).thenThrow(new ProductMisMatchException());
		MockMvc mockMvc= MockMvcBuilders.standaloneSetup(this.productDetailController).build();
		mockMvc.perform(put("/products/{id}", "123")
				.contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(productInfoRequest)))
				.andExpect(status().isBadRequest());
		verify(this.productDetailServiceMock,times(1)).updateProductPriceByProductId(anyObject(), anyString());
		verifyNoMoreInteractions(this.productDetailServiceMock);
	}

	@Test
	public void testUpdateProductPriceWhenProductDoesNotExist() throws Exception{
		CurrentPrice currentPrice = new CurrentPrice("13.59","USD");
		ProductInfoRequest productInfoRequest= new ProductInfoRequest("1386","Test",currentPrice);
		Mockito.when(this.productDetailServiceMock.updateProductPriceByProductId(anyObject(), anyString())).thenThrow(new ProductNotFoundException());
		MockMvc mockMvc= MockMvcBuilders.standaloneSetup(this.productDetailController).build();
		mockMvc.perform(put("/products/{id}", "1386")
				.contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(productInfoRequest)))
				.andExpect(status().isNotFound());
		verify(this.productDetailServiceMock,times(1)).updateProductPriceByProductId(anyObject(), anyString());
		verifyNoMoreInteractions(this.productDetailServiceMock);
	}
}

