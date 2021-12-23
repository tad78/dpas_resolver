package com.sainsburys.dpas.provider;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class DpasApplication {

	public static void main(String[] args) {
		SpringApplication.run(DpasApplication.class, args);		
		
	}
}
