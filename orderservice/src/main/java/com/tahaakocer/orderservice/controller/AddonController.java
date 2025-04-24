package com.tahaakocer.orderservice.controller;

import com.tahaakocer.orderservice.dto.AddonDto;
import com.tahaakocer.orderservice.dto.response.GeneralResponse;
import com.tahaakocer.orderservice.service.impl.AddonServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequestMapping("/api/addon")
@RestController
public class AddonController {
    private final AddonServiceImpl addonServiceImpl;

    public AddonController(AddonServiceImpl addonServiceImpl) {
        this.addonServiceImpl = addonServiceImpl;
    }


    @PostMapping("/create")
    public ResponseEntity<GeneralResponse<AddonDto>> createAddon(@RequestBody AddonDto addonDto) {
        return ResponseEntity.ok(GeneralResponse.<AddonDto>builder()
                .code(200)
                .message("Addon created successfully")
                .data(addonServiceImpl.createAddon(addonDto))
                .build()
        );
    }
    @GetMapping("/get-all")
    public ResponseEntity<GeneralResponse<List<AddonDto>>> getAllAddons() {
        return ResponseEntity.ok(GeneralResponse.<List<AddonDto>>builder()
                .code(200)
                .message("Addons found successfully")
                .data(addonServiceImpl.getAllAddons())
                .build()
        );
    }
    @GetMapping("/get-by-main-product-id")
    public ResponseEntity<GeneralResponse<List<AddonDto>>> getAddonByMainProductId(@RequestParam UUID mainProductId) {
        return ResponseEntity.ok(GeneralResponse.<List<AddonDto>>builder()
                .code(200)
                .message("Addons found successfully")
                .data(addonServiceImpl.getAddonByMainProductId(mainProductId))
                .build()
        );
    }
    @DeleteMapping("/delete-by-id")
    public ResponseEntity<GeneralResponse<Void>> deleteAddonById(@RequestParam String id) {
        addonServiceImpl.deleteAddonById(UUID.fromString(id));
        return ResponseEntity.ok(GeneralResponse.<Void>builder()
                .code(200)
                .message("Addon deleted successfully")
                .data(null)
                .build()
        );
    }
}
