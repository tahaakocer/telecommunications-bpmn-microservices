package com.tahaakocer.crm.controller;

import com.tahaakocer.commondto.crm.AccountDto;
import com.tahaakocer.commondto.crm.AccountRefDto;
import com.tahaakocer.commondto.crm.CustomerDto;
import com.tahaakocer.commondto.crm.PartyRoleDto;
import com.tahaakocer.commondto.request.GeneralOrderRequest;
import com.tahaakocer.commondto.response.GeneralResponse;
import com.tahaakocer.crm.service.AccountRefService;
import com.tahaakocer.crm.service.CustomerService;
import com.tahaakocer.crm.service.PartyRoleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/crm")
public class CrmController {

    private final CustomerService customerService;
    private final PartyRoleService partyRoleService;
    private final AccountRefService accountRefService;

    public CrmController(CustomerService customerService,
                         PartyRoleService partyRoleService, AccountRefService accountRefService) {
        this.customerService = customerService;
        this.partyRoleService = partyRoleService;
        this.accountRefService = accountRefService;
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
    @PostMapping("/get-party-role-by-order-request-id")
    ResponseEntity<GeneralResponse<PartyRoleDto>> getPartyRoleByOrderRequestId(@RequestBody GeneralOrderRequest generalOrderRequest) {
        PartyRoleDto partyRoleDto = this.partyRoleService.getPartyRoleByOrderRequestId(generalOrderRequest.getOrderRequestId());
        return ResponseEntity.ok(GeneralResponse.<PartyRoleDto>builder()
                .code(200)
                .message("PartyRole retrieved successfully")
                .data(partyRoleDto)
                .build()
        );
    }
    @PostMapping("/customer/create-keycloak-user")
    public ResponseEntity<GeneralResponse<CustomerDto>> createKeycloakUser(@RequestBody GeneralOrderRequest generalOrderRequest) {
        CustomerDto customer = this.customerService.createKeycloakUserForCustomer(generalOrderRequest.getOrderRequestId());
        return ResponseEntity.ok(GeneralResponse.<CustomerDto>builder()
                .code(200)
                .message("Keycloak user created successfully")
                .data(customer)
                .build()
        );
    }
    @PostMapping("/party-role/create-account-ref")
    public ResponseEntity<GeneralResponse<AccountRefDto>> createAccountRef(@RequestBody GeneralOrderRequest generalOrderRequest) {
        AccountRefDto accountRefDto = this.accountRefService.createAccountRef(generalOrderRequest.getOrderRequestId());
        return ResponseEntity.ok(GeneralResponse.<AccountRefDto>builder()
                .code(200)
                .message("AccountRef created successfully")
                .data(accountRefDto)
                .build()
        );
    }



}
