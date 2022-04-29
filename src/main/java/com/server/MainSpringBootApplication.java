package com.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {"com.example.api", "com.actions"})
@EnableJpaRepositories(basePackages = "com.example.api.repository")
@EntityScan(basePackages = "com.example.api.entities")
public class MainSpringBootApplication {

	public static void main(String[] args) {
		SpringApplication.run(MainSpringBootApplication.class, args);
	}

}
