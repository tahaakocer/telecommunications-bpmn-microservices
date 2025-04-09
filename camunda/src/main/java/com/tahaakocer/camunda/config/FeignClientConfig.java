package com.tahaakocer.camunda.config;

import com.tahaakocer.camunda.service.KeycloakTokenService;
import feign.RequestInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class FeignClientConfig {

    private final KeycloakTokenService keycloakTokenService;

    public FeignClientConfig(KeycloakTokenService keycloakTokenService) {
        this.keycloakTokenService = keycloakTokenService;
    }


    @Bean
    public RequestInterceptor keycloakRequestInterceptor() {
        return requestTemplate -> {
            String accessToken = keycloakTokenService.obtainAccessToken();
            log.info(" FEIGN CLIENT - Setting access token");
            requestTemplate.header("Authorization", "Bearer " + accessToken);
        };
    }
}