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
        return updateDTO.getAccountRef() != null;
    }

    @Override
    public boolean objectStatus(OrderRequest order) {
        return order.getBaseOrder().getAccountRef() != null;
    }

    @Override
    public void update(OrderRequest order, OrderUpdateDto updateDTO) {

        if (!canHandle(updateDTO)) return;
        try {
           AccountRef accountRef = order.getBaseOrder().getAccountRef();
           this.accountRefMapper.updateAccountRefFromDto(accountRef, updateDTO.getAccountRef());
           this.setBaseOrderUpdateProperties(order);
           this.setOrderRequestUpdateProperties(order);
           this.orderRequestRepository.save(order);
           log.info("Order updated successfully");
        } catch (Exception e) {
            log.error("Account Ref update failed: {}", e.getMessage());
            throw new GeneralException("Account Ref update failed: " + e.getMessage());
        }

    }

    @Override
    public void create(OrderRequest order, OrderUpdateDto updateDTO) {
        if (!canHandle(updateDTO)) return;
        try {
            AccountRef accountRef = new AccountRef();
            accountRef.setId(UUID.randomUUID());
            accountRef.setRefAccountId(updateDTO.getAccountRef().getRefAccountId());
            accountRef.setAccountCode(updateDTO.getAccountRef().getAccountCode());
            order.getBaseOrder().setAccountRef(accountRef);
            this.setOrderRequestCreateProperties(order);
            this.setBaseOrderCreateProperties(order);
            this.orderRequestRepository.save(order);
            log.info("Account Ref created: {}", accountRef);
        } catch (Exception e) {
            log.error("Account Ref creation failed: {}", e.getMessage());
            throw new GeneralException("Account Ref creation failed: " + e.getMessage());
        }
    }

}
