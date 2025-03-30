package com.coffeecup.coffeecupadminsystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class CoffeecupAdminSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(CoffeecupAdminSystemApplication.class, args);
	}

}
