package com.tahaakocer.externalapiservice.service;



import com.tahaakocer.externalapiservice.exception.GeneralException;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.AccessTokenResponse;
import org.springframework.stereotype.Service;
@Service
@Slf4j
public class KeycloakTokenService {

    private final Keycloak keycloakClient;

    public KeycloakTokenService(Keycloak keycloakClient) {
        this.keycloakClient = keycloakClient;
    }

    public String obtainAccessToken() {
        try {
            AccessTokenResponse tokenResponse = this.keycloakClient.tokenManager().getAccessToken();
            String accessToken = tokenResponse.getToken();

            log.info("Yeni access token başarıyla alındı.");
            return accessToken;
        } catch (Exception e) {
            log.error("Keycloak'tan token alınırken hata oluştu: {}", e.getMessage());
            throw new GeneralException("Yeni access token alınamadı.", e);
        }
    }
}
