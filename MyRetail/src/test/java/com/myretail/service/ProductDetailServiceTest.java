package com.myretail.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.myretail.dao.ProductPriceRepository;
import com.myretail.exception.ProductMisMatchException;
import com.myretail.exception.ProductNotFoundException;
import com.myretail.exception.ProductPriceNotFoundException;
import com.myretail.model.ProductInfo;
import com.myretail.request.ProductInfoRequest;
import com.myretail.response.CurrentPrice;
import com.myretail.response.ProductInfoResponse;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
@WebMvcTest(ProductDetailService.class)
public class ProductDetailServiceTest{

@Configuration
 static class ProductDetailServiceTestConfiguration {
	@Bean
	public ProductDetailService productDetailService() {
		return new ProductDetailServiceImpl();
	}
	@Bean
	public ProductPriceRepository productPriceRepository() {
		return Mockito.mock(ProductPriceRepository.class);
	}

	@Bean
	public RestTemplate restTemplate() {
		return Mockito.mock(RestTemplate.class);
	}
 }

@Autowired
private ProductDetailService productDetailService ;
@Autowired
private RestTemplate restTemplateMock;
@Autowired
private ProductPriceRepository productPriceRepositoryMock;

private static final String successResponseEntityMock =readFile("productData.txt");
@Before
public void setUp(){
	Mockito.reset(this.restTemplateMock,this.productPriceRepositoryMock);
}
public static String readFile(String filename) {
    String result = "";
    try {
    	InputStream inputStream =ProductDetailServiceTest.class.getResourceAsStream(filename);
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder sb = new StringBuilder();
        String line = br.readLine();
        while (line != null) {
            sb.append(line);
            line = br.readLine();
        }
        result = sb.toString();
    } catch(Exception e) {
        e.printStackTrace();
    }
    return result;
}
private Map jsonStringToMapConvert(String responseEntityMock){
	Map<String, Object> map = new HashMap<String, Object>();
	try {
		ObjectMapper mapper = new ObjectMapper();
		String json = responseEntityMock;
		// convert JSON string to Map
		map = mapper.readValue(json, new TypeReference<Map<String, Object>>(){});
		System.out.println(map);
	} catch (JsonGenerationException e) {
		e.printStackTrace();
	} catch (JsonMappingException e) {
		e.printStackTrace();
	} catch (IOException e) {
		e.printStackTrace();
	}
	return map;
}

private ResponseEntity<Object> mockResponseSetup(String responseEntityMock){
	Map bodyMap = jsonStringToMapConvert(responseEntityMock);
	Map<String,List<String>> headersMap= new HashMap<>();
	List<String> listHeaders=new ArrayList<>();
	listHeaders.add("application/json");
	headersMap.put("Content-Type", listHeaders);
	MultiValueMap<String, String> headers = new LinkedMultiValueMap<>(headersMap);
	HttpStatus httpStatus=HttpStatus.OK;
	ResponseEntity<Object> responseObject = new ResponseEntity<Object>(bodyMap, headers, httpStatus);
	return responseObject;
}


@Test
public void testGetProductinfoServiceoWhenProductIdExists() throws Exception {
	Mockito.when(this.restTemplateMock.getForEntity(anyString(), anyObject())).thenReturn(mockResponseSetup(successResponseEntityMock));
	CurrentPrice current_price= new CurrentPrice("182.59", "USD");
	ProductInfo productInfo = new ProductInfo("123", current_price);
	Mockito.when(this.productPriceRepositoryMock.findByProductId(anyString())).thenReturn(productInfo);

	ProductInfoResponse productInfoResponse= this.productDetailService.getProductInfoByProductId("123");

	assertEquals(current_price,productInfoResponse.getCurrentPrice());
	verify(this.productPriceRepositoryMock, times(1)).findByProductId(anyString());
	verify(this.restTemplateMock, times(1)).getForEntity(anyString(), anyObject());
}

@Test(expected=ProductNotFoundException.class)
public void testGetProductinfoServiceWhenProductNotFound() throws Exception {
	Mockito.when(this.restTemplateMock.getForEntity(anyString(), anyObject())).thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

	this.productDetailService.getProductInfoByProductId("123");
}

@Test(expected=ProductPriceNotFoundException.class)
public void testGetProductinfoServiceoWhenProductPriceNotFound() throws Exception {
	Mockito.when(this.restTemplateMock.getForEntity(anyString(), anyObject())).thenReturn(mockResponseSetup(successResponseEntityMock));
	Mockito.when(this.productPriceRepositoryMock.findByProductId(anyString())).thenReturn(null);

	this.productDetailService.getProductInfoByProductId("123");
}

@Test
public void testUpdateProductPriceInRepo() throws Exception {
	ProductInfoRequest productInfoRequest = new ProductInfoRequest("123", "The Samsung TV", new CurrentPrice("199.99", "USD"));
	CurrentPrice current_price = new CurrentPrice("132.32", "USD");
	ProductInfo productInfo = new ProductInfo("123", current_price);

	Mockito.when(this.restTemplateMock.getForEntity(anyString(), anyObject())).thenReturn(mockResponseSetup(successResponseEntityMock));
	Mockito.when(this.productPriceRepositoryMock.findByProductId(anyString())).thenReturn(productInfo);
	Mockito.when(this.productPriceRepositoryMock.save(any(ProductInfo.class))).thenReturn(productInfo);

	String response=this.productDetailService.updateProductPriceByProductId(productInfoRequest, "123");

	assertEquals("Success",response);
}

@Test(expected=ProductMisMatchException.class)
public void testUpdateProductPriceFailedWhenProductIdMisMatch() throws Exception {
	ProductInfoRequest productInfoRequest = new ProductInfoRequest("123", "The Samsung TV", new CurrentPrice("199.99", "USD"));

	this.productDetailService.updateProductPriceByProductId(productInfoRequest, "456");
}

@Test(expected=ProductNotFoundException.class)
public void testUpdateProductPriceFailedWhenProductwasNotFound() throws Exception {
	ProductInfoRequest productInfoRequest = new ProductInfoRequest("123", "The Samsung TV", new CurrentPrice("199.99", "USD"));
	Mockito.when(this.restTemplateMock.getForEntity(anyString(), anyObject())).thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

	this.productDetailService.updateProductPriceByProductId(productInfoRequest, "123");
}

}
