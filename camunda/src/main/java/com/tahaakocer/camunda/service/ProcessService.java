package com.tahaakocer.camunda.service;

import com.tahaakocer.camunda.config.ProcessMappingConfig;
import com.tahaakocer.camunda.exception.GeneralException;
import com.tahaakocer.camunda.exception.StartProcessException;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.AccessTokenResponse;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ProcessService {
    Logger logger = org.slf4j.LoggerFactory.getLogger(ProcessService.class);
    private final RuntimeService runtimeService;
    private final Keycloak keycloakClient;
    private final ProcessMappingConfig processMapping;

    public ProcessService(RuntimeService runtimeService,
                          Keycloak keycloakClient, ProcessMappingConfig processMapping) {
        this.runtimeService = runtimeService;
        this.keycloakClient = keycloakClient;
        this.processMapping = processMapping;
    }

    public ProcessInstance startProcess(String orderType, String channel, Map<String, Object> variables) {
        try {

            String token = obtainAccessToken();

            String processKey = determineProcessKey(orderType, channel);
            if (processKey == null) {
                throw new StartProcessException("Belirtilen orderType ve channel için uygun process bulunamadı: " + orderType + ", " + channel);
            }

            Map<String, Object> processVariables = new HashMap<>(variables);
            processVariables.put("orderType", orderType);
            processVariables.put("channel", channel);

            String businessKey = generateBusinessKey(orderType, channel);
            ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(
                    processKey,
                    businessKey,
                    processVariables
            );

            logger.info("Process başlatıldı. ProcessInstanceId: {}, BusinessKey: {}, OrderType: {}, Channel: {}",
                    processInstance.getId(), businessKey, orderType, channel);

            return processInstance;
        } catch (Exception e) {
            logger.error("Process başlatılırken hata oluştu. OrderType: {}, Channel: {}", orderType, channel, e);
            throw new StartProcessException("Process başlatılırken hata oluştu: " + e.getMessage(), e);
        }
    }

    private String obtainAccessToken() {
        try {
            AccessTokenResponse tokenResponse = this.keycloakClient.tokenManager().getAccessToken();
            String accessToken = tokenResponse.getToken();

            logger.info("Yeni access token başarıyla alındı.");
            return accessToken;
        } catch (Exception e) {
            logger.error("Keycloak'tan token alınırken hata oluştu: {}", e.getMessage());
            throw new GeneralException("Yeni access token alınamadı.", e);
        }
    }


    private String determineProcessKey(String orderType, String channel) {
        Map<String, String> channelMap = processMapping.getMapping().get(orderType.toUpperCase());
        if (channelMap == null) {
            logger.warn("Konfigürasyonda orderType '{}' için mapping bulunamadı.", orderType);
            return null;
        }

        String processKey = channelMap.get(channel.toUpperCase());
        if (processKey == null) {
            logger.warn("Konfigürasyonda orderType '{}' ve channel '{}' için mapping bulunamadı.", orderType, channel);
        }

        return processKey;
    }

    //gpt verdi. ne ise yaradıgını sonra arastir
    private String generateBusinessKey(String orderType, String channel) {
        return String.format("%s-%s-%d",
                orderType.toUpperCase(),
                channel.toUpperCase(),
                System.currentTimeMillis());
    }
}