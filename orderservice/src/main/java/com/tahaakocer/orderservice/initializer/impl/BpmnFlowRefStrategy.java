package com.tahaakocer.orderservice.initializer.impl;

import com.tahaakocer.commondto.order.BpmnFlowRefDto;
import com.tahaakocer.commondto.order.OrderUpdateDto;
import com.tahaakocer.orderservice.exception.GeneralException;
import com.tahaakocer.orderservice.initializer.OrderUpdateStrategy;
import com.tahaakocer.orderservice.mapper.BpmnFlowRefMapper;
import com.tahaakocer.orderservice.model.mongo.BpmnFlowRef;
import com.tahaakocer.orderservice.model.mongo.OrderRequest;
import com.tahaakocer.orderservice.repository.mongo.OrderRequestRepository;
import com.tahaakocer.orderservice.utils.KeycloakUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Component
public class BpmnFlowRefStrategy implements OrderUpdateStrategy {
    private final BpmnFlowRefMapper bpmnFlowRefMapper;
    private final OrderRequestRepository orderRequestRepository;

    public BpmnFlowRefStrategy(BpmnFlowRefMapper bpmnFlowRefMapper, OrderRequestRepository orderRequestRepository) {
        this.bpmnFlowRefMapper = bpmnFlowRefMapper;
        this.orderRequestRepository = orderRequestRepository;
    }

    @Override
    public boolean canHandle(OrderUpdateDto updateDTO) {
        return updateDTO.getBpmnFlowRef() != null;
    }

    @Override
    public boolean objectStatus(OrderRequest order) {
        return order.getBaseOrder().getBpmnFlowRef() != null;
    }

    @Override
    public void update(OrderRequest order, OrderUpdateDto updateDTO) {
        try {
            updateBpmnFlowRefFromDto(order.getBaseOrder().getBpmnFlowRef(), updateDTO.getBpmnFlowRef());
            order.getBaseOrder().setLastModifiedBy(KeycloakUtil.getKeycloakUsername());
            order.getBaseOrder().setUpdateDate(LocalDateTime.now());
            order.setLastModifiedBy(order.getBaseOrder().getLastModifiedBy());
            order.setUpdateDate(order.getBaseOrder().getUpdateDate());
            this.orderRequestRepository.save(order);
        } catch (Exception e) {
            log.error("BpmnFlowRef update failed: {}", e.getMessage());
            throw new GeneralException("BpmnFlowRef update failed: " + e.getMessage());
        }
    }

    @Override
    public void create(OrderRequest orderRequest, OrderUpdateDto updateDTO) {
        if(!canHandle(updateDTO)) return;

        BpmnFlowRefDto bpmnFlowRefDto;
        BpmnFlowRef bpmnFlowRef;
        try {
            bpmnFlowRef = new BpmnFlowRef();
            bpmnFlowRef.setId(UUID.randomUUID());
            bpmnFlowRefDto = updateDTO.getBpmnFlowRef();
            updateBpmnFlowRefFromDto(bpmnFlowRef, bpmnFlowRefDto);
            orderRequest.getBaseOrder().setBpmnFlowRef(bpmnFlowRef);
            orderRequest.getBaseOrder().setLastModifiedBy(KeycloakUtil.getKeycloakUsername());
            orderRequest.getBaseOrder().setUpdateDate(LocalDateTime.now());
            orderRequest.setLastModifiedBy(orderRequest.getBaseOrder().getLastModifiedBy());
            orderRequest.setUpdateDate(orderRequest.getBaseOrder().getUpdateDate());
            this.orderRequestRepository.save(orderRequest);

        } catch (Exception e) {
            log.error("BpmnFlowRef update failed: {}", e.getMessage());
            throw new GeneralException("BpmnFlowRef update failed: " + e.getMessage());
        }
    }

    private void updateBpmnFlowRefFromDto(BpmnFlowRef target, BpmnFlowRefDto source) {
        this.bpmnFlowRefMapper.updateBpmnFlowRefFromDto(target, source);
    }
}
