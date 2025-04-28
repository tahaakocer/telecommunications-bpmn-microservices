package com.tahaakocer.orderservice.initializer.impl;

import com.tahaakocer.commondto.order.AccountRefDto;
import com.tahaakocer.commondto.order.OrderUpdateDto;
import com.tahaakocer.orderservice.exception.GeneralException;
import com.tahaakocer.orderservice.initializer.OrderUpdateStrategy;
import com.tahaakocer.orderservice.mapper.AccountRefMapper;
import com.tahaakocer.orderservice.model.mongo.AccountRef;
import com.tahaakocer.orderservice.model.mongo.OrderRequest;
import com.tahaakocer.orderservice.repository.mongo.OrderRequestRepository;
import com.tahaakocer.orderservice.utils.KeycloakUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Component
public class AccountRefStrategy implements OrderUpdateStrategy {
    private final AccountRefMapper accountRefMapper;
    private final OrderRequestRepository orderRequestRepository;
    public AccountRefStrategy(AccountRefMapper accountRefMapper,
                              OrderRequestRepository orderRequestRepository) {
        this.accountRefMapper = accountRefMapper;
        this.orderRequestRepository = orderRequestRepository;
    }

    @Override
    public boolean canHandle(OrderUpdateDto updateDTO) {
        return updateDTO.getAccountRefs() != null && !updateDTO.getAccountRefs().isEmpty();
    }

    @Override
    public boolean objectStatus(OrderRequest order) {
        return order.getBaseOrder().getAccountRefs() != null && !order.getBaseOrder().getAccountRefs().isEmpty();
    }

    @Override
    public void update(OrderRequest order, OrderUpdateDto updateDTO) {
        if (!canHandle(updateDTO)) return;
        try {
            List<AccountRef> existingAccountRefs = order.getBaseOrder().getAccountRefs();
            List<AccountRefDto> updatedAccountRefs = updateDTO.getAccountRefs();

            Map<UUID,AccountRef> accountRefMap = existingAccountRefs.stream()
                    .collect(Collectors.toMap(AccountRef::getId, Function.identity()));

            for(AccountRefDto accountRefDto : updatedAccountRefs) {
                if(accountRefDto.getId() != null && accountRefMap.containsKey(accountRefDto.getId())) {
                    AccountRef existingAccountRef = accountRefMap.get(accountRefDto.getId());
                    updateAccountRefsFromDto(existingAccountRef,accountRefDto);
                } else {
                    AccountRef newAccountRef = new AccountRef();
                    newAccountRef.setId(UUID.randomUUID());
                    updateAccountRefsFromDto(newAccountRef, accountRefDto);
                    existingAccountRefs.add(newAccountRef);
                }
            }

            if (updateDTO.isRemoveUnlistedAccountRefs()) {
                List<UUID> updatedIds = updatedAccountRefs.stream()
                        .map(AccountRefDto::getId)
                        .filter(Objects::nonNull)
                        .toList();

                existingAccountRefs.removeIf(accountRef ->
                        accountRef.getId() != null && !updatedIds.contains(accountRef.getId()));
            }

            updateBaseProperties(order);
        }catch (Exception e) {
            log.error("Account Ref update failed: {}", e.getMessage());
            throw new GeneralException("Account Ref update failed: " + e.getMessage());
        }

    }

    @Override
    public void create(OrderRequest order, OrderUpdateDto updateDTO) {
        if(!canHandle(updateDTO)) return;
        try {
            List<AccountRef> accountRefs = new ArrayList<>();

            for(AccountRefDto accountRefDto : updateDTO.getAccountRefs()) {
                AccountRef accountRef = new AccountRef();
                accountRef.setId(UUID.randomUUID());
                updateAccountRefsFromDto(accountRef, accountRefDto);
                accountRefs.add(accountRef);
            }
            order.getBaseOrder().setAccountRefs(accountRefs);
            updateBaseProperties(order);
            log.info("Account Ref created successfully");
        }catch (Exception e) {
            log.error("Account Ref creation failed: {}", e.getMessage());
            throw new GeneralException("Account Ref creation failed: " + e.getMessage());
        }
    }
    private void updateBaseProperties(OrderRequest orderRequest) {
        LocalDateTime now = LocalDateTime.now();
        String username = KeycloakUtil.getKeycloakUsername();

        orderRequest.getBaseOrder().setUpdateDate(now);
        orderRequest.getBaseOrder().setLastModifiedBy(username);
        orderRequest.setUpdateDate(now);
        orderRequest.setLastModifiedBy(username);

        this.orderRequestRepository.save(orderRequest);
    }
    private void updateAccountRefsFromDto(AccountRef target, AccountRefDto source) {
        this.accountRefMapper.updateAccountRefFromDto(target, source);
    }
}
