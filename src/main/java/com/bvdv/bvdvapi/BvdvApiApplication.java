package com.bvdv.bvdvapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = {"com.bvdv.bvdvapi.models"})
public class BvdvApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(BvdvApiApplication.class, args);
	}

}
