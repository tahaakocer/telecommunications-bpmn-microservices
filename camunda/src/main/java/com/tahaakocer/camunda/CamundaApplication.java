package com.tahaakocer.camunda;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.web.bind.annotation.CrossOrigin;

@SpringBootApplication
@EnableFeignClients
public class CamundaApplication {

	public static void main(String[] args) {
		SpringApplication.run(CamundaApplication.class, args);
	}

}
