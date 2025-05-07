package com.tahaakocer.crm.controller;

import com.tahaakocer.commondto.response.GeneralResponse;
import com.tahaakocer.crm.dto.*;
import com.tahaakocer.crm.mapper.PartnerMapper;
import com.tahaakocer.crm.service.PartnerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/crm/partner")
public class PartnerController {
    private final PartnerService partnerService;
    private final PartnerMapper partnerMapper;

    public PartnerController(PartnerService partnerService, PartnerMapper partnerMapper) {
        this.partnerService = partnerService;
        this.partnerMapper = partnerMapper;
    }

    @PostMapping("/auth/register")
    public ResponseEntity<GeneralResponse<PartnerRegisterResponse>> registerUserMongo(
            @RequestBody PartnerRegisterRequest partnerRegisterRequest) {
        return ResponseEntity.ok(GeneralResponse.<PartnerRegisterResponse>builder()
                .code(200)
                .message("Partner başarıyla kaydedildi.")
                .data(partnerService.registerPartner(this.partnerMapper.requestToDto(partnerRegisterRequest)))
                .build()
        );

    }
    @PostMapping("/auth/login")
    public ResponseEntity<GeneralResponse<LoginResponse>> login(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(GeneralResponse.<LoginResponse>builder()
                .code(200)
                .message("Giriş başarılı.")
                .data(partnerService.loginPartner(loginRequest))
                .build());
    }
    @PostMapping("/auth/refresh")
    public ResponseEntity<GeneralResponse<LoginResponse>> refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        return ResponseEntity.ok(GeneralResponse.<LoginResponse>builder()
                .code(200)
                .message("Token yenilendi.")
                .data(partnerService.refreshToken(refreshTokenRequest))
                .build());
    }
}
