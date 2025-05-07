package com.tahaakocer.crm.config;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KeycloakConfig {

    @Value("${keycloak.util.auth-server-url}")
    private String authServerUrl;

    @Value("${keycloak.util.realm}")
    private String realm;

    @Value("${keycloak.util.client-id}")
    private String clientId;

    @Value("${keycloak.util.admin-username}")
    private String username;

    @Value("${keycloak.util.admin-password}")
    private String password;

    @Value("${keycloak.util.client-secret}")
    private String clientSecret;

    @Bean
    public Keycloak keycloak() {
        return KeycloakBuilder.builder()
                .serverUrl(authServerUrl)
                .realm(realm)
                .grantType(OAuth2Constants.PASSWORD)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .username(username)
                .password(password)
                .build();
    }


}