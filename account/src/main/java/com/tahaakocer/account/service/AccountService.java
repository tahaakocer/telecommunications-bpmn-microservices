package com.tahaakocer.account.service;

import com.tahaakocer.account.client.OrderRequestServiceClient;
import com.tahaakocer.account.client.PartyRoleServiceClient;
import com.tahaakocer.account.exception.GeneralException;
import com.tahaakocer.account.mapper.AccountMapper;
import com.tahaakocer.account.model.*;
import com.tahaakocer.account.repository.AccountRepository;
import com.tahaakocer.commondto.crm.AccountDto;
import com.tahaakocer.commondto.order.AccountRefDto;
import com.tahaakocer.commondto.order.OrderRequestDto;
import com.tahaakocer.commondto.order.OrderUpdateDto;
import com.tahaakocer.commondto.request.GeneralOrderRequest;
import com.tahaakocer.commondto.response.GeneralResponse;
import com.tahaakocer.commondto.response.OrderRequestResponse;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.query.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class AccountService {
    private final AccountRepository accountRepository;
    private final OrderRequestServiceClient orderRequestServiceClient;
    private final ContactMediumService contactMediumService;
    private final BillingAccountSacInfoService billingAccountSacInfoService;
    private final BillingAccountService billingAccountService;
    private final PartyRoleRefService partyRoleRefService;
    private final PartyRoleServiceClient partyRoleServiceClient;
    private final AccountMapper accountMapper;

    public AccountService(AccountRepository accountRepository,
                          OrderRequestServiceClient orderRequestServiceClient,
                          ContactMediumService contactMediumService,
                          BillingAccountSacInfoService billingAccountSacInfoService,
                          BillingAccountService billingAccountService,
                          PartyRoleRefService partyRoleRefService,
                          PartyRoleServiceClient partyRoleServiceClient,
                          AccountMapper accountMapper) {
        this.accountRepository = accountRepository;
        this.orderRequestServiceClient = orderRequestServiceClient;
        this.contactMediumService = contactMediumService;
        this.billingAccountSacInfoService = billingAccountSacInfoService;
        this.billingAccountService = billingAccountService;
        this.partyRoleRefService = partyRoleRefService;
        this.partyRoleServiceClient = partyRoleServiceClient;
        this.accountMapper = accountMapper;
    }

    public AccountDto createAccount(String orderRequestId) {
        OrderRequestDto orderRequestDto = this.callOrderRequestMethod(orderRequestId);
        Account account = new Account();
        account.setReasor("NEW");
        Account savedAccount = this.saveAccount(account);
        List<ContactMedium> contactMedia =
                this.contactMediumService.createContactMediumWithOrder(orderRequestDto, savedAccount);
        BillingAccountSacInfo billingAccountSacInfo =
                this.billingAccountSacInfoService.createBillingAccountSacInfoByOrder(orderRequestDto, savedAccount);
        BillingAccount billingAccount =
                this.billingAccountService.createBillingAccountByAccount(savedAccount);

        PartyRoleRef savedPartyRoleRef = this.partyRoleRefService.createPartyRoleRefByAccount(
                orderRequestDto.getBaseOrder().getPartyRoleRef().getRefPartyRoleId(),
                savedAccount
        );
        this.createOrderAccountRef(orderRequestId,savedAccount);
        this.callCreateAccountRefMethod(orderRequestId);

        savedAccount.setContactMedia(contactMedia);
        savedAccount.setBillingAccount(billingAccount);
        savedAccount.setBillingAccountSacInfo(billingAccountSacInfo);
        savedAccount.setPartyRoleRef(savedPartyRoleRef);
        return this.accountMapper.entityToDto(savedAccount);
    }
    private void createOrderAccountRef(String orderRequestId, Account account) {
        AccountRefDto orderAccountRefDto = new AccountRefDto();
        orderAccountRefDto.setAccountCode(account.getAccountCode());
        orderAccountRefDto.setRefAccountId(account.getId());
        OrderUpdateDto orderUpdateDto = new OrderUpdateDto();
        orderUpdateDto.setAccountRefs(List.of(orderAccountRefDto));

        OrderRequestResponse orderRequestResponse = this.callUpdateOrderRequestMethod(orderRequestId, orderUpdateDto);
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
    private OrderRequestResponse callUpdateOrderRequestMethod(String orderRequestId, OrderUpdateDto orderUpdateDto) {
        try {
            GeneralResponse<OrderRequestResponse> orderRequest = this.orderRequestServiceClient.updateOrderRequest(
                    UUID.fromString(orderRequestId), orderUpdateDto).getBody();

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
    private com.tahaakocer.commondto.crm.AccountRefDto callCreateAccountRefMethod(String orderRequestId) {
        try {
            ResponseEntity<GeneralResponse<com.tahaakocer.commondto.crm.AccountRefDto>> accountRefDtoGeneralResponse =
                    this.partyRoleServiceClient.createAccountRef(
                            GeneralOrderRequest.builder().orderRequestId(orderRequestId).build());
            GeneralResponse<com.tahaakocer.commondto.crm.AccountRefDto> body = accountRefDtoGeneralResponse.getBody();
            if (body == null || body.getCode() != 200) {
                log.error("Failed to get orderRequest from order service client");
                throw new GeneralException("Failed to get orderRequest from order service client");
            }
            return body.getData();
        } catch (Exception e) {
            log.error("Error occurred while creating customer: {}", e.getMessage());
            throw new GeneralException("Failed to get orderRequest from order service client");
        }
    }

}
