package com.tahaakocer.crm.service;

import com.tahaakocer.crm.exception.GeneralException;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.GroupRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
@Slf4j
public class KeycloakService {

    private final Keycloak keycloakClient;

    @Value("${keycloak.util.realm}")
    private String realm;

    @Value("${keycloak.util.client-id}")
    private String clientId;

    @Value("${keycloak.util.client-secret}")
    private String clientSecret;

    @Value("${keycloak.util.auth-server-url}")
    private String authServerUrl;


    public KeycloakService(Keycloak keycloakClient) {
        this.keycloakClient = keycloakClient;
    }
    public void assignUserToPartnerGroup(String userId) {
        assignGroupRoleToUser(userId, "partner");

    }

    public void assignUserToCustomerGroup(String userId) {
        assignGroupRoleToUser(userId, "customer");
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

    public String createKeycloakUser(String firstName, String lastName,
                                     String email, String password) {
        log.info("Creating Keycloak user with realm: {}, clientId: {}", realm, clientId);
        RealmResource realmResource = this.keycloakClient.realm(realm);
        UsersResource usersResource = realmResource.users();

        // Kullanıcı adı
        List<UserRepresentation> existingUsers = usersResource.searchByUsername(email, true);
        if (!existingUsers.isEmpty()) {
            log.error("Kullanıcı adı zaten mevcut: {}", email);
            throw new GeneralException("Kullanıcı adı zaten mevcut.");
        }

        // E-posta
        List<UserRepresentation> existingEmails = usersResource.searchByEmail(email, true);
        if (!existingEmails.isEmpty()) {
            log.error("E-posta adresi zaten mevcut: {}", email);
            throw new GeneralException("E-posta adresi zaten mevcut.");
        }

        UserRepresentation user = new UserRepresentation();
        user.setEnabled(true);
        user.setUsername(email);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setEmailVerified(true);
        Response response = usersResource.create(user);
        String userId;

        if (response.getStatus() == 201) {
            userId = response.getLocation().getPath().replaceAll(".*/([^/]+)$", "$1");
            log.info("Kullanıcı başarıyla oluşturuldu (Status 201). Kullanıcı ID: {}", userId);
        } else {
            List<UserRepresentation> createdUsers = usersResource.searchByUsername(email, true);
            if (!createdUsers.isEmpty()) {
                userId = createdUsers.get(0).getId();
                log.warn("Kullanıcı oluşturuldu ancak beklenmeyen durum kodu alındı: {}. Kullanıcı ID: {}", response.getStatus(), userId);
            } else if (response.getStatus() == 409) {
                log.error("Kullanıcı adı veya e-posta zaten mevcut.");
                throw new GeneralException("Kullanıcı adı veya e-posta zaten mevcut.");
            } else {
                String errorMessage = response.readEntity(String.class); // Hata mesajını al
                log.error("Keycloak'ta kullanıcı oluşturulamadı. Status: {}, Mesaj: {}", response.getStatus(), errorMessage);
                throw new GeneralException("Keycloak'ta kullanıcı oluşturulamadı. Status: " + response.getStatus() + ", Mesaj: " + errorMessage);
            }
        }

        String finalPassword = password;
        if (password == null || password.isEmpty()) {
            finalPassword = "12345678";
            log.info("Kullanıcı için geçici şifre oluşturuldu: {}", userId);
        }

        setUserPassword(realmResource.users().get(userId), finalPassword);
        log.info("Keycloak'ta kullanıcı oluşturuldu ve şifre ayarlandı. Kullanıcı ID: {}", userId);
        return userId;
    }

    private void setUserPassword(UserResource userResource, String password) {
        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setTemporary(password == null || password.isEmpty());
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(password);
        userResource.resetPassword(credential);
    }

    private String generateTemporaryPassword() {
        // Geçici şifre oluşturma mantığı
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()";
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        // 12 karakter uzunluğunda rastgele şifre
        for (int i = 0; i < 12; i++) {
            int index = random.nextInt(chars.length());
            sb.append(chars.charAt(index));
        }
        return sb.toString();
    }


    private void assignGroupRoleToUser(String userId, String groupName) {
        RealmResource realmResource = this.keycloakClient.realm(realm);

        // Tüm grupları al
        List<GroupRepresentation> allGroups = realmResource.groups().groups();


        GroupRepresentation group = allGroups.stream()
                .filter(g -> groupName.equals(g.getName()))
                .findFirst()
                .orElse(null);

        if (group == null) {
            log.error(groupName + " grubu bulunamadı!");
            throw new GeneralException(groupName + " grubu bulunamadı");
        }

        // Grup ID'sini kullanarak direkt ekleme yap
        String groupId = group.getId();
        realmResource.users().get(userId).joinGroup(groupId);

        log.info("Kullanıcı '{}' ID'li " + groupName + " grubuna eklendi: {}", userId, groupId);
    }
}
