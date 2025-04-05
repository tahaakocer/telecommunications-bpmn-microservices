package com.tahaakocer.camunda.controller;

import com.tahaakocer.camunda.dto.AuthResponse;
import com.tahaakocer.camunda.exception.GeneralException;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.AccessTokenResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@Slf4j
@RestController
public class AuthController {
    private final Keycloak keycloak;

    public AuthController(Keycloak keycloak) {
        this.keycloak = keycloak;
    }

    @GetMapping("/api/token")
    public ResponseEntity<AuthResponse> getAccessToken() {
        log.info("Getting access token from Keycloak.");
        try {
            AccessTokenResponse tokenResponse = this.keycloak.tokenManager().getAccessToken();
            log.info("Yeni access token başarıyla alındı.");
            return ResponseEntity.ok(AuthResponse.builder().accessToken(tokenResponse.getToken()).build());
        }
        catch (Exception e) {
            log.error("Keycloak'tan token alınırken hata oluştu: {}", e.getMessage());
            throw new GeneralException("Yeni access token alınamadı.", e);
        }

    }
}