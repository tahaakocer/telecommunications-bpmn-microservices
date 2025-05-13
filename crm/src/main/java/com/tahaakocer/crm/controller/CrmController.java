package com.tahaakocer.crm.controller;

import com.tahaakocer.commondto.request.GeneralOrderRequest;
import com.tahaakocer.commondto.response.GeneralResponse;
import com.tahaakocer.crm.dto.AccountDto;
import com.tahaakocer.crm.dto.CustomerDto;
import com.tahaakocer.crm.dto.PartyRoleDto;
import com.tahaakocer.crm.service.AccountService;
import com.tahaakocer.crm.service.CustomerService;
import com.tahaakocer.crm.service.PartyRoleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/crm")
public class CrmController {

    private final CustomerService customerService;
    private final PartyRoleService partyRoleService;
    private final AccountService accountService;

    public CrmController(CustomerService customerService,
                         PartyRoleService partyRoleService,
                         AccountService accountService) {
        this.customerService = customerService;
        this.partyRoleService = partyRoleService;
        this.accountService = accountService;
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
    @GetMapping("/get-party-role-by-order-request-id")
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
    @PostMapping("/account/create")
    public ResponseEntity<GeneralResponse<AccountDto>> createAccount(@RequestBody GeneralOrderRequest generalOrderRequest) {
        AccountDto accountDto = this.accountService.createAccount(
                generalOrderRequest.getOrderRequestId(),
                generalOrderRequest.getUpdate().getName());
        return ResponseEntity.ok(GeneralResponse.<AccountDto>builder()
                .code(200)
                .message("Account created successfully")
                .data(accountDto)
                .build()
        );
    }


}
