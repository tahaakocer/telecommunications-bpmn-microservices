package com.tahaakocer.orderservice.client;


import com.tahaakocer.orderservice.dto.process.StartProcessResponse;
import com.tahaakocer.orderservice.dto.response.GeneralResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@FeignClient(name = "process-service", url = "${services.process-service.url}")
public interface ProcessServiceClient {


    @PostMapping("/api/starter-process/start/{orderType}/{channel}")
    ResponseEntity<GeneralResponse<StartProcessResponse>> startProcess(
            @PathVariable("orderType") String orderType,
            @PathVariable("channel") String channel,
            @RequestBody Map<String, Object> variables
    );

}