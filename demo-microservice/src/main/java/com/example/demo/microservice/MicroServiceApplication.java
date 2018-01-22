package com.example.demo.microservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableEurekaClient
@ServletComponentScan
public class MicroServiceApplication {

	private static final Logger logger = LoggerFactory
			.getLogger(MicroServiceApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(MicroServiceApplication.class, args);
	}

}
