package com.tahaakocer.orderservice.client;

import com.tahaakocer.orderservice.dto.response.GeneralResponse;
import com.tahaakocer.orderservice.dto.response.MaxSpeedResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "infrastructure-service", url = "${services.infrastructure-service.url}")
public interface InfrastructureServiceClient {

    @GetMapping("/max-speed-from-tt")
    ResponseEntity<GeneralResponse<MaxSpeedResponse>> maxSpeedFromTT(@RequestParam Integer bbk);
}
