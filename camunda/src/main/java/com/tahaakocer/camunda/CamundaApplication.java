package com.tahaakocer.camunda;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.web.bind.annotation.CrossOrigin;

@SpringBootApplication
@EnableFeignClients
@EnableDiscoveryClient

public class CamundaApplication {

	public static void main(String[] args) {
		SpringApplication.run(CamundaApplication.class, args);
	}

}
