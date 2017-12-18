package com.myretail.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
@Configuration
@EnableMongoRepositories(basePackages="com.myretail.dao")
public class MongoDBConfig {

}
//Create ApplicationConfig for REST