package com.tahaakocer.externalapiservice.config;

import com.tahaakocer.externalapiservice.service.KeycloakTokenService;
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