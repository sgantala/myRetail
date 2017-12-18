package com.myretail;


import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import com.myretail.dao.ProductPriceRepository;
import com.myretail.model.ProductInfo;
import com.myretail.response.CurrentPrice;
@SpringBootApplication(scanBasePackages ={"com.myretail"})
public class MyRetailApplication {
@Autowired
private ProductPriceRepository repository;
	public static void main(String[] args) {
		SpringApplication.run(MyRetailApplication.class, args);
	}
	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}

	 @PostConstruct
	    public void storeSampleData() {
	        CurrentPrice currentPrice=new CurrentPrice("129.89", "USD");
	        repository.save(new ProductInfo("13860428",currentPrice));

	        CurrentPrice currentPrice2=new CurrentPrice("212.89", "USD");
	        repository.save(new ProductInfo("16696652",currentPrice2));
	    }
}
