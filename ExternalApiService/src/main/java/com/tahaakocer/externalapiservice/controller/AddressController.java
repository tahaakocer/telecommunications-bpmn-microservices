package com.tahaakocer.externalapiservice.controller;

import com.tahaakocer.commondto.request.GeneralOrderRequest;
import com.tahaakocer.commondto.response.OrderRequestResponse;
import com.tahaakocer.externalapiservice.dto.GeneralResponse;
import com.tahaakocer.externalapiservice.dto.infrastructure.MaxSpeedResponse;
import com.tahaakocer.externalapiservice.service.BbkService;
import com.tahaakocer.externalapiservice.service.InfrastructureService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/bbk-service")
public class AddressController {
    private final BbkService bbkService;
    private final InfrastructureService infrastructureService;

    public AddressController(BbkService bbkService, InfrastructureService infrastructureService) {
        this.bbkService = bbkService;
        this.infrastructureService = infrastructureService;
    }

    @PostMapping("/update-address")
    public ResponseEntity<GeneralResponse<OrderRequestResponse>> updateAddress(
            @RequestBody GeneralOrderRequest orderRequest
    ) {
        OrderRequestResponse orderRequestResponse = this.bbkService.updateAddress(UUID.fromString(orderRequest.getOrderRequestId()));
        return ResponseEntity.ok(GeneralResponse.<OrderRequestResponse>builder()
                .code(200)
                .message("Address updated successfully")
                .data(orderRequestResponse).build()
        );
    }

    @GetMapping("/districts")
    public ResponseEntity<?> getDistricts(@RequestParam String cityCode) {
        try {
            return ResponseEntity.ok(bbkService.getAddressData(1, cityCode));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Veri getirilemedi: " + e.getMessage());
        }
    }

    @GetMapping("/townships")
    public ResponseEntity<?> getTownships(@RequestParam String districtCode) {
        try {
            return ResponseEntity.ok(bbkService.getAddressData(2, districtCode));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Veri getirilemedi: " + e.getMessage());
        }
    }

    @GetMapping("/neighborhoods")
    public ResponseEntity<?> getNeighborhoods(@RequestParam String townshipCode) {
        try {
            return ResponseEntity.ok(bbkService.getAddressData(3, townshipCode));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Veri getirilemedi: " + e.getMessage());
        }
    }

    @GetMapping("/streets")
    public ResponseEntity<?> getStreets(@RequestParam String neighborhoodCode) {
        try {
            return ResponseEntity.ok(bbkService.getAddressData(4, neighborhoodCode));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Veri getirilemedi: " + e.getMessage());
        }
    }

    @GetMapping("/buildings")
    public ResponseEntity<?> getBuildings(@RequestParam String streetCode) {
        try {
            return ResponseEntity.ok(bbkService.getAddressData(5, streetCode));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Veri getirilemedi: " + e.getMessage());
        }
    }

    @GetMapping("/bbk")
    public ResponseEntity<?> getBbk(@RequestParam String buildingCode) {
        try {
            return ResponseEntity.ok(bbkService.getAddressData(6, buildingCode));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Veri getirilemedi: " + e.getMessage());
        }
    }

    @GetMapping("/infrastructure")
    public ResponseEntity<?> getInfrastructure(@RequestParam String bbkCode) {
        try {
            return ResponseEntity.ok(infrastructureService.getInfrastructureDetail(bbkCode));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Altyapı verisi getirilemedi: " + e.getMessage());
        }
    }

    @GetMapping("/max-speed")
    public ResponseEntity<MaxSpeedResponse> getMaxSpeed(@RequestParam String bbkCode) {
        try {
            MaxSpeedResponse response = infrastructureService.getMaxSpeed(bbkCode);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}