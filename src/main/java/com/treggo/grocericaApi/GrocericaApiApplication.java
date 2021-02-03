package com.treggo.grocericaApi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class GrocericaApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(GrocericaApiApplication.class, args);
	}

}
