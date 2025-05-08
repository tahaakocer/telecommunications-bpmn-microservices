package com.tahaakocer.crm.controller;

import com.tahaakocer.commondto.request.GeneralOrderRequest;
import com.tahaakocer.commondto.response.GeneralResponse;
import com.tahaakocer.crm.dto.CustomerDto;
import com.tahaakocer.crm.dto.PartyRoleDto;
import com.tahaakocer.crm.service.CustomerService;
import com.tahaakocer.crm.service.PartyRoleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/crm")
public class CrmController {

    private final CustomerService customerService;
    private final PartyRoleService partyRoleService;

    public CrmController(CustomerService customerService, PartyRoleService partyRoleService) {
        this.customerService = customerService;
        this.partyRoleService = partyRoleService;
    }


    @PostMapping("/customer/create")
    public ResponseEntity<GeneralResponse<CustomerDto>> createCustomer(@RequestBody GeneralOrderRequest generalOrderRequest) {
        CustomerDto customerDto = this.customerService.createCustomer(generalOrderRequest.getOrderRequestId());
        return ResponseEntity.ok(GeneralResponse.<CustomerDto>builder()
                .code(200)
                .message("Customer created successfully")
                .data(customerDto)
                .build()
        );
    }

    @GetMapping("/party-role/{partyRoleId}")
    ResponseEntity<GeneralResponse<PartyRoleDto>> getPartyRole(@PathVariable String partyRoleId) {
        PartyRoleDto partyRoleDto = this.partyRoleService.getPartyRole(partyRoleId);
        return ResponseEntity.ok(GeneralResponse.<PartyRoleDto>builder()
                .code(200)
                .message("PartyRole retrieved successfully")
                .data(partyRoleDto)
                .build()
        );
    }


}
