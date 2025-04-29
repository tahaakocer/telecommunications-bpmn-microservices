package com.tahaakocer.externalapiservice.controller;

import com.tahaakocer.commondto.request.GeneralOrderRequest;
import com.tahaakocer.commondto.response.OrderRequestResponse;
import com.tahaakocer.externalapiservice.dto.GeneralResponse;
import com.tahaakocer.externalapiservice.service.BbkService;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@RestController
@RequestMapping("/api/bbk-service")
public class AddressController {
    private final BbkService bbkService;
    private final RestTemplate restTemplate;

    public AddressController(BbkService bbkService, RestTemplate restTemplate) {
        this.bbkService = bbkService;
        this.restTemplate = new RestTemplateBuilder()
                .messageConverters(new StringHttpMessageConverter(), new MappingJackson2HttpMessageConverter())
                .build();
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
            String url = "https://user.goknet.com.tr/sistem/getTTAddressWebservice.php?city=" + cityCode + "&datatype=city";
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            return ResponseEntity.ok(response.getBody());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Veri getirilemedi: " + e.getMessage());
        }
    }

    @GetMapping("/townships")
    public ResponseEntity<?> getTownships(@RequestParam String districtCode) {
        try {
            String url = "https://user.goknet.com.tr/sistem/getTTAddressWebservice.php?district=" + districtCode + "&datatype=district";
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            return ResponseEntity.ok(response.getBody());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Veri getirilemedi: " + e.getMessage());
        }
    }

    @GetMapping("/villages")
    public ResponseEntity<?> getVillages(@RequestParam String townshipCode) {
        try {
            String url = "https://user.goknet.com.tr/sistem/getTTAddressWebservice.php?township=" + townshipCode + "&datatype=township";
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            return ResponseEntity.ok(response.getBody());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Veri getirilemedi: " + e.getMessage());
        }
    }

    @GetMapping("/neighborhoods")
    public ResponseEntity<?> getNeighborhoods(@RequestParam String villageCode) {
        try {
            String url = "https://user.goknet.com.tr/sistem/getTTAddressWebservice.php?village=" + villageCode + "&datatype=village";
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            return ResponseEntity.ok(response.getBody());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Veri getirilemedi: " + e.getMessage());
        }
    }

    @GetMapping("/streets")
    public ResponseEntity<?> getStreets(@RequestParam String neighborhoodCode) {
        try {
            String url = "https://user.goknet.com.tr/sistem/getTTAddressWebservice.php?neighborhood=" + neighborhoodCode + "&datatype=neighborhood";
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            return ResponseEntity.ok(response.getBody());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Veri getirilemedi: " + e.getMessage());
        }
    }

    @GetMapping("/buildings")
    public ResponseEntity<?> getBuildings(@RequestParam String streetCode) {
        try {
            String url = "https://user.goknet.com.tr/sistem/getTTAddressWebservice.php?street=" + streetCode + "&datatype=street";
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            return ResponseEntity.ok(response.getBody());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Veri getirilemedi: " + e.getMessage());
        }
    }

    @GetMapping("/apartments")
    public ResponseEntity<?> getApartments(@RequestParam String buildingCode) {
        try {
            String url = "https://user.goknet.com.tr/sistem/getTTAddressWebservice.php?building=" + buildingCode + "&datatype=building";
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            return ResponseEntity.ok(response.getBody());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Veri getirilemedi: " + e.getMessage());
        }
    }
}

