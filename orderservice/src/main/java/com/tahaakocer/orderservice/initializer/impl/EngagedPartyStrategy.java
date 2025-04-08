package com.tahaakocer.orderservice.initializer.impl;

import com.tahaakocer.orderservice.dto.EngagedPartyDto;
import com.tahaakocer.orderservice.dto.update.OrderUpdateDto;
import com.tahaakocer.orderservice.exception.GeneralException;
import com.tahaakocer.orderservice.initializer.OrderUpdateStrategy;
import com.tahaakocer.orderservice.mapper.EngagedPartyMapper;
import com.tahaakocer.orderservice.model.mongo.EngagedParty;
import com.tahaakocer.orderservice.model.mongo.OrderRequest;
import com.tahaakocer.orderservice.repository.mongo.OrderRequestRepository;
import com.tahaakocer.orderservice.utils.KeycloakUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Component
public class EngagedPartyStrategy implements OrderUpdateStrategy {

    private final EngagedPartyMapper engagedPartyMapper;
    private final OrderRequestRepository orderRequestRepository;

    public EngagedPartyStrategy(EngagedPartyMapper engagedPartyMapper,
                                OrderRequestRepository orderRequestRepository) {
        this.engagedPartyMapper = engagedPartyMapper;
        this.orderRequestRepository = orderRequestRepository;
    }

    @Override
    public boolean canHandle(OrderUpdateDto updateDTO) {
        return updateDTO.getEngagedParty() != null;
    }

    @Override
    public boolean objectStatus(OrderRequest orderRequest) {
        return orderRequest.getBaseOrder().getEngagedParty() != null;
    }

    @Override
    public void update(OrderRequest orderRequest, OrderUpdateDto updateDTO) {
        if (!canHandle(updateDTO)) return;

        try {
         updateEngagedPartyFromDto(orderRequest.getBaseOrder().getEngagedParty(), updateDTO.getEngagedParty());
         orderRequest.getBaseOrder().setLastModifiedBy(orderRequest.getBaseOrder().getEngagedParty().getLastModifiedBy());
         orderRequest.getBaseOrder().setUpdateDate(orderRequest.getBaseOrder().getEngagedParty().getUpdateDate());
         orderRequest.setUpdateDate(orderRequest.getBaseOrder().getEngagedParty().getUpdateDate());
         orderRequest.setLastModifiedBy(orderRequest.getBaseOrder().getEngagedParty().getLastModifiedBy());
         this.orderRequestRepository.save(orderRequest);
        } catch (Exception e) {
            throw new GeneralException("Engaged Party update failed: " + e.getMessage());
        }
    }

    @Override
    public void create(OrderRequest orderRequest, OrderUpdateDto updateDTO) {
        if (!canHandle(updateDTO)) return;

        EngagedPartyDto engagedPartyDto;
        EngagedParty engagedParty;
        try {
            engagedParty = new EngagedParty();
            engagedPartyDto = updateDTO.getEngagedParty();
            updateEngagedPartyFromDto(engagedParty, engagedPartyDto);
            engagedParty.setCreatedBy(KeycloakUtil.getKeycloakUsername());
            engagedParty.setCreateDate(LocalDateTime.now());
            orderRequest.getBaseOrder().setEngagedParty(engagedParty);
            orderRequest.getBaseOrder().setUpdateDate(engagedParty.getUpdateDate());
            orderRequest.getBaseOrder().setLastModifiedBy(engagedParty.getLastModifiedBy());
            orderRequest.setLastModifiedBy(engagedParty.getLastModifiedBy());
            orderRequest.setUpdateDate(engagedParty.getUpdateDate());
            this.orderRequestRepository.save(orderRequest);
        } catch (Exception e) {
            log.error("Engaged Party creation failed: {}", e.getMessage());
            throw new GeneralException("Engaged Party creation failed: " + e.getMessage());
        }
    }

    private void updateEngagedPartyFromDto(EngagedParty target, EngagedPartyDto source) {
        this.engagedPartyMapper.updateEngagedPartyFromDto(target, source);
        target.setLastModifiedBy(KeycloakUtil.getKeycloakUsername());
        target.setUpdateDate(LocalDateTime.now());
        log.info("Engaged Party updated.");

    }

}
