package com.tahaakocer.account.controller;

import com.tahaakocer.account.service.AccountService;
import com.tahaakocer.commondto.crm.AccountDto;
import com.tahaakocer.commondto.request.GeneralOrderRequest;
import com.tahaakocer.commondto.response.GeneralResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
