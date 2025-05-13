package com.tahaakocer.crm.config;


import com.tahaakocer.crm.service.KeycloakService;
import feign.RequestInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class FeignClientConfig {

    private final KeycloakService keycloakService;

    public FeignClientConfig(KeycloakService keycloakService) {
        this.keycloakService = keycloakService;
    }


    @Bean
    public RequestInterceptor keycloakRequestInterceptor() {
        return requestTemplate -> {
            String accessToken = keycloakService.obtainAccessToken();
            log.info(" FEIGN CLIENT - Setting access token");
            requestTemplate.header("Authorization", "Bearer " + accessToken);
        };
    }
}