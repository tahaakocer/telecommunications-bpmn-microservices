package com.tahaakocer.orderservice.controller;

import com.tahaakocer.commondto.order.SpecificationDto;
import com.tahaakocer.orderservice.dto.request.SpecificationCreateRequest;
import com.tahaakocer.orderservice.dto.response.GeneralResponse;
import com.tahaakocer.orderservice.service.SpecificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/specifications")
public class SpecificationController {
    private final SpecificationService specificationService;


    public SpecificationController(SpecificationService specificationService) {
        this.specificationService = specificationService;

    }

    @PostMapping("/create")
    public ResponseEntity<GeneralResponse<SpecificationDto>> createSpecification(@RequestBody SpecificationCreateRequest request) {
        SpecificationDto specificationDto = specificationService.createSpecification(request);
        return ResponseEntity.ok(GeneralResponse.<SpecificationDto>builder()
                .code(200)
                .message("Specification created successfully")
                .data(specificationDto)
                .build()
        );
    }

    @PostMapping("/create-batch")
    public ResponseEntity<GeneralResponse<List<SpecificationDto>>> createSpecification(@RequestBody List<SpecificationCreateRequest> requests) {
        List<SpecificationDto> specificationDtos = specificationService.createSpecificationBatch(requests);
        return ResponseEntity.ok(GeneralResponse.<List<SpecificationDto>>builder()
                .code(200)
                .message("Specification created successfully")
                .data(specificationDtos)
                .build()
        );
    }
    @GetMapping("/search")
    public ResponseEntity<GeneralResponse<List<SpecificationDto>>> searchSpecifications(
            @RequestParam String query
    ) {
        return ResponseEntity.ok(GeneralResponse.<List<SpecificationDto>>builder()
                .code(200)
                .message("Specifications searched successfully")
                .data(specificationService.searchSpecification(query))
                .build()
        );
    }
    @GetMapping("/get-all")
    public ResponseEntity<GeneralResponse<List<SpecificationDto>>> getAllSpecifications() {
        return ResponseEntity.ok(GeneralResponse.<List<SpecificationDto>>builder()
                .code(200)
                .message("Specifications found successfully")
                .data(specificationService.getAllSpecifications())
                .build()
        );
    }
    @GetMapping("/get-by-id")
    public ResponseEntity<GeneralResponse<SpecificationDto>> getSpecificationById(@RequestParam String id) {
        return ResponseEntity.ok(GeneralResponse.<SpecificationDto>builder()
                .code(200)
                .message("Specification found successfully")
                .data(specificationService.getSpecificationById(UUID.fromString(id)))
                .build()
        );
    }
    @GetMapping("/get-by-code")
    public ResponseEntity<GeneralResponse<SpecificationDto>> getSpecificationByCode(@RequestParam String code) {
        return ResponseEntity.ok(GeneralResponse.<SpecificationDto>builder()
                .code(200)
                .message("Specification found successfully")
                .data(specificationService.getSpecificationByCode(code))
                .build()
        );
    }
    @DeleteMapping("/delete-by-id")
    public ResponseEntity<GeneralResponse<Void>> deleteSpecificationById(@RequestParam String id) {
        specificationService.deleteSpecificationById(UUID.fromString(id));
        return ResponseEntity.ok(GeneralResponse.<Void>builder()
                .code(200)
                .message("Specification deleted successfully")
                .data(null)
                .build()
        );
    }
}
