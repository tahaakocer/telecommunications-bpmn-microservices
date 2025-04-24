package com.tahaakocer.orderservice.initializer.impl;

import com.tahaakocer.orderservice.dto.ActiveStatusDefinedByDto;
import com.tahaakocer.orderservice.dto.update.OrderUpdateDto;
import com.tahaakocer.orderservice.exception.GeneralException;
import com.tahaakocer.orderservice.initializer.OrderUpdateStrategy;
import com.tahaakocer.orderservice.mapper.ActiveStatusDefinedByMapper;
import com.tahaakocer.orderservice.model.mongo.ActiveStatusDefinedBy;
import com.tahaakocer.orderservice.model.mongo.OrderRequest;
import com.tahaakocer.orderservice.repository.mongo.OrderRequestRepository;
import com.tahaakocer.orderservice.utils.KeycloakUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Component
public class ActiveStatusDefinedByStrategy implements OrderUpdateStrategy {
    private final ActiveStatusDefinedByMapper activeStatusDefinedByMapper;
    private final OrderRequestRepository orderRequestRepository;

    public ActiveStatusDefinedByStrategy(ActiveStatusDefinedByMapper activeStatusDefinedByMapper, OrderRequestRepository orderRequestRepository) {
        this.activeStatusDefinedByMapper = activeStatusDefinedByMapper;
        this.orderRequestRepository = orderRequestRepository;
    }

    @Override
    public boolean canHandle(OrderUpdateDto updateDTO) {
        return updateDTO.getActiveStatusDefinedBy() != null;
    }

    @Override
    public boolean objectStatus(OrderRequest order) {
        return order.getActiveStatusDefinedBy() != null;
    }

    @Override
    public void update(OrderRequest order, OrderUpdateDto updateDTO) {
        try{
            updateActiveStatusDefinedByFromDto(order.getActiveStatusDefinedBy(), updateDTO.getActiveStatusDefinedBy());
            order.setLastModifiedBy(KeycloakUtil.getKeycloakUsername());
            order.setUpdateDate(LocalDateTime.now());
            this.orderRequestRepository.save(order);
        }
        catch (Exception e) {
            log.error("Error while updating ActiveStatusDefinedBy:" + e.getMessage());
            throw new GeneralException("Error while updating ActiveStatusDefinedBy: " + e.getMessage());
        }
    }

    @Override
    public void create(OrderRequest order, OrderUpdateDto updateDTO) {
        if(!canHandle(updateDTO)) return;

        ActiveStatusDefinedBy activeStatusDefinedBy;
        ActiveStatusDefinedByDto activeStatusDefinedByDto;
        try {
            activeStatusDefinedBy = new ActiveStatusDefinedBy();
            activeStatusDefinedBy.setId(UUID.randomUUID());
            activeStatusDefinedByDto = updateDTO.getActiveStatusDefinedBy();
            this.updateActiveStatusDefinedByFromDto(activeStatusDefinedBy, activeStatusDefinedByDto);
            order.setActiveStatusDefinedBy(activeStatusDefinedBy);
            order.setLastModifiedBy(KeycloakUtil.getKeycloakUsername());
            order.setUpdateDate(LocalDateTime.now());
            this.orderRequestRepository.save(order);
        }catch (Exception e) {
            log.error("Error while creating ActiveStatusDefinedBy:" + e.getMessage());
            throw new GeneralException("Error while creating ActiveStatusDefinedBy: " + e.getMessage());
        }
    }

    private void updateActiveStatusDefinedByFromDto(ActiveStatusDefinedBy target, ActiveStatusDefinedByDto source) {
      this.activeStatusDefinedByMapper.updateActiveStatusDefinedByFromDto(target,source);
    }
}
