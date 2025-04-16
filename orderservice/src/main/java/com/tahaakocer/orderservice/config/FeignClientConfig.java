package com.tahaakocer.orderservice.config;

import com.tahaakocer.orderservice.service.KeycloakTokenService;
import com.tahaakocer.orderservice.utils.KeycloakUtil;
import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignClientConfig {

    @Bean
    public RequestInterceptor requestInterceptor(KeycloakTokenService tokenService) {
        return requestTemplate -> {
            String token = tokenService.obtainAccessToken();
            requestTemplate.header("Authorization", "Bearer " + token);
        };
    }
}