package com.tahaakocer.orderservice.controller;

import com.tahaakocer.commondto.order.AccountDto;
import com.tahaakocer.orderservice.dto.response.GeneralResponse;
import com.tahaakocer.orderservice.service.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/account")
public class AccountController {
    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/create")
    public ResponseEntity<GeneralResponse<AccountDto>> createAccount(AccountDto accountDto) {
        return ResponseEntity.ok(GeneralResponse.<AccountDto>builder()
                .code(HttpStatus.CREATED.value())
                .message("Account created successfully")
                .data(accountService.createAccount(accountDto)).build()
        );
    }
    @PutMapping("/update")
    public ResponseEntity<GeneralResponse<AccountDto>> updateAccount(AccountDto accountDto) {
        return ResponseEntity.ok(GeneralResponse.<AccountDto>builder()
                .code(200)
                .message("Account updated successfully")
                .data(accountService.updateAccount(accountDto))
                .build()
        );
    }
    @GetMapping("/get")
    public ResponseEntity<GeneralResponse<AccountDto>> getAccount(
            @RequestParam(required = false) UUID id,
            @RequestParam(required = false) String accountCode,
            @RequestParam(required = false) UUID orderRequestId
            ) {
        return ResponseEntity.ok(GeneralResponse.<AccountDto>builder()
                .code(200)
                .message("Account found successfully")
                .data(this.accountService.get(id, accountCode, orderRequestId)).build()
        );
    }
    @DeleteMapping("/delete")
    public ResponseEntity<GeneralResponse<Void>> deleteAccount(@RequestParam UUID id) {
        this.accountService.deleteAccount(id);
        return ResponseEntity.ok(GeneralResponse.<Void>builder()
                .code(200)
                .message("Account deleted successfully")
                .data(null)
                .build()
        );
    }
}

