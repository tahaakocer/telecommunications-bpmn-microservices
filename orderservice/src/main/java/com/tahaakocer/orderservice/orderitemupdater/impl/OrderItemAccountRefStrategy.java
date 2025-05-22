package com.tahaakocer.orderservice.orderitemupdater.impl;

import com.tahaakocer.commondto.order.OrderUpdateDto;
import com.tahaakocer.orderservice.exception.GeneralException;
import com.tahaakocer.orderservice.mapper.AccountRefMapper;
import com.tahaakocer.orderservice.model.mongo.AccountRef;
import com.tahaakocer.orderservice.model.mongo.BaseOrderItem;
import com.tahaakocer.orderservice.orderitemupdater.OrderItemUpdateStrategy;
import com.tahaakocer.orderservice.repository.mongo.OrderItemRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Slf4j
public class OrderItemAccountRefStrategy implements OrderItemUpdateStrategy {
    private final OrderItemRepository orderItemRepository;
    private final AccountRefMapper accountRefMapper;

    public OrderItemAccountRefStrategy(OrderItemRepository orderItemRepository,
                                       AccountRefMapper accountRefMapper) {
        this.orderItemRepository = orderItemRepository;
        this.accountRefMapper = accountRefMapper;
    }

    @Override
    public boolean canHandle(OrderUpdateDto updateDTO) {
        return updateDTO.getAccountRef() != null;
    }

    @Override
    public boolean objectStatus(BaseOrderItem orderItem) {
        return orderItem.getAccountRef() != null;
    }

    @Override
    public void update(BaseOrderItem orderItem, OrderUpdateDto updateDTO) {
        if (!canHandle(updateDTO)) return;
        try {
            AccountRef accountRef = orderItem.getAccountRef();
            this.accountRefMapper.updateAccountRefFromDto(accountRef,updateDTO.getAccountRef());
            this.setBaseModelUpdateProperties(orderItem);
            this.orderItemRepository.save(orderItem);
        } catch (Exception e) {
            log.error("Error while updating Order Item status: " + e.getMessage());
            throw new GeneralException("Error while updating Order Item status: " + e.getMessage());
        }
    }

    @Override
    public void create(BaseOrderItem orderItem, OrderUpdateDto updateDTO) {
        if (!canHandle(updateDTO)) return;
        try {
            AccountRef accountRef = new AccountRef();
            accountRef.setId(UUID.randomUUID());
            accountRef.setRefAccountId(updateDTO.getAccountRef().getRefAccountId());
            accountRef.setAccountCode(updateDTO.getAccountRef().getAccountCode());
            orderItem.setAccountRef(accountRef);
            this.setBaseModelCreateProperties(orderItem);
            this.orderItemRepository.save(orderItem);
        } catch (Exception e) {
            log.error("Error occurred while creating accountRef: {}", e.getMessage());
            throw new GeneralException("Error occurred while creating accountRef: " + e.getMessage());
        }
    }
}
