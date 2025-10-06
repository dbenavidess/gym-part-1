package com.dbenavidess.gym_part_1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class GymPart1Application {

	public static void main(String[] args) {
		SpringApplication.run(GymPart1Application.class, args);
	}

}
