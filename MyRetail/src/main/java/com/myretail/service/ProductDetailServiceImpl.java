package com.myretail.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.myretail.dao.ProductPriceRepository;
import com.myretail.exception.ProductMisMatchException;
import com.myretail.exception.ProductNotFoundException;
import com.myretail.exception.ProductPriceNotFoundException;
import com.myretail.model.ProductInfo;
import com.myretail.request.ProductInfoRequest;
import com.myretail.response.ProductInfoResponse;

@Service
@Component
public class ProductDetailServiceImpl implements ProductDetailService {
	@Autowired
    private ProductPriceRepository productPriceRepository;
	@Autowired
	private RestTemplate restTemplate;

	@Value("${redskyApiHostUrl}")
	private String redSkyHost;

	@Value("${pathUrl}")
	private String pathUrl;

	@Value("${excludedQueryParams}")
	private String excludedQueryParams;

	@Value("${excludeQueryKey}")
	private String excludeQueryKey;

	@Value("${HTTP}")
	private String http;

	@SuppressWarnings({"unchecked","rawtypes"})
	@Override
	public ProductInfoResponse getProductInfoByProductId(String productId) {
		ProductInfoResponse productInfoResponse = new ProductInfoResponse();
		UriComponentsBuilder uriComponents = UriComponentsBuilder.newInstance();
		if(productId!=null){
		 String url=uriComponents.scheme(http).host(redSkyHost).path(pathUrl+productId).
					queryParam(excludeQueryKey, excludedQueryParams).build().toUriString();
        	try {
				ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
				Map<String, Map> infoMap = new HashMap<String, Map>();
        		if(response!=null&& response.getStatusCode()!=null){
        			infoMap = response.getBody();
        			if(infoMap!=null && infoMap.containsKey("product")){
						Map<String,Map> productMap = infoMap.get("product");
        				if(productMap.containsKey("item")){
							Map<String,Map> itemMap =productMap.get("item");
        					if(itemMap.containsKey("product_description")){
        						Map<String,String> prodDescrMap = itemMap.get(("product_description"));
        						if(prodDescrMap.containsKey("title")){
        							productInfoResponse.setTitle(prodDescrMap.get("title")) ;
        						}
        					}
        				}
        			}else{
        				throw new RuntimeException("Invalid Response returned");
        			}
        				ProductInfo productInfoFromRepo = productPriceRepository.findByProductId(productId);
        				if(productInfoFromRepo!=null && productInfoFromRepo.getCurrent_price()!=null){
            					productInfoResponse.setCurrentPrice(productInfoFromRepo.getCurrent_price());
            					productInfoResponse.setProductId(productId);
            			}else{
            				throw new ProductPriceNotFoundException();
            			}
        		}
        	   }
        	catch (ProductPriceNotFoundException e) {
        		throw new ProductPriceNotFoundException();
        	}
        	catch (Exception e) {
        		throw new ProductNotFoundException();
        	}

		}
		return productInfoResponse;
	}

	@Override
	public String updateProductPriceByProductId(ProductInfoRequest productInfoRequest, String productId) {
		if(productInfoRequest.getProductId()!=null && productId!=null && productId.equals(productInfoRequest.getProductId())){
			if(productInfoRequest.getCurrentPrice()!=null && !productInfoRequest.getCurrentPrice().getValue().isEmpty()){
				if(getProductInfoByProductId(productId)!=null){
					ProductInfo productInfo=new ProductInfo(productId, productInfoRequest.getCurrentPrice());
					productPriceRepository.save(productInfo);
				}else{
					throw new ProductNotFoundException();
				}
			}
		}else{
			throw new ProductMisMatchException();
		}
		return "Success";
	}

}
