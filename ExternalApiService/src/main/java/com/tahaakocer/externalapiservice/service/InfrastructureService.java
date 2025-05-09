package com.tahaakocer.externalapiservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tahaakocer.externalapiservice.dto.bbk.InfrastructureDetailResponse;
import com.tahaakocer.externalapiservice.dto.infrastructure.MaxSpeedResponse;
import com.tahaakocer.externalapiservice.exception.GeneralException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class InfrastructureService {
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${services.netspeed.infrastructure-url}")
    private String infrastructureUrl;

    // kbps değerini mbps'ye çevirmek için sabiti tanımlıyoruz
    private static final int KBPS_TO_MBPS_DIVIDER = 1000;

    public InfrastructureService(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    public MaxSpeedResponse getMaxSpeed(String bbkCode) {
        InfrastructureDetailResponse infrastructureDetail = getInfrastructureDetail(bbkCode);

        if (infrastructureDetail == null || infrastructureDetail.getMaxSpeed() == null) {
            return new MaxSpeedResponse(null, null);
        }

        // MaxSpeed değeri kbps cinsinden geliyor
        Integer maxSpeedKbps = Integer.parseInt(infrastructureDetail.getMaxSpeed());
        Integer maxSpeedMbps = (maxSpeedKbps != null) ? Math.round(maxSpeedKbps / (float)KBPS_TO_MBPS_DIVIDER) : null;

        // ADSL ve VDSL port durumlarını kontrol et
        if (infrastructureDetail.getAdsl() != null &&
                "VAR".equals(infrastructureDetail.getAdsl().getPortState()) &&
                infrastructureDetail.getVdsl() != null &&
                "VAR".equals(infrastructureDetail.getVdsl().getPortState())) {

            // Hem ADSL hem VDSL varsa, VDSL genellikle daha hızlıdır
            return new MaxSpeedResponse(null, maxSpeedMbps);
        } else if (infrastructureDetail.getAdsl() != null &&
                "VAR".equals(infrastructureDetail.getAdsl().getPortState())) {
            return new MaxSpeedResponse(maxSpeedMbps, null);
        } else if (infrastructureDetail.getVdsl() != null &&
                "VAR".equals(infrastructureDetail.getVdsl().getPortState())) {
            return new MaxSpeedResponse(null, maxSpeedMbps);
        } else {
            return new MaxSpeedResponse(null, null);
        }
    }

    public InfrastructureDetailResponse getInfrastructureDetail(String bbkCode) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("searchKey", bbkCode);

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(formData, headers);

        log.info("Requesting infrastructure data for BBK: {}", bbkCode);

        ResponseEntity<String> response;
        try {
            response = restTemplate.postForEntity(infrastructureUrl, requestEntity, String.class);
            log.info("Response received with status: {}", response.getStatusCode());

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                // Response string JSON olarak parse edilmeli (iç tırnak işaretleri temizlenerek)
                String jsonString = response.getBody();
                if (jsonString.startsWith("\"") && jsonString.endsWith("\"")) {
                    // Dış tırnak işaretlerini kaldır ve escape karakterlerini temizle
                    jsonString = jsonString.substring(1, jsonString.length() - 1).replace("\\", "");
                }

                return objectMapper.readValue(jsonString, InfrastructureDetailResponse.class);
            } else {
                log.error("Infrastructure service returned unsuccessful response: {}", response.getStatusCode());
                throw new GeneralException("Altyapı servisi başarısız yanıt döndü: " + response.getStatusCode());
            }
        } catch (Exception e) {
            log.error("Netspeed altyapı servisinde hata oluştu: {}", e.getMessage(), e);
            throw new GeneralException("Netspeed altyapı servisinde hata oluştu: " + e.getMessage(), e);
        }
    }
}