package com.tahaakocer.externalapiservice.service;

import com.tahaakocer.externalapiservice.dto.infrastructure.MaxSpeedResponse;
import com.tahaakocer.externalapiservice.dto.infrastructure.TTInfrastructureDetailDto;
import com.tahaakocer.externalapiservice.exception.GeneralException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Objects;

@Service
@Slf4j
public class InfrastructureService {
    private final RestTemplate restTemplate;

    @Value("${services.ttaddress-service.url}")
    private String ttUrl;

    // kbps değerini mbps'ye çevirmek için sabiti tanımlıyoruz
    private static final int KBPS_TO_MBPS_DIVIDER = 1000;

    public InfrastructureService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public MaxSpeedResponse maxSpeedFromTT(Integer bbk) {
        TTInfrastructureDetailDto ttInfrastructureDetailDto = getTTInfrastructureDetail(bbk);
        Integer adslKbps = ttInfrastructureDetailDto.getAdslMesafeBazliHiz();
        Integer vdslKbps = ttInfrastructureDetailDto.getVdslMesafeBazliHiz();
        Integer adslMbps = (adslKbps != null) ? Math.round(adslKbps / (float)KBPS_TO_MBPS_DIVIDER) : null;
        Integer vdslMbps = (vdslKbps != null) ? Math.round(vdslKbps / (float)KBPS_TO_MBPS_DIVIDER) : null;
        log.info("BBK: {}, ADSL hızı: {} kbps ({} mbps), VDSL hızı: {} kbps ({} mbps)",
                bbk, adslKbps, adslMbps, vdslKbps, vdslMbps);
        if (adslMbps != null && vdslMbps != null) {
            if (adslMbps > vdslMbps) {
                return new MaxSpeedResponse(adslMbps, null);
            } else {
                return new MaxSpeedResponse(null, vdslMbps);
            }
        } else if (adslMbps != null) {
            return new MaxSpeedResponse(adslMbps, null);
        } else if (vdslMbps != null) {
            return new MaxSpeedResponse(null, vdslMbps);
        } else {
            return new MaxSpeedResponse(null, null);
        }
    }

    public TTInfrastructureDetailDto getTTInfrastructureDetail(Integer bbk) {
        ResponseEntity<TTInfrastructureDetailDto> response;
        String requestUrl = ttUrl + "?kod=" + bbk + "&datatype=checkAddress";
        try {
            response = restTemplate.getForEntity(requestUrl, TTInfrastructureDetailDto.class);
            log.info("Response received with status: {}", response.getStatusCode());
            return Objects.requireNonNull(response.getBody());
        }
        catch (Exception e) {
            log.error("BbkService - order get feign servisinde hata olustu: {}", e.getMessage(), e);
            throw new GeneralException("order get feign servisinde hata olustu: " + e.getMessage(), e);
        }
    }
}