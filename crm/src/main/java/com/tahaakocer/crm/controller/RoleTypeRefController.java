package com.tahaakocer.crm.controller;

import com.tahaakocer.commondto.response.GeneralResponse;
import com.tahaakocer.crm.dto.RoleTypeRefDto;
import com.tahaakocer.crm.service.RoleTypeRefService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/crm/role-type-ref")
public class RoleTypeRefController {
    private final RoleTypeRefService roleTypeRefService;

    public RoleTypeRefController(RoleTypeRefService roleTypeRefService) {
        this.roleTypeRefService = roleTypeRefService;
    }

    @PostMapping("/create")
    public ResponseEntity<GeneralResponse<RoleTypeRefDto>> create(@RequestParam String name) {
        RoleTypeRefDto roleTypeRefDto = this.roleTypeRefService.createRoleTypeRef(name);
        return ResponseEntity.ok(GeneralResponse.<RoleTypeRefDto>builder()
                .code(200)
                .message("RoleTypeRef created successfully")
                .data(roleTypeRefDto)
                .build()
        );
    }
    @GetMapping("/get-all")
    public ResponseEntity<GeneralResponse<List<RoleTypeRefDto>>> getAll() {
        List<RoleTypeRefDto> roleTypeRefDtos = this.roleTypeRefService.getAllRoleTypeRefs();
        return ResponseEntity.ok(GeneralResponse.<List<RoleTypeRefDto>>builder()
                .code(200)
                .message("RoleTypeRefs retrieved successfully")
                .data(roleTypeRefDtos)
                .build()
        );

    }
}
