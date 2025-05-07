package com.tahaakocer.crm.utils;

import com.tahaakocer.crm.exception.GeneralException;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.Form;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.representations.AccessTokenResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;


@Component
public class KeycloakUtil {
    @Value("${keycloak.util.auth-server-url}")
    private String authServerUrl;

    @Value("${keycloak.util.realm}")
    private String realm;

    @Value("${keycloak.util.client-id}")
    private String clientId;

    @Value("${keycloak.util.client-secret}")
    private String clientSecret;

    @SuppressWarnings("unchecked")
    public static String getKeycloakUsername() {
        try {
            return SecurityContextHolder.getContext().getAuthentication().getName();
        } catch (Exception e) {
            throw new GeneralException("username, security context'ten get edilemedi.");
        }
    }

    public Keycloak getKeycloakClientForUser(String u, String p) {
        return KeycloakBuilder.builder()
                .serverUrl(authServerUrl)
                .realm(realm)
                .grantType(OAuth2Constants.PASSWORD)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .username(u)
                .password(p)
                .build();
    }


}