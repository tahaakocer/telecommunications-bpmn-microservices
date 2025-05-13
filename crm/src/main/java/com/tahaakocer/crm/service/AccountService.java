package com.tahaakocer.crm.service;

import com.tahaakocer.commondto.order.OrderRequestDto;
import com.tahaakocer.commondto.response.GeneralResponse;
import com.tahaakocer.crm.client.OrderRequestServiceClient;
import com.tahaakocer.crm.dto.AccountDto;
import com.tahaakocer.crm.dto.PartyRoleDto;
import com.tahaakocer.crm.exception.GeneralException;
import com.tahaakocer.crm.mapper.AccountMapper;
import com.tahaakocer.crm.mapper.PartyRoleMapper;
import com.tahaakocer.crm.model.Account;
import com.tahaakocer.crm.model.AccountRef;
import com.tahaakocer.crm.model.Customer;
import com.tahaakocer.crm.model.PartyRole;
import com.tahaakocer.crm.repository.AccountRefRepository;
import com.tahaakocer.crm.repository.AccountRepository;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.query.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Slf4j
public class AccountService {
    private final AccountMapper accountMapper;
    private final AccountRepository accountRepository;
    private final OrderRequestServiceClient orderRequestServiceClient;
    private final PartyRoleService partyRoleService;
    private final AccountRefRepository accountRefRepository;

    public AccountService(AccountMapper accountMapper,
                          AccountRepository accountRepository,
                          OrderRequestServiceClient orderRequestServiceClient,
                          PartyRoleService partyRoleService,
                          AccountRefRepository accountRefRepository) {
        this.accountMapper = accountMapper;
        this.accountRepository = accountRepository;
        this.orderRequestServiceClient = orderRequestServiceClient;
        this.partyRoleService = partyRoleService;
        this.accountRefRepository = accountRefRepository;
    }

    public AccountDto createAccount(String orderRequestId,String name) {
        OrderRequestDto orderRequestDto = this.callOrderRequestMethod(orderRequestId);
        PartyRole partyRole = this.partyRoleService.getParyRoleEntityByOrderRequestId(orderRequestId);

        Account account = new Account();
        account.setAccountName(name);
        account.setFormattedBillingAddress(orderRequestDto.getBaseOrder().getEngagedParty().getFormattedAddress());
        account.setEndDate(LocalDateTime.now().plusYears(1));
        Account saved = this.saveAccount(account);
        AccountRef accountRef = new AccountRef();
        accountRef.setAccountCode(saved.getAccountCode());
        accountRef.setRefAccountId(saved.getId());
        accountRef.setPartyRole(partyRole);
        this.saveAccountRef(accountRef);
        return this.accountMapper.entityToDto(saved);
    }
    private OrderRequestDto callOrderRequestMethod(String orderRequestId) {
        try {
            GeneralResponse<OrderRequestDto> orderRequest = this.orderRequestServiceClient.getOrderRequest(
                    UUID.fromString(orderRequestId)).getBody();

            if (orderRequest == null || orderRequest.getCode() != 200) {
                log.error("Failed to get orderRequest from order service client");
                throw new GeneralException("Failed to get orderRequest from order service client");
            }
            return orderRequest.getData();
        } catch (Exception e) {
            log.error("Error occurred while creating customer: {}", e.getMessage());
            throw new GeneralException("Failed to get orderRequest from order service client");
        }
    }
    private Account saveAccount(Account account) {
        try {
            Account saved = this.accountRepository.save(account);
            log.info("Saving account: " + account);
            return saved;
        } catch (Exception e) {
            log.error("Error occurred while saving account: {}", e.getMessage());
            throw new GeneralException("Failed to save account");
        }
    }
    private AccountRef saveAccountRef(AccountRef accountRef) {
        try {
            AccountRef saved = this.accountRefRepository.save(accountRef);
            log.info("Saving account: " + accountRef);
            return saved;
        } catch (Exception e) {
            log.error("Error occurred while saving accountRef: {}", e.getMessage());
            throw new GeneralException("Failed to save accountRef");
        }
    }
}
