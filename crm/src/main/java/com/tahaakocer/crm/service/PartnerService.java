package com.tahaakocer.crm.service;

import com.tahaakocer.crm.dto.*;
import com.tahaakocer.crm.exception.GeneralException;
import com.tahaakocer.crm.mapper.PartnerMapper;
import com.tahaakocer.crm.mapper.PartnerUserMapper;
import com.tahaakocer.crm.model.*;
import com.tahaakocer.crm.repository.PartnerRepository;
import com.tahaakocer.crm.utils.KeycloakUtil;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.GroupRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class PartnerService {
    private final PartnerRepository partnerRepository;
    private final PartnerMapper partnerMapper;
    private final Keycloak keycloak;
    private final KeycloakUtil keycloakUtil;
    private final IndividualService individualService;
    private final ContactMediumService contactMediumService;
    private final PartnerUserMapper partnerUserMapper;
    private final PartyRoleService partyRoleService;
    private final RestTemplate restTemplate;

    @Value("${keycloak.util.realm}")
    private String realm;

    @Value("${keycloak.util.client-id}")
    private String clientId;

    @Value("${keycloak.util.client-secret}")
    private String clientSecret;

    @Value("${keycloak.util.auth-server-url}")
    private String authServerUrl;

    public PartnerService(PartnerRepository partnerRepository, PartnerMapper partnerMapper,
                          Keycloak keycloak, KeycloakUtil keycloakUtil, IndividualService individualService, ContactMediumService contactMediumService, PartnerUserMapper partnerUserMapper, PartyRoleService partyRoleService, RestTemplate restTemplate) {
        this.partnerRepository = partnerRepository;
        this.partnerMapper = partnerMapper;
        this.keycloak = keycloak;
        this.keycloakUtil = keycloakUtil;
        this.individualService = individualService;
        this.contactMediumService = contactMediumService;
        this.partnerUserMapper = partnerUserMapper;
        this.partyRoleService = partyRoleService;
        this.restTemplate = restTemplate;
    }

    public PartnerDto getPartner(String partnerId) {
        Partner partner = this.partnerRepository.findById(UUID.fromString(partnerId))
                .orElseThrow(() -> new GeneralException("Partner not found"));
        return this.partnerMapper.entityToDto(partner);
    }
    @Transactional
    public PartnerDto registerPartner(PartnerRegisterRequest partnerRegisterRequest) {

        PartyRole partyRole = this.partyRoleService.createPartyRole("PARTNER");
        PartnerUser partnerUser = this.partnerUserMapper.partnerRegisterRequestToPartnerUserEntity(partnerRegisterRequest);

        Partner partner = new Partner();
        partner.setPartnerUser(partnerUser);
        partner.setHasCommunicationPermAppr(true);
        partner.setHasPersonalDataUsagePerm(true);


        // Create user in Keycloak
        String keycloakUserId = createKeycloakUser(
                keycloak,
                partnerRegisterRequest.getFirstName(),
                partnerRegisterRequest.getLastName(),
                partnerRegisterRequest.getEmail(),
                partnerRegisterRequest.getPassword()
        );

        partnerUser.setKeycloakUserId(keycloakUserId);
        partnerUser.setPartner(partner);
        partner.setPartnerUser(partnerUser);
        // Assign partner group to user
        assignGroupRoleToUser(keycloakUserId);
        Individual individual = this.individualService.createIndividualWithPartnerUser(partnerUser, partyRole);
        List<ContactMedium> contactMedia = this.contactMediumService.createContactMediumWithPartnerUser(partnerUser, partyRole);
        partyRole.setIndividual(individual);
        partyRole.setContactMedia(contactMedia);
        partyRole.setPartner(partner);
        partner.setPartyRole(partyRole);
        Partner savedPartner = this.partnerRepository.save(partner);

        return this.partnerMapper.entityToDto(savedPartner);

    }

    private String createKeycloakUser(Keycloak keycloak, String firstName, String lastName,
                                      String email, String password) {
        log.info("Creating Keycloak user with realm: {}, clientId: {}", realm, clientId);
        RealmResource realmResource = keycloak.realm(realm);
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

        setUserPassword(realmResource.users().get(userId), password);
        log.info("Keycloak'ta kullanıcı oluşturuldu ve şifre ayarlandı. Kullanıcı ID: {}", userId);
        return userId;
    }

    private void setUserPassword(UserResource userResource, String password) {
        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setTemporary(false);
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(password);
        userResource.resetPassword(credential);
    }

    private void assignGroupRoleToUser(String userId) {
        RealmResource realmResource = keycloak.realm(realm);

        // Tüm grupları al
        List<GroupRepresentation> allGroups = realmResource.groups().groups();

        // Partner grubunu bul
        GroupRepresentation partnerGroup = allGroups.stream()
                .filter(g -> "partner".equals(g.getName()))
                .findFirst()
                .orElse(null);

        if (partnerGroup == null) {
            log.error("Partner grubu bulunamadı!");
            throw new GeneralException("Partner grubu bulunamadı");
        }

        // Grup ID'sini kullanarak direkt ekleme yap
        String groupId = partnerGroup.getId();
        realmResource.users().get(userId).joinGroup(groupId);

        log.info("Kullanıcı '{}' ID'li partner grubuna eklendi: {}", userId, groupId);
    }

    public LoginResponse loginPartner(LoginRequest loginRequest) {
        try {
            log.info("Kullanıcı girişi deneniyor: {}", loginRequest.getUsername());

            // Kullanıcı doğrulama ve token almak için
            Keycloak userKeycloak = this.keycloakUtil.getKeycloakClientForUser(loginRequest.getUsername(),
                    loginRequest.getPassword());
            AccessTokenResponse tokenResponse = userKeycloak.tokenManager().getAccessToken();

            // Kullanıcının rollerini almak için
            RealmResource realmResource = keycloak.realm(realm);
            String userId = keycloak.realm(realm).users().searchByUsername(loginRequest.getUsername(), true)
                    .stream().findFirst()
                    .orElseThrow(() -> new GeneralException("Kullanıcı bulunamadı"))
                    .getId();

            // Kullanıcı rollerini al
            List<String> roles = realmResource.users().get(userId)
                    .roles().realmLevel().listAll().stream()
                    .map(RoleRepresentation::getName)
                    .toList();

            // Kullanıcı gruplarını al
            List<String> groups = realmResource.users().get(userId)
                    .groups().stream()
                    .map(GroupRepresentation::getName)
                    .toList();

            return LoginResponse.builder()
                    .accessToken(tokenResponse.getToken())
                    .refreshToken(tokenResponse.getRefreshToken())
                    .expiresIn(tokenResponse.getExpiresIn())
                    .refreshExpiresIn(tokenResponse.getRefreshExpiresIn())
                    .tokenType(tokenResponse.getTokenType())
                    .roles(roles)
                    .groups(groups)
                    .build();

        } catch (Exception e) {
            log.error("Giriş işlemi sırasında hata oluştu: {}", e.getMessage());
            throw new GeneralException("Kullanıcı adı veya şifre hatalı: " + e.getMessage());
        }
    }

    public LoginResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
        try {
            log.info("Refreshing token for realm: {}", realm);

            // Prepare the token endpoint URL
            String tokenEndpoint = String.format("%s/realms/%s/protocol/openid-connect/token",
                    authServerUrl, realm);

            // Prepare request headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            // Prepare request body
            MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
            body.add("grant_type", OAuth2Constants.REFRESH_TOKEN);
            body.add("client_id", clientId);
            body.add("client_secret", clientSecret);
            body.add("refresh_token", refreshTokenRequest.getRefreshToken());

            // Create HTTP entity
            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

            // Send request to Keycloak token endpoint
            AccessTokenResponse tokenResponse = restTemplate.postForObject(
                    tokenEndpoint,
                    request,
                    AccessTokenResponse.class
            );

            if (tokenResponse == null) {
                throw new GeneralException("Token yenileme başarısız: Yanıt alınamadı");
            }

            // Get user information to retrieve roles and groups
            RealmResource realmResource = keycloak.realm(realm);
            String userId = keycloak.realm(realm).users().searchByAttributes("email", true)
                    .stream()
                    .findFirst()
                    .orElseThrow(() -> new GeneralException("Kullanıcı bulunamadı"))
                    .getId();

            // Get user roles
            List<String> roles = realmResource.users().get(userId)
                    .roles().realmLevel().listAll().stream()
                    .map(RoleRepresentation::getName)
                    .toList();

            // Get user groups
            List<String> groups = realmResource.users().get(userId)
                    .groups().stream()
                    .map(GroupRepresentation::getName)
                    .toList();

            return LoginResponse.builder()
                    .accessToken(tokenResponse.getToken())
                    .refreshToken(tokenResponse.getRefreshToken())
                    .expiresIn(tokenResponse.getExpiresIn())
                    .refreshExpiresIn(tokenResponse.getRefreshExpiresIn())
                    .tokenType(tokenResponse.getTokenType())
                    .roles(roles)
                    .groups(groups)
                    .build();

        } catch (Exception e) {
            log.error("Token yenileme sırasında hata oluştu: {}", e.getMessage());
            throw new GeneralException("Token yenileme başarısız: " + e.getMessage());
        }
    }
}