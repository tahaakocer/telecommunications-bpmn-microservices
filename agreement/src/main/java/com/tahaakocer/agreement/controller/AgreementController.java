package com.tahaakocer.agreement.controller;

import com.tahaakocer.agreement.service.AgreementService;
import com.tahaakocer.commondto.agreement.AgreementDto;
import com.tahaakocer.commondto.request.GeneralOrderRequest;
import com.tahaakocer.commondto.response.GeneralResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/agreement")
public class AgreementController {
    private final AgreementService agreementService;

    public AgreementController(AgreementService agreementService) {
        this.agreementService = agreementService;
    }

    @PostMapping("/create")
    public ResponseEntity<GeneralResponse<AgreementDto>> createAgreement(
            @RequestBody  GeneralOrderRequest generalOrderRequest
    ) {
        AgreementDto agreementDto = this.agreementService.createAgreement(generalOrderRequest.getOrderRequestId());
        return ResponseEntity.ok(GeneralResponse.<AgreementDto>builder()
                .code(200)
                .message("Agreement created successfully")
                .data(agreementDto)
                .build()
        );
    }
    @GetMapping("/get")
    public ResponseEntity<GeneralResponse<AgreementDto>> getAgreement(
            @RequestParam("agreementId") String agreementId
    ) {
        AgreementDto agreementDto = this.agreementService.getAgreement(agreementId);
        return ResponseEntity.ok(GeneralResponse.<AgreementDto>builder()
                .code(200)
                .message("Agreement retrieved successfully")
                .data(agreementDto)
                .build()
        );
    }
    @PostMapping("/get-by-order-id")
    public ResponseEntity<GeneralResponse<AgreementDto>> getAgreementByOrderId(
            @RequestBody GeneralOrderRequest generalOrderRequest
    ) {
        AgreementDto agreementDto = this.agreementService.getAgreementByOrderId(generalOrderRequest.getOrderRequestId());
        return ResponseEntity.ok(GeneralResponse.<AgreementDto>builder()
                .code(200)
                .message("Agreement retrieved successfully")
                .data(agreementDto)
                .build()
        );
    }
}
