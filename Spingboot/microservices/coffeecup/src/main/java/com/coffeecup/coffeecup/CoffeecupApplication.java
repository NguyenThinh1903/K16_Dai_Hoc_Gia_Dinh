package com.coffeecup.coffeecup;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class CoffeecupApplication {

	public static void main(String[] args) {
		SpringApplication.run(CoffeecupApplication.class, args);
	}

}
