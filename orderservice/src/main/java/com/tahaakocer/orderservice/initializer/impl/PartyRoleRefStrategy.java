package com.tahaakocer.orderservice.initializer.impl;

import com.tahaakocer.commondto.order.OrderUpdateDto;
import com.tahaakocer.commondto.order.PartyRoleRefDto;
import com.tahaakocer.orderservice.exception.GeneralException;
import com.tahaakocer.orderservice.initializer.OrderUpdateStrategy;
import com.tahaakocer.orderservice.mapper.PartyRoleRefMapper;
import com.tahaakocer.orderservice.model.OrderRequest;
import com.tahaakocer.orderservice.model.PartyRoleRef;
import com.tahaakocer.orderservice.repository.mongo.OrderRequestRepository;
import com.tahaakocer.orderservice.utils.KeycloakUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
@Slf4j
public class PartyRoleRefStrategy implements OrderUpdateStrategy {
    private final PartyRoleRefMapper partyRoleRefMapper;

    private final OrderRequestRepository orderRequestRepository;
    public PartyRoleRefStrategy(PartyRoleRefMapper partyRoleRefMapper,
                                OrderRequestRepository orderRequestRepository) {
        this.partyRoleRefMapper = partyRoleRefMapper;
        this.orderRequestRepository = orderRequestRepository;
    }

    @Override
    public boolean canHandle(OrderUpdateDto updateDTO) {
        return updateDTO.getPartyRoleRef() != null;
    }

    @Override
    public boolean objectStatus(OrderRequest order) {
        return order.getBaseOrder().getPartyRoleRef() != null;
    }

    @Override
    public void update(OrderRequest order, OrderUpdateDto updateDTO) {
        if(!canHandle(updateDTO)) return;
        try{
            this.partyRoleRefMapper.updatePartyRoleRefFromDto(
                    order.getBaseOrder().getPartyRoleRef(),
                    updateDTO.getPartyRoleRef()
            );
            order.getBaseOrder().setLastModifiedBy(KeycloakUtil.getKeycloakUsername());
            order.getBaseOrder().setUpdateDate(LocalDateTime.now());
            order.setLastModifiedBy(order.getBaseOrder().getLastModifiedBy());
            order.setUpdateDate(order.getBaseOrder().getUpdateDate());
            this.orderRequestRepository.save(order);
        } catch (Exception e) {
            log.error("Failed to update order: {}", e.getMessage());
            throw new GeneralException("Failed to update order: " + e.getMessage());
        }
    }

    @Override
    public void create(OrderRequest order, OrderUpdateDto updateDTO) {
        if(!canHandle(updateDTO)) return;

        PartyRoleRef partyRoleRef;
        PartyRoleRefDto partyRoleRefDto;
        try {
            partyRoleRefDto = updateDTO.getPartyRoleRef();
            partyRoleRef = new PartyRoleRef();
            this.partyRoleRefMapper.updatePartyRoleRefFromDto(partyRoleRef, partyRoleRefDto);
            partyRoleRef.setId(UUID.randomUUID());
            LocalDateTime now = LocalDateTime.now();
            String username = KeycloakUtil.getKeycloakUsername();
            order.getBaseOrder().setPartyRoleRef(partyRoleRef);
            order.getBaseOrder().setLastModifiedBy(username);
            order.getBaseOrder().setUpdateDate(now);
            order.setLastModifiedBy(username);
            order.setUpdateDate(now);
            this.orderRequestRepository.save(order);
            log.info("PartyRoleRef created successfully");
        }catch (Exception e) {
            log.error("PartyRoleRef creation failed: {}", e.getMessage());
            throw new GeneralException("PartyRoleRef creation failed: " + e.getMessage());
        }

    }
}
