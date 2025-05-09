package com.tahaakocer.externalapiservice.controller;

import com.tahaakocer.externalapiservice.dto.GeneralResponse;
import com.tahaakocer.externalapiservice.dto.bbk.InfrastructureDetailResponse;
import com.tahaakocer.externalapiservice.dto.infrastructure.MaxSpeedResponse;
import com.tahaakocer.externalapiservice.dto.infrastructure.TTInfrastructureDetailDto;
import com.tahaakocer.externalapiservice.service.InfrastructureService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/infrastructure-service")
public class InfrastructureController {
    private final InfrastructureService infrastructureService;

    public InfrastructureController(InfrastructureService infrastructureService) {
        this.infrastructureService = infrastructureService;
    }

    @GetMapping("/get-tt-infrastructure-detail")
    public ResponseEntity<GeneralResponse<InfrastructureDetailResponse>> getTTInfrastructureDetail(@RequestParam Integer bbk) {
        return ResponseEntity.ok(GeneralResponse.<InfrastructureDetailResponse>builder()
                .code(200)
                .message("TT infrastructure detail")
                .data(infrastructureService.getInfrastructureDetail(String.valueOf(bbk)))
                .build());
    }
    @GetMapping("/max-speed-from-tt")
    public ResponseEntity<GeneralResponse<MaxSpeedResponse>> maxSpeedFromTT(@RequestParam Integer bbk) {
        return ResponseEntity.ok(GeneralResponse.<MaxSpeedResponse>builder()
                .code(200)
                .message("Max speed from TT")
                .data(infrastructureService.getMaxSpeed(String.valueOf(bbk)))
                .build());
    }
}
