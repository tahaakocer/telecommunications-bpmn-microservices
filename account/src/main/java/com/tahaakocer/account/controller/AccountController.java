package com.tahaakocer.account.controller;

import com.tahaakocer.account.service.AccountService;
import com.tahaakocer.commondto.crm.AccountDto;
import com.tahaakocer.commondto.request.GeneralOrderRequest;
import com.tahaakocer.commondto.response.GeneralResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/crm/account")
public class AccountController {
    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/create")
    public ResponseEntity<GeneralResponse<AccountDto>> createAccount(
            @RequestBody GeneralOrderRequest generalOrderRequest) {
        AccountDto accountDto = this.accountService.createAccount(generalOrderRequest.getOrderRequestId());
        return ResponseEntity.ok(GeneralResponse.<AccountDto>builder()
                .code(200)
                .message("Account created successfully")
                .data(accountDto)
                .build()
        );
    }

    @GetMapping("/get")
    public ResponseEntity<GeneralResponse<AccountDto>> getAccount(@RequestParam String id) {
        AccountDto accountDto = this.accountService.getAccount(id);
        return ResponseEntity.ok(GeneralResponse.<AccountDto>builder()
                .code(200)
                .message("Account found successfully")
                .data(accountDto)
                .build()
        );
    }
    @GetMapping("/get-by-order-id")
    public ResponseEntity<GeneralResponse<List<AccountDto>>> getAccountByOrderId(@RequestParam String orderId) {
        List<AccountDto> accountDtos = this.accountService.getAccountByOrderRequestId(orderId);
        return ResponseEntity.ok(GeneralResponse.<List<AccountDto>>builder()
                .code(200)
                .message("Account found successfully")
                .data(accountDtos)
                .build()
        );
    }
}
