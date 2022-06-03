package com.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.api", "com.actions", "com.jobrunr", "com.config"})
public class MainSpringBootApplication {

	public static void main(String[] args) {
		SpringApplication.run(MainSpringBootApplication.class, args);
	}
}
