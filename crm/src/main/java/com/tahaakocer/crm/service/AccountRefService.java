package com.tahaakocer.crm.service;

import com.tahaakocer.commondto.crm.AccountRefDto;
import com.tahaakocer.commondto.order.OrderRequestDto;
import com.tahaakocer.commondto.response.GeneralResponse;
import com.tahaakocer.crm.client.OrderRequestServiceClient;
import com.tahaakocer.crm.exception.GeneralException;
import com.tahaakocer.crm.mapper.AccountRefMapper;
import com.tahaakocer.crm.model.AccountRef;
import com.tahaakocer.crm.model.PartyRole;
import com.tahaakocer.crm.repository.AccountRefRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class AccountRefService {
    private final AccountRefRepository accountRefRepository;
    private final OrderRequestServiceClient orderRequestServiceClient;
    private final AccountRefMapper accountRefMapper;
    private final PartyRoleService partyRoleService;

    public AccountRefService(AccountRefRepository accountRefRepository,
                             OrderRequestServiceClient orderRequestServiceClient,
                             AccountRefMapper accountRefMapper,
                             PartyRoleService partyRoleService) {
        this.accountRefRepository = accountRefRepository;
        this.orderRequestServiceClient = orderRequestServiceClient;
        this.accountRefMapper = accountRefMapper;
        this.partyRoleService = partyRoleService;
    }
    @Transactional
    public AccountRefDto createAccountRef(String orderRequestId) {
        OrderRequestDto orderRequestDto = this.callOrderRequestMethod(orderRequestId);

        AccountRef accountRef = new AccountRef();

        // Null kontrolü ekleyin
        if (orderRequestDto.getBaseOrder() != null &&
                orderRequestDto.getBaseOrder().getAccountRef() != null) {

            accountRef.setRefAccountId(orderRequestDto.getBaseOrder().getAccountRef().getRefAccountId());
            accountRef.setAccountCode(orderRequestDto.getBaseOrder().getAccountRef().getAccountCode());
        } else {
            log.error("No account references found in order request: {}", orderRequestId);
        }

        PartyRole partyRole = this.partyRoleService.getPartyRoleEntityByOrderRequestId(orderRequestId);
        accountRef.setPartyRole(partyRole);

        AccountRef savedAccountRef = this.saveAccountRef(accountRef);
        return this.accountRefMapper.entityToDto(savedAccountRef);
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
