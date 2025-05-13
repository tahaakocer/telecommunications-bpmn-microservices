package com.tahaakocer.orderservice.client;


import com.tahaakocer.orderservice.dto.process.StartProcessResponse;
import com.tahaakocer.orderservice.dto.response.GeneralResponse;
import com.tahaakocer.orderservice.dto.response.MaxSpeedResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@FeignClient(name = "camunda", url = "${services.process-service.url}")
public interface ProcessServiceClient {


    @PostMapping("/start/{orderType}/{channel}")
    ResponseEntity<GeneralResponse<StartProcessResponse>> startProcess(
            @PathVariable("orderType") String orderType,
            @PathVariable("channel") String channel,
            @RequestBody Map<String, Object> variables
    );

}