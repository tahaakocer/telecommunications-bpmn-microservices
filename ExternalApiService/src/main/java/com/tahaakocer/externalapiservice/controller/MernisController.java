package com.tahaakocer.externalapiservice.controller;

import com.tahaakocer.commondto.request.GeneralOrderRequest;
import com.tahaakocer.externalapiservice.dto.GeneralResponse;
import com.tahaakocer.externalapiservice.dto.mernis.ValidMernisResponse;
import com.tahaakocer.externalapiservice.service.MernisService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/mernis")
public class MernisController {

    private final MernisService mernisService;

    public MernisController(MernisService mernisService) {
        this.mernisService = mernisService;
    }

    @PostMapping("/valid")
    public ResponseEntity<GeneralResponse<ValidMernisResponse>> mernis(@RequestBody GeneralOrderRequest orderRequest) {
        UUID orderRequestId = UUID.fromString(orderRequest.getOrderRequestId());
        return ResponseEntity.ok(GeneralResponse.<ValidMernisResponse>builder()
                        .code(200)
                        .data(mernisService.checkIfRealPerson(orderRequestId))
                        .message("Mernis checked successfully")
                .build());
    }
}
